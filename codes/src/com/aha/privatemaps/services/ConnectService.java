package com.aha.privatemaps.services;

import java.util.List;


import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;


public class ConnectService {
	private static Context context;
	private static final String SERVICE = "UBikeService";
	private static ActivityManager mAM;
	public ConnectService(Context c)
	{
		
	}
	static void check(Context c)
	{
		context =c;
		mAM = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
		boolean isRunService=false;
		List<ActivityManager.RunningServiceInfo> mRunningTasks = mAM.getRunningServices(999);
		for(ActivityManager.RunningServiceInfo info: mRunningTasks)
		{
			isRunService|=info.process.equals(SERVICE);
		}
		
		if(isRunService)
		{
			Intent i = new Intent(context,MainService.class);
			context.stopService(i);
		}
	}
	static void closeService(Context c)
	{
		context =c;
		mAM = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
		boolean isRunService=false;
		List<ActivityManager.RunningServiceInfo> mRunningTasks = mAM.getRunningServices(999);
		for(ActivityManager.RunningServiceInfo info: mRunningTasks)
		{
			isRunService|=info.process.equals(SERVICE);
		}
		if(isRunService)
		{
			Intent i = new Intent(context,MainService.class);
			context.stopService(i);
		}	
	}
}
