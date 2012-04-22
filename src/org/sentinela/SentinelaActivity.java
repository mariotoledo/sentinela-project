package org.sentinela;

import android.app.Activity;
import android.bluetooth.*;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.os.Bundle;
import android.view.View.OnClickListener;
import android.view.View;

public class SentinelaActivity extends Activity {
	private ArrayAdapter<String> mArrayAdapter;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Button connectButton = (Button) findViewById(R.id.connectButton);
        
        mArrayAdapter = new ArrayAdapter<String>(this, 0);
        
        connectButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            	connectBluetooth();
                //v.setVisibility(View.GONE);
            }
        });
        
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        this.registerReceiver(mReceiver, filter);
        
        //filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        //this.registerReceiver(mReceiver, filter);
    }
    
    private void connectBluetooth(){
    	BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    	if (mBluetoothAdapter == null) {
    	    //Aparelho não suporta bluetooth
    	}
    	
    	//Habilitando o bluetooth
    	if (!mBluetoothAdapter.isEnabled()) {
    	    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
    	    startActivityForResult(enableBtIntent, 1);
    	}
    	
    	doDiscovery(mBluetoothAdapter);
    }
    
    private void doDiscovery(BluetoothAdapter mBluetoothAdapter) {
    	//cancela a busca atual e reinicia a busca
        if (mBluetoothAdapter.isDiscovering()) {
        	mBluetoothAdapter.cancelDiscovery();
        }
        
        //inicia a busca por dispositivos
        mBluetoothAdapter.startDiscovery();
    }
    
    //Delegate do ACTION_FOUND
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
    	@Override
    	public void onReceive(Context arg0, Intent arg1) {
            String action = arg1.getAction();
            
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = arg1.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                mArrayAdapter.add(device.getName() + "\n" + device.getAddress());
            }
        }
    };
    
}