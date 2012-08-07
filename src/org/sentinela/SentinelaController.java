																																																																																																																																																																																							package org.sentinela;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.*;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.util.logging.Level;
import java.util.logging.Logger;
import nxt.sensor.Ultrasonic;
import org.sentinela.R;
import nxt.sentinela.Sentinela;
import nxt.sentinela.SentinelaComunicator;

class Exec extends AsyncTask<String, Void, String> {
    Ultrasonic us;
    
    Exec(Ultrasonic us) {
        this.us = us;																																																			
    }

	@Override
	protected String doInBackground(String... arg0) {
		while(true){
			try {
				Thread.sleep(300);
				us.checkDistance();
			} catch (InterruptedException ex) {
				Logger.getLogger(Exec.class.getName()).log(Level.SEVERE, null, ex);
				return null;
			}
		
		}
	}
}

public class SentinelaController extends Activity{
	
	public static final int MESSAGE_TOAST = 0;
	public static final String TOAST = null;
	public static final int MESSAGE_STATE_CHANGE = 1;
	
    public Sentinela robo;
    public Ultrasonic us;
        
	public SentinelaComunicator sc;
	BluetoothDevice device;
	
	Button moveFoward;
	Button moveLeft;
	Button moveBackward;
	Button moveRight;
    Button speedMore;
    Button speedLess;
    TextView txSpeed;
    
    BluetoothAdapter bluetoothAdapter;
    
    public void onDestroy(){
    	bluetoothAdapter.disable();
    }
	
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.sentinela_controller);
	    
    	sc = new SentinelaComunicator(mHandler);
    	Bundle extras = getIntent().getExtras();
    	String adress = extras.getString("adress");
    	
    	bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
	        
    	BluetoothDevice device = bluetoothAdapter.getRemoteDevice(adress);
	        
    	sc.connect(device);
    	robo = new Sentinela(sc);
    	us = new Ultrasonic(robo, sc, 0);
            
    	Exec exec = new Exec(us);
    	exec.execute(new String[] {null});

	    moveFoward = (Button) findViewById(R.id.moveFowardButton);
	    moveLeft = (Button) findViewById(R.id.moveLeftButton);
	    moveBackward = (Button) findViewById(R.id.moveBackwardButton);
	    moveRight = (Button) findViewById(R.id.moveRightButton);
	    speedMore = (Button) findViewById(R.id.speedMore);
	    speedLess = (Button) findViewById(R.id.speedLess);
	    txSpeed = (TextView) findViewById(R.id.txSpeedView);
	    txSpeed.setText(Integer.toString(robo.getSpeed()));
	    
	    //speedMore.setOnTouchListener(new View.OnTouchListener() {
	   // @Override
	    	/*public boolean onTouch(View arg0, MotionEvent event) {
	    		if(event.getAction() == MotionEvent.ACTION_DOWN){
	    			robo.forward();
	    		}
	    		if(event.getAction() == MotionEvent.ACTION_UP){
	    			robo.stop();
	    		}
	    	
	    		return true;
	    	}
	    });
	    
	    speedLess.setOnTouchListener(new View.OnTouchListener() {
	    	@Override
	    	public boolean onTouch(View arg0, MotionEvent event) {
	    		if(event.getAction() == MotionEvent.ACTION_DOWN){
	    			robo.setSpeed(-10);
	    		}
	    		txSpeed.setText(Integer.toString(robo.getSpeed()));
	    		return true;
	    	}
	    });
	        
	    moveFoward.setOnTouchListener(new View.OnTouchListener() {
	    	@Override
	    	public boolean onTouch(View arg0, MotionEvent event) {
	    		if(event.getAction() == MotionEvent.ACTION_DOWN){
	    			robo.forward();
	    		}
	    		if(event.getAction() == MotionEvent.ACTION_UP){
	    			robo.stop();
	    		}
	    		return true;
	    	}
	    });
	    
	    moveBackward.setOnTouchListener(new View.OnTouchListener() {
	    	@Override
	    	public boolean onTouch(View arg0, MotionEvent event) {
	    		if(event.getAction() == MotionEvent.ACTION_DOWN){
	    			robo.backward();
	    		}
	    		if(event.getAction() == MotionEvent.ACTION_UP){
	    			robo.stop();
	    		}
	    		return true;
	    	}
	    });
	    
	    moveRight.setOnTouchListener(new View.OnTouchListener() {
	    	@Override
	    	public boolean onTouch(View arg0, MotionEvent event) {
	    		if(event.getAction() == MotionEvent.ACTION_DOWN){
	    			robo.right();
	    		}
	    		if(event.getAction() == MotionEvent.ACTION_UP){
	    			robo.stop();
	    		}
	    		return true;
	    	}
	    });
        
	    moveLeft.setOnTouchListener(new View.OnTouchListener() {
	    	@Override
	    	public boolean onTouch(View arg0, MotionEvent event) {
	    		if(event.getAction() == MotionEvent.ACTION_DOWN){
	    			robo.left();
	    		}
	    		if(event.getAction() == MotionEvent.ACTION_UP){
	    			robo.stop();
	    		}
	    		return true;
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
	 
	 @Override
	 protected void onStop() {
	     super.onStop();
	     sc.stop();
	     bluetoothAdapter.cancelDiscovery();
	 }
}