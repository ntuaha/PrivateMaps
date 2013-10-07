package com.aha.privatemaps.sensors;




import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;



public class GPS implements LocationListener{

	public Location location; 
	private GPS_Notification notification;
	private static float GPS_ACCURACY_LIMIT = 100.0f;
	public interface GPS_Notification
	{
		public void update(Location location);
	}
	
	public GPS(GPS_Notification notification)
	{
		this.notification = notification;
		location = null;
	}



	public void onLocationChanged(Location location) {
		if(location.hasAccuracy())
		{
			float accuracy = location.getAccuracy();
			Log.d("GPS",accuracy+"");
			if(accuracy< GPS_ACCURACY_LIMIT)
			{
				this.location = location;	
				this.notification.update(location);
			}
		}
	}

	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

}

