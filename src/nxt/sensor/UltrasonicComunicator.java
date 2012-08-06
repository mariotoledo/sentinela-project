package nxt.sensor;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.CharBuffer;
import java.util.UUID;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;


public class UltrasonicComunicator {
	
	public UltrasonicComunicator(){}
	
	public void writeMessage(byte msg, String nxt, BluetoothSocket socket) throws InterruptedException{
	    BluetoothSocket connSock;

	    //Swith nxt socket
	    if(nxt.equals("nxt2")){
	        connSock=socket;
	    }else if(nxt.equals("nxt1")){
	        connSock=socket;
	    }else{
	        connSock=null;
	    }

	    if(connSock!=null){
	        try {

	            OutputStreamWriter out=new OutputStreamWriter(connSock.getOutputStream());
	            out.write(msg);
	            out.flush();

	            Thread.sleep(1000);


	        } catch (IOException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }
	    }else{
	        //Error
	    }
	}

	public byte[] readMessage(BluetoothSocket socket){
	    char[] buffer = null;
	    //Swith nxt socket

	    if(socket!=null){
	        try {

	            InputStreamReader in=new InputStreamReader(socket.getInputStream());
	            in.read(buffer);
	            
	            byte[] aux = new byte[buffer.length];
	            for(int i = 0; i < aux.length; i++)
	            {
	            	aux[i] = (byte)buffer[i];
	            }
	            
	            return aux;


	        } catch (IOException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	            return null;
	        }
	    }else{
	        Log.d(">>>>>>>>> ", "socket NULL! ");
	        return null;
	    }

	}

}
