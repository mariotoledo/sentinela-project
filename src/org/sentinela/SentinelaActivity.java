package org.sentinela;

import android.app.Activity;
import android.widget.Button;
import android.os.Bundle;
import android.view.View.OnClickListener;
import android.view.View;

public class SentinelaActivity extends Activity {
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Button connectButton = (Button) findViewById(R.id.connectButton);
        
        connectButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            	connectBluetooth();
                //v.setVisibility(View.GONE);
            }
        });
    }
    
    private void connectBluetooth(){
    	//conect bluetooh
    }
}