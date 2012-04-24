package org.sentinela;

import java.util.ArrayList;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

public class ConnectDeviceActivity extends Activity{
	
	private ArrayAdapter<String> mArrayAdapter;
	
	 public void onCreate(Bundle savedInstanceState) {
		 	super.onCreate(savedInstanceState);
	        setContentView(R.layout.device_list);
		 
		 	ArrayList<String> aus = new ArrayList<String>();
	        mArrayAdapter = new ArrayAdapter<String>(this,R.layout.device_name);
	        
	        ListView newDevicesListView = (ListView) findViewById(R.id.new_devices);
	        newDevicesListView.setAdapter(mArrayAdapter);
	        newDevicesListView.setOnItemClickListener(mDeviceClickListener);
	        
	        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
	        this.registerReceiver(ultimateReceiver, filter);
	        
	        doDiscovery();
	 }
	 
	 private void doDiscovery() {
		 	BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
	    	//cancela a busca atual e reinicia a busca
	        if (mBluetoothAdapter.isDiscovering()) {
	        	mBluetoothAdapter.cancelDiscovery();
	        }
	        
	        //inicia a busca por dispositivos
	        mBluetoothAdapter.startDiscovery();
	    }
	    
	    //Delegate do ACTION_FOUND
	    private final BroadcastReceiver ultimateReceiver = new BroadcastReceiver() {
	    	@Override
	    	public void onReceive(Context arg0, Intent arg1) {
	            String action = arg1.getAction();
	            
	            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
	            	TextView textView = (TextView) findViewById(R.id.no_devices);
	            	textView.setVisibility(1);
	            	
	                BluetoothDevice device = arg1.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
	                mArrayAdapter.add(device.getName() + "\n" + device.getAddress());
	                System.out.println("" + device.getName() + "\n" + device.getAddress());
	            }
	        }
	    };
	    
	    private OnItemClickListener mDeviceClickListener = new OnItemClickListener() {
	        public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {
	            System.out.println("clicado");
	        }
	    };

}
