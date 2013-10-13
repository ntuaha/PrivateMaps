package com.aha.privatemaps.services;

import java.util.List;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;


public class ServiceManager {
	public ServiceManager(Messenger m){
		mMessenger = m;
	}
	Messenger mMessenger;
	private final static String WATCH_TAG = "com.aha.service";
	private final String SERIVCEBASENAME="com.aha.service";
	public Messenger mService = null;
	/** Flag indicating whether we have called bind on the service. */
	boolean mIsBound;

	/**
	 * Class for interacting with the main interface of the service.
	 */
	private ServiceConnection mConnection = new ServiceConnection() {
	    public void onServiceConnected(ComponentName className, IBinder service) {
	        mService = new Messenger(service);

	        // We want to monitor the service for as long as we are
	        // connected to it.
	        try {
	            Message msg = Message.obtain(null,MessageHandler.MSG_REGISTER_CLIENT);
	            msg.replyTo = mMessenger;
	            mService.send(msg);
	            Log.d("ServiceManager","Service send");
	        } catch (RemoteException e) {
	        	Log.d("ServiceManager","Error Error");
	        }
	    }

	    public void onServiceDisconnected(ComponentName className) {
	        mService = null;

	    }
	};

	public void doBindService(Context context) {
	    // Establish a connection with the service.  We use an explicit
	    // class name because there is no reason to be able to let other
	    // applications replace our component.
	    context.bindService(new Intent(context, MainService.class), mConnection, Context.BIND_AUTO_CREATE);
	    mIsBound = true;
	    Log.d("ServiceManager","Bind Service strat");
	}

	void doUnbindService(Context context) {
	    if (mIsBound) {
	        // If we have received the service, and hence registered with
	        // it, then now is the time to unregister.
	        if (mService != null) {
	            try {
	                Message msg = Message.obtain(null,MessageHandler.MSG_UNREGISTER_CLIENT);
	                msg.replyTo = mMessenger;
	                mService.send(msg);
	            } catch (RemoteException e) {
	                // There is nothing special we need to do if the service
	                // has crashed.
	            }
	        }

	        // Detach our existing connection.
	        context.unbindService(mConnection);
	        mIsBound = false;
	    }
	}
	

	public void initService(Context context)
	{	    
		Intent intent = new Intent();
		intent.setClassName(SERIVCEBASENAME, MainService.class.getName());
		context.getApplicationContext().bindService(intent, mConnection,Context.BIND_AUTO_CREATE);
		Log.d("ServiceManager","Service strat");
	}
		
	public Messenger getMessager(){
		return mService;
	}
	public void releaseService(Context context) 
	{
		context.getApplicationContext().unbindService(mConnection);
		Intent intent = new Intent(context,MainService.class);
		context.stopService(intent);
		mConnection = null;
	}
	public void unbindService(Context context){
		context.getApplicationContext().unbindService(mConnection);
		mConnection = null;
	}
	static public void killService(Context context,String name){
		ActivityManager mAM = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningServiceInfo> mRunningTasks=mAM.getRunningServices(999);
		for(ActivityManager.RunningServiceInfo info: mRunningTasks)
		{
			Log.d("service ",info.process);
			Log.d("service ",info.service.getPackageName());
			Log.d("service ",info.service.getClassName());
			Log.d("service ",info.service.toString());
			

			if(info.service.getClassName().equals(name)){
				Log.d("service_kill",info.process);
				Log.d("service_kill",info.service.toString());
				android.os.Process.killProcess(info.pid);
				android.os.Process.sendSignal(info.pid, 9);
				
			}
		}
		for(ActivityManager.RunningServiceInfo info: mRunningTasks)
		{

			if(info.service.getClassName().equals(name)){
				Log.d("service_killed",info.process);
				Log.d("service_killed",info.service.toString());			
			}
		}

	}
	static public boolean isOtherAppOnForeground(Context context){
		ActivityManager mAM = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningAppProcessInfo> mRunningTasks=mAM.getRunningAppProcesses();
		boolean ans = false;
		for(ActivityManager.RunningAppProcessInfo info: mRunningTasks)
		{
			if(info.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND && info.processName.equals(WATCH_TAG)){
				ans = true;
				break;
			}

		}
		return ans;

	}

}
