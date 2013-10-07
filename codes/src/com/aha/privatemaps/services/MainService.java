package com.aha.privatemaps.services;


import java.util.ArrayList;

import com.aha.privatemaps.MainActivity;
import com.aha.privatemaps.sensors.GPS;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;




public class MainService extends Service implements GPS.GPS_Notification
{
	private GPS gps;
	private static int ONGOING_NOTIFICATION = 1;
	private static int UPDATE_LOCATION_PERIODIC = 10;
	private static int UPDATE_LOCATION_DISTANCE = 0;
	private LocationManager locationManager;	

	private void sendPoistionToUIThread(int message_type)
	{
		Bundle data = new Bundle();
		data.putDouble("lat", gps.location.getLatitude());
		data.putDouble("lng", gps.location.getLongitude());
		data.putLong("time", gps.location.getTime());
		Message message = Message.obtain(null,message_type);
		message.setData(data);	
		boradcastMessage(message);
	}

	//Message
	ArrayList<Messenger> mClients = new ArrayList<Messenger>();

	private MessageHandler messageHandler = new MessageHandler() {

		public void display(Message msg)
		{
			if(UI_state==MessageHandler.MSG_RESUME)
			{
				sendPoistionToUIThread(MSG_DISPLAY);
			}
		}
		public void register(Message msg)
		{
			mClients.add(msg.replyTo);
		}
		public void unregister(Message msg)
		{
			mClients.remove(msg.replyTo);
		}
	};
	final Messenger mMessenger = new Messenger(messageHandler);


	@Override
	public void onCreate()
	{
		super.onCreate();
		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		Log.i("SensorService", "Start");
		initialLocationService();
		setForeground();
	}
	@Override
	public void onDestroy() 
	{
		locationManager.removeUpdates(gps);
		//unregisterReceiver(serviceReceiver);
		super.onDestroy();
	}

	@Override
	public IBinder onBind(Intent intent) {
		return mMessenger.getBinder();
	}

	@SuppressWarnings("deprecation")
	private void setForeground()
	{
		Notification notification = new Notification(android.R.drawable.ic_dialog_map, null,System.currentTimeMillis());
		PendingIntent contentIntent = PendingIntent.getActivity(MainService.this, 0,new Intent(MainService.this, MainActivity.class), 0);
		notification.setLatestEventInfo(MainService.this, "MyUBike","Working", contentIntent);
		startForeground(ONGOING_NOTIFICATION,notification);
	}
	private void initialLocationService()
	{
		Log.i("SensorService", "GPS start before");
		gps = new GPS(MainService.this);
		
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, UPDATE_LOCATION_PERIODIC, UPDATE_LOCATION_DISTANCE, gps);
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, UPDATE_LOCATION_PERIODIC, UPDATE_LOCATION_DISTANCE, gps);
		Log.i("SensorService", "GPS start");
	}

	@Override
	public boolean onUnbind(Intent intent) 
	{
		return true;
	}
	/*
	private void checkFolder(){
		File folder = new File(Environment.getExternalStorageDirectory() + "/MyUBike/");
		Log.d("folder_MyService",folder.getAbsolutePath());
		boolean success = false;
		if(!folder.exists())
		{      
			success = folder.mkdirs();
			Log.d("folder_test_MyService",folder.exists()+"|"+success );
		}           
		if (!success)   
		{       
			// Do something on success       			
		}else{      

			// Do something else on failure   
		}  
	}
*/
	private void boradcastMessage(Message msg){
		for (int i=mClients.size()-1; i>=0; i--) {
			try {
				mClients.get(i).send(msg);
			} catch (RemoteException e) {
				Log.d("UBikeService","mClient is null!");
				mClients.remove(i);
			}
		}
	}
	@Override
	public void update(Location location) 
	{
		sendPoistionToUIThread(MessageHandler.MSG_DISPLAY);	
	}



}
