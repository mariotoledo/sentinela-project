package org.sentinela;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class SentinelaController extends Activity{
	
	protected static final int MESSAGE_TOAST = 0;
	protected static final String TOAST = null;
	protected static final int MESSAGE_STATE_CHANGE = 1;
	
	SentinelaComunicator sc;
	BluetoothDevice device;
	
	Button moveFoward;
	/*Button moveLeft;
	Button moveBackward;
	Button moveRight;*/
	
	 public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.sentinela_controller);
	        
	        sc = new SentinelaComunicator(mHandler);
	        Bundle extras = getIntent().getExtras();
	        String adress = extras.getString("adress");
	        
	        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
	        
	        BluetoothDevice device = bluetoothAdapter.getRemoteDevice(adress);
	        
	        sc.connect(device);
	        
	        moveFoward = (Button) findViewById(R.id.moveFowardButton);
	        /*moveLeft = (Button) findViewById(R.id.moveLeftButton);
	        moveBackward = (Button) findViewById(R.id.moveBackwardButton);
	        moveRight = (Button) findViewById(R.id.moveRightButton);*/
	        
	        
	        moveFoward.setOnClickListener(new OnClickListener() {
	            public void onClick(View v) {
	            	sc.motor(1,(byte) 0x0c, true, true);
	            }
	        });
	        /*moveLeft.setOnClickListener(new OnClickListener() {
	            public void onClick(View v) {
	            	sc.motor(1,(byte) 0x0c, true, true);
	            }
	        });
	        moveBackward.setOnClickListener(new OnClickListener() {
	            public void onClick(View v) {
	            	sc.motor(1,(byte) 0x0c, true, true);
	            }
	        });
	        moveRight.setOnClickListener(new OnClickListener() {
	            public void onClick(View v) {
	            	sc.motor(1,(byte) 0x0c, true, true);
	            }
	        });*/
	 }
	 
	 private final Handler mHandler = new Handler() {
	        @Override
	        public void handleMessage(Message msg) {
	            switch (msg.what) {
	            case MESSAGE_TOAST:
	                Toast.makeText(getApplicationContext(), msg.getData().getString(TOAST), Toast.LENGTH_SHORT).show();
	                break;
	            case MESSAGE_STATE_CHANGE:
	                //mState = msg.arg1;
	                //displayState();
	                break;
	            }
	        }
	    };
	    
}
