package org.sentinela;

import org.sentinela.R;
import java.util.ArrayList;

import android.app.Activity;
import android.bluetooth.*;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View.OnClickListener;
import android.view.View;

public class SentinelaActivity extends Activity {
	private static final int bluetooth_request = 1;
    private static final int device_request = 2;
    
    BluetoothAdapter bluetoothAdapter;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Button connectButton = (Button) findViewById(R.id.connectButton);
        
        connectButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            	connectBluetooth();
            }
        });
        
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }
    
    private void connectBluetooth(){
    	if (bluetoothAdapter == null) {
    	    //Aparelho n�o suporta bluetooth
    		System.out.println("Aparelho nao suporta bluetooth");
    	}
    	
    	//Habilitando o bluetooth
    	if (!bluetoothAdapter.isEnabled()) {
    	    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
    	    startActivityForResult(enableBtIntent, bluetooth_request);
    	}
    	else {
    		openChooseDeviceActivity();
    	}
    }
    
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
        	//retorno da requisi��o de ligar o bluetooth (caso ele n�o estivesse ligado)
        	case bluetooth_request:
        		if (resultCode == Activity.RESULT_OK) {
        			openChooseDeviceActivity();
        		}
        		break;
        	//Retorno da escolha do dispositivo, com o endere�o do dispositivo
        	case device_request:
        		if (resultCode == Activity.RESULT_OK) {
        			String endereco = data.getExtras().getString("device_address");
        			
        			Intent i = new Intent(SentinelaActivity.this, ChooseAction.class);
        			i.putExtra("adress", endereco);
        			
        			startActivity(i);
        		}
        		break;
        }
    }
    
    @Override
	 protected void onDestroy() {
	     super.onDestroy();
	     bluetoothAdapter.disable();
	 }
    
    private void openChooseDeviceActivity(){
    	//mandando abrir a activity para escolha do dispositivo
    	Intent intent = new Intent(this, ConnectDeviceActivity.class);
        startActivityForResult(intent, device_request);
    }
}