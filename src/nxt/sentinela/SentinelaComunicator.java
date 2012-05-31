package nxt.sentinela;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import org.sentinela.Constants;
import org.sentinela.SentinelaController;

public class SentinelaComunicator implements Constants {

    public static final int STATE_NONE = 0;
    public static final int STATE_CONNECTING = 1;
    public static final int STATE_CONNECTED = 2;
	    
    private int mState;
    private Handler mHandler;
    private BluetoothAdapter mAdapter;
    
    private ConnectThread mConnectThread;
    private ConnectedThread mConnectedThread;
    
    public SentinelaComunicator(Handler handler) {
        mAdapter = BluetoothAdapter.getDefaultAdapter();
        mHandler = handler;
        setState(STATE_NONE);
    }

    private synchronized void setState(int state) {
        mState = state;
        if (mHandler != null) {
            mHandler.obtainMessage(SentinelaController.MESSAGE_STATE_CHANGE, state, -1).sendToTarget();
        } else {
            //XXX
        }
    }
    
    public synchronized int getState() {
        return mState;
    }
    
    public synchronized void setHandler(Handler handler) {
        mHandler = handler;
    }
    
    private void toast(String text) {
        if (mHandler != null) {
            Message msg = mHandler.obtainMessage(SentinelaController.MESSAGE_TOAST);
            Bundle bundle = new Bundle();
            bundle.putString(SentinelaController.TOAST, text);
            msg.setData(bundle);
            mHandler.sendMessage(msg);
        } else {
            //XXX
        }
    }

    public synchronized void connect(BluetoothDevice device) {
        //Log.i("NXT", "NXTTalker.connect()");
        
        if (mState == STATE_CONNECTING) {
            if (mConnectThread != null) {
                mConnectThread.cancel();
                mConnectThread = null;
            }
        }
        
        if (mConnectedThread != null) {
            mConnectedThread.cancel();
            mConnectedThread = null;
        }
        
        mConnectThread = new ConnectThread(device);
        mConnectThread.start();
        setState(STATE_CONNECTING);
    }
    
    public synchronized void connected(BluetoothSocket socket, BluetoothDevice device) {
        if (mConnectThread != null) {
            mConnectThread.cancel();
            mConnectThread = null;
        }
        
        if (mConnectedThread != null) {
            mConnectedThread.cancel();
            mConnectedThread = null;
        }
        
        mConnectedThread = new ConnectedThread(socket);
        mConnectedThread.start();
        
        //toast("Connected to " + device.getName());
        
        setState(STATE_CONNECTED);
    }
    
    public synchronized void stop() {
        if (mConnectThread != null) {
            mConnectThread.cancel();
            mConnectThread = null;
        }
        
        if (mConnectedThread != null) {
            mConnectedThread.cancel();
            mConnectedThread = null;
        }
        setState(STATE_NONE);
    }
    
    private void connectionFailed() {
        setState(STATE_NONE);
        //toast("Connection failed");
    }
    
    private void connectionLost() {
        setState(STATE_NONE);
        //toast("Connection lost");
    }
     
    private void write(byte[] out) {
        ConnectedThread r;
        synchronized (this) {
            if (mState != STATE_CONNECTED) {
                return;
            }
            r = mConnectedThread;
        }
        r.write(out);
    }
    
    private int read(byte[] out) {
        ConnectedThread r;
        synchronized (this) {
            if (mState != STATE_CONNECTED) {
                return -1;
            }
            r = mConnectedThread;
        }
        try {
            return r.mmInStream.read(out);
        } catch(IOException e) {return -1;}
    }
    
    private int read() {
        ConnectedThread r;
        synchronized (this) {
            if (mState != STATE_CONNECTED) {
                return -1;
            }
            r = mConnectedThread;
        }
        try {
            return r.mmInStream.read();
        } catch(IOException e) {return -1;}
    }
    
    private class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;
        
        public ConnectThread(BluetoothDevice device) {
            mmDevice = device;
            BluetoothSocket tmp = null;
            
            try {
                tmp = device.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            mmSocket = tmp;
        }
        
        public void run() {
            setName("ConnectThread");
            mAdapter.cancelDiscovery();
            
            try {
                mmSocket.connect();
            } catch (IOException e) {
                connectionFailed();
                try {
                    mmSocket.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                return;
            }
            
            synchronized (SentinelaComunicator.this) {
                mConnectThread = null;
            }
            
            connected(mmSocket, mmDevice);
        }
        
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;
        
        public ConnectedThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;
            
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            
            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }
        
        public void run() {
            byte[] buffer = new byte[1024];
            int bytes;
            
            while (true) {
                try {
                    bytes = mmInStream.read(buffer);
                    //toast(Integer.toString(bytes) + " bytes read from device");
                } catch (IOException e) {
                    e.printStackTrace();
                    connectionLost();
                    break;
                }
            }
        }
        
        public void write(byte[] buffer) {
            try {
                mmOutStream.write(buffer);
            } catch (IOException e) {
                e.printStackTrace();
                // XXX?
            }
        }
        
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
  
    public void motor(int portId, byte power, boolean speedReg, boolean motorSync) {
        byte[] request =
        { 0x0c, 0x00,
        DIRECT_COMMAND_NOREPLY, SET_OUTPUT_STATE, (byte)portId,
        power, (byte)mode, (byte)regulationMode,
        (byte)turnRatio, (byte)runState, (byte)tachoLimit,
        (byte)(tachoLimit >>> 8), (byte)(tachoLimit >>> 16),
        (byte)(tachoLimit >>> 24)
        };
        write(request);
    }
    
    public synchronized void sendData(byte[] command)
    {
        int lenMSB = command.length >> 8;
        int lenLSB = command.length;
        byte[] b = {(byte)lenLSB};
        write(b);
        b[0] = (byte)lenMSB;
        write(b);
        write(command);
    }

    public void LSWrite(byte portId, byte[] txData, byte rxDataLength)
    {
        byte[] request = {DIRECT_COMMAND_NOREPLY, LS_WRITE, portId, (byte)txData.length, rxDataLength};
        request = appendBytes(request, txData);
        sendData(request);
    }

    public byte[] LSRead(byte portId)
    {
        byte[] request = {DIRECT_COMMAND_REPLY, LS_READ, portId};
        sendData(request);
        byte[] reply = readData();

        byte rxLength = reply[3];
        byte[] rxData = new byte[rxLength];
        if (reply[2] == 0)
        System.arraycopy(reply, 4, rxData, 0, rxLength);
        else
        System.out.println("Error in Sensor.LSRead()\n" +
            "Illegal reply");
        return rxData;
    }

    public synchronized byte[] readData() {
        byte[] reply = null;
        int length = -1;
        int lenMSB;
        int lenLSB;

        do
            lenLSB = read();
        while (lenLSB < 0);

        lenMSB = read(); // MSB of reply length
        length = (0xFF & lenLSB) | ((0xFF & lenMSB) << 8);
        reply = new byte[length];
        // Rest of packet
        read(reply);
        return reply;
    }

    private byte[] appendBytes(byte[] array1, byte[] array2)
    {
        byte[] array = new byte[array1.length + array2.length];
        System.arraycopy(array1, 0, array, 0, array1.length);
        System.arraycopy(array2, 0, array, array1.length, array2.length);
        return array;
    }
  
    protected synchronized void setOutputState(int portId, byte power, int mode,
                                               int regulationMode, int turnRatio,
                                               int runState, long tachoLimit) {
        byte[] request = {
            DIRECT_COMMAND_NOREPLY, SET_OUTPUT_STATE, (byte)portId,
            power, (byte)mode, (byte)regulationMode,
            (byte)turnRatio, (byte)runState, (byte)tachoLimit,
            (byte)(tachoLimit >>> 8), (byte)(tachoLimit >>> 16),
            (byte)(tachoLimit >>> 24)};
        sendData(request);
    }

    public void setMode() {
        byte COMMAND_STATE = 0x41; // Command or reply length = 1
        byte DEFAULT_ADDRESS = 0x02;
        byte value = 0x01;
        byte[] txData = {DEFAULT_ADDRESS, COMMAND_STATE, CONTINUOUS_MEASUREMENT};
        int portId = 0;
        LSWrite((byte)portId, txData, (byte)0);
    }
}