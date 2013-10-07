package com.aha.privatemaps.poi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Observable;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.aha.privatemaps.R;
import com.aha.privatemaps.maputility.MyItemizedOverlay;
import com.google.android.maps.GeoPoint;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

public class POIManager extends Observable
{
	private List<POIData> pois;
	private volatile static POIManager uniqueInstance;
	private static int FAR_BICYCLE = R.drawable.far_bicycle;
	private static int NEAR_BICYCLE = R.drawable.near_bicycle;
	public static float NEAR_AREA_RADIUS = 500.0f;
	public static POIManager getInstance(XmlPullParser xmlParser)
	{
		if (uniqueInstance == null)
		{
			synchronized (POIManager.class)
			{
				if(uniqueInstance == null)
				{
					uniqueInstance = new POIManager(xmlParser);
				}
			}
		}
		return uniqueInstance;
	}
	private POIManager(XmlPullParser xmlParser)
	{
		pois = new ArrayList<POIData>();
		try {
			while (xmlParser.next() != XmlPullParser.END_DOCUMENT)
			{
				String bustag = xmlParser.getName();
				if((bustag != null) && bustag.equals("marker")) 
				{
					POIData data = new POIData();
					int size = xmlParser.getAttributeCount();

					for(int i=0 ; i < size; i++) 
					{

						String attrName = xmlParser.getAttributeName(i);
						String attrValue = xmlParser.getAttributeValue(i);
						if((attrName != null) && attrName.equals("address")) {														
							data.addressZh = attrValue;										
						} else if((attrName != null) && attrName.equals("addressen")) {														
							data.addressEn = attrValue;										
						} else if((attrName != null) && attrName.equals("nameen")) {														
							data.nameEn = attrValue;										
						} else if((attrName != null) && attrName.equals("name")) {														
							data.nameZh = attrValue;										
						} else if((attrName != null) && attrName.equals("lat")) {														
							data.lat = Double.parseDouble(attrValue);										
						} else if((attrName != null) && attrName.equals("lng")) {														
							data.lng = Double.parseDouble(attrValue);										
						} else if((attrName != null) && attrName.equals("distance")) {														
							data.distance = Double.parseDouble(attrValue);										
						} else if((attrName != null) && attrName.equals("mday")) {														
							data.mday = attrValue;										
						} else if((attrName != null) && attrName.equals("tot")) {														
							data.tot = Integer.parseInt(attrValue);										
						} else if((attrName != null) && attrName.equals("sus")) {														
							data.sus = Integer.parseInt(attrValue);										
						} else if((attrName != null) && attrName.equals("icon_type")) {														
							data.icon_type = Integer.parseInt(attrValue);										
						} 
					}
					if(data.lat!=0.0)
					{
						//GeoPoint point = new GeoPoint((int)(data.lat*1e6),(int)(data.lng*1e6));
						//Marker marker = 
						//data.overlay=new MyItemizedOverlay(context.getResources().getDrawable(FAR_BICYCLE),point,data.nameZh,data.distanceToMe+"");
						pois.add(data);
					}
				}

			}

		} catch (XmlPullParserException e) {			
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void updateCurrentPosition(Context context,double endLatitude,double endLongitude,long time)
	{
		float[] results = new float[1];
		for (POIData poi:pois)
		{
			Location.distanceBetween(poi.lat, poi.lng, endLatitude, endLongitude, results);
			poi.distanceToMe = results[0];
			//GeoPoint point = new GeoPoint((int)(poi.lat*1e6),(int)(poi.lng*1e6));
			if(poi!=null)
				if(results[0] <= NEAR_AREA_RADIUS)
				{				
					//poi.overlay = new MyItemizedOverlay(context.getResources().getDrawable(NEAR_BICYCLE),point,poi.nameZh,poi.distanceToMe+""); 
				}else{
					//poi.overlay = new MyItemizedOverlay(context.getResources().getDrawable(FAR_BICYCLE),point,poi.nameZh,poi.distanceToMe+"");
				}				
		}	
		Collections.sort(pois,new POIComparator());
		this.setChanged();
		Bundle bundle = new Bundle();
		bundle.putDouble("lat", endLatitude);
		bundle.putDouble("lng", endLongitude);
		bundle.putLong("time", time);
		this.notifyObservers(bundle);

	}
	static public class POIComparator implements Comparator<POIData>
	{

		@Override
		public int compare(POIData arg0, POIData arg1) {
			if(arg0.distanceToMe>arg1.distanceToMe)
				return 1;
			else
				return -1;
		}

	}
	public List<POIData> getPOIs()
	{
		Log.d("size",this.pois.size()+"");
		return this.pois;
	}
}
