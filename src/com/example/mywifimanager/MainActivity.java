package com.example.mywifimanager;

import java.util.List;

import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity {
	public static final String TAG = "MainActivity";  
    
    private Button mBtn1, mBtn2;  
    ListView listView;
    private WifiAdmin mWifiAdmin;  
    private Context mContext = null; 
    //扫描wifi使用
    TextView mainText; 
    WifiManager mainWifi; 
    WifiReceiver receiverWifi; 
    List<ScanResult> wifiList; 
    StringBuilder sb = new StringBuilder(); 
  //  mySimpleAdapter simpleAdapter=new mySimpleAdapter();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 mContext = this; 
		setContentView(R.layout.activity_main);
		//找现对应的组件id
		mainText = (TextView) findViewById(R.id.textView1); 
		listView=(ListView)findViewById(R.id.listView);
		mBtn1 = (Button)findViewById(R.id.button1);  
        mBtn2 = (Button)findViewById(R.id.button2); 
        mainWifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);//启动WifiManager
        receiverWifi = new WifiReceiver(); 
        registerReceiver(receiverWifi, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)); 
        mainWifi.startScan(); 
        mainText.setText("\n正在扫描可用的Wifi...\n"); 
        mBtn1.setText("点击连接Wifi");  
        mBtn2.setText("点击创建Wifi热点"); 
        //button1的事件
        mBtn1.setOnClickListener(new Button.OnClickListener() {  
            
            @Override  
            public void onClick(View v) {  
                
                mWifiAdmin = new WifiAdmin(mContext) {  
                      
                    @Override  
                    public void myUnregisterReceiver(BroadcastReceiver receiver) {  
                        // TODO Auto-generated method stub  
                        MainActivity.this.unregisterReceiver(receiver);  
                    }  
                      
                    @Override  
                    public Intent myRegisterReceiver(BroadcastReceiver receiver,  
                            IntentFilter filter) {  
                        // TODO Auto-generated method stub  
                        MainActivity.this.registerReceiver(receiver, filter);  
                        return null;  
                    }  
                      
                    @Override  
                    public void onNotifyWifiConnected() {  
                        // TODO Auto-generated method stub  
                        Log.v(TAG, "have connected success!");  
                        Log.v(TAG, "###############################");  
                    }  
                      
                    @Override  
                    public void onNotifyWifiConnectFailed() {  
                        // TODO Auto-generated method stub  
                        Log.v(TAG, "have connected failed!");  
                        Log.v(TAG, "###############################");  
                    }  
                };  
                mWifiAdmin.openWifi();  
                mWifiAdmin.addNetwork(mWifiAdmin.createWifiInfo("witmob-adsl", "witmob1001", WifiAdmin.TYPE_WEP));  
                  
            }  
        });  
          
        mBtn2.setOnClickListener(new Button.OnClickListener() {  
              
            @Override  
            public void onClick(View v) {  
                // TODO Auto-generated method stub  
                  
                WifiApAdmin wifiAp = new WifiApAdmin(mContext);  
                wifiAp.startWifiAp("\"witmob-adsl\"", "witmob1001");  
            }  
        });  
          
    } 
//	@Override  
//    public boolean onCreateOptionsMenu(Menu menu) {  
//        // Inflate the menu; this adds items to the action bar if it is present.  
//        getMenuInflater().inflate(R.menu.activity_main, menu);  
//        return true;  
//    }  
	//扫描菜单
	public boolean onCreateOptionsMenu(Menu menu) { 
        menu.add(0, 0, 0, "刷新扫描"); 
        return super.onCreateOptionsMenu(menu); 
   } 

   public boolean onMenuItemSelected(int featureId, MenuItem item) { 
        mainWifi.startScan(); 
        mainText.setText("正在扫描可用的WiFi..."); 
        return super.onMenuItemSelected(featureId, item); 
   } 

//   protected void onPause1() { 
//        unregisterReceiver(receiverWifi); 
//        super.onPause(); 
//   } 

//   protected void onResume1() { 
//        registerReceiver(receiverWifi, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)); 
//        super.onResume(); 
//   } 
    
   class WifiReceiver extends BroadcastReceiver { 
        public void onReceive(Context c, Intent intent) { 
             sb = new StringBuilder(); 
             wifiList = mainWifi.getScanResults(); 
           for(int i = 0; i < wifiList.size(); i++){ 
             sb.append(new Integer(i+1).toString() + "."); 
             sb.append((wifiList.get(i)).toString()); 
             sb.append("\n"); 
           } 
           mainText.setText(sb); 
          // listView.setAdapter(simpleAdapter);
           
        } 
   } 
	
	//扫描结束
	@Override  
    public void onResume() {  
        super.onResume();  
          
        Log.d("Rssi", "Registered");  
    }  

    @Override  
    public void onPause() {  
        super.onPause();  
          
        Log.d("Rssi", "Unregistered");  
    }  
}

