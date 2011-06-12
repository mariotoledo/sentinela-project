package org.sentinela;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import org.opencv.samples.colorblobdetect.*;
import android.widget.Button;

public class ChooseAction extends Activity{
	
	String address;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_action);
        Button controllerButton = (Button) findViewById(R.id.controllerButton);
        Button cameraButton = (Button) findViewById(R.id.cameraButton);
        
        Bundle extras = getIntent().getExtras();
        address = extras.getString("adress");
        
        controllerButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            	Intent i = new Intent(ChooseAction.this, SentinelaController.class);
    			i.putExtra("adress", address);
    			
    			startActivity(i);
            }
        });
        
        cameraButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            	Intent i = new Intent(ChooseAction.this, ColorBlobDetectionActivity.class);
    			i.putExtra("adress", address);
    			
    			startActivity(i);
            }
        });
        
    }

}
