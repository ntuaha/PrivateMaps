package com.aha.privatemaps.maputility;

import java.util.List;

import android.graphics.drawable.Drawable;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class MapManager {
	public GoogleMap instance;
	private MapController mapController;
	private List<Overlay> overlays;
    private int minLatitude = Integer.MAX_VALUE;
    private int maxLatitude = Integer.MIN_VALUE;
    private int minLongitude = Integer.MAX_VALUE;
    private int maxLongitude = Integer.MIN_VALUE;
    private double hpadding =0.05;
    private double vpadding =0.1;
    private int counter=0;
    private Drawable userIcon;
    private Drawable targetIcon;
	private  MyItemizedOverlay previousObj;
	@SuppressWarnings("rawtypes")
	private  ItemizedOverlay previousOriObj;
	@SuppressWarnings("rawtypes")
	private  ItemizedOverlay focusOriObj;
    
	public MapManager(GoogleMap mapView,int lat,int lng,int size,Drawable ui,Drawable targetIcon){
		instance = mapView;
		//mapController =mapController mapView.getController();
		mapController.setCenter(new GeoPoint(lat,lng)); 
		mapController.setZoom(size);
		//overlays = mapView.getOverlays();
		userIcon = ui;
		previousOriObj = null;
		focusOriObj = null;
		this.targetIcon = targetIcon;
	}

	public void setCurrentPoistion(double lat,double lng)
	{
		overlays.remove(previousObj);
		GeoPoint point = new GeoPoint((int)(lat*1e6),(int)(lng*1e6));
		//mapController.animateTo(point);		
		previousObj=new MyItemizedOverlay(userIcon,point,"","");
		overlays.add(previousObj);
		
	}
	@SuppressWarnings("rawtypes")
	public void putPOI(ItemizedOverlay overlay)
	{		
		overlays.add(overlay);
	}
	public void clearOverlays()
	{
		overlays.clear();
	}
	
	
	public void resetOverlay()
	{
		previousObj = null;
		counter = 0;
		overlays.clear();
	    minLatitude = Integer.MAX_VALUE;
	    maxLatitude = Integer.MIN_VALUE;
	    minLongitude = Integer.MAX_VALUE;
	    maxLongitude = Integer.MIN_VALUE;
	}
	public void setMapBoundsToPois(GeoPoint point) 
	{
	    // If there is only on one result
	    // directly animate to that location
	    
	    if (counter== 1) { // animate to the location
	        mapController.animateTo(point);
	    } else {
	        // find the lat, lon span


	        // Find the boundaries of the item set
            int lat = point.getLatitudeE6(); 
            int lon = point.getLongitudeE6();

            maxLatitude = Math.max(lat, maxLatitude);
            minLatitude = Math.min(lat, minLatitude);
            maxLongitude = Math.max(lon, maxLongitude);
            minLongitude = Math.min(lon, minLongitude);
	    }
	} // end of the method
	public void moveGoodScale(){
        // leave some padding from corners
        // such as 0.1 for hpadding and 0.2 for vpadding
        maxLatitude = maxLatitude + (int)((maxLatitude-minLatitude)*hpadding);
        minLatitude = minLatitude - (int)((maxLatitude-minLatitude));

        maxLongitude = maxLongitude + (int)((maxLongitude-minLongitude)*vpadding);
        minLongitude = minLongitude - (int)((maxLongitude-minLongitude)*vpadding);

        // Calculate the lat, lon spans from the given pois and zoom
        mapController.zoomToSpan(Math.abs(maxLatitude - minLatitude), Math.abs(maxLongitude - minLongitude));

        // Animate to the center of the cluster of points
        mapController.animateTo(new GeoPoint(
              (maxLatitude + minLatitude) / 2, (maxLongitude + minLongitude) / 2));
        Log.i("lat","Lat:"+(maxLatitude + minLatitude) / 2/1e6+",lng:"+(maxLongitude + minLongitude) / 2/1e6);
	}

	public void alertPOI(ItemizedOverlay overlay) 
	{
		// TODO Auto-generated method stub
		if(previousOriObj!=null)
		{
			overlays.add(previousOriObj);
		}
		if(focusOriObj!=null)
		{
			overlays.remove(focusOriObj);
		}
		previousOriObj = overlay;
		focusOriObj = new MyItemizedOverlay(targetIcon,overlay.getCenter(),"","");
		overlays.remove(overlay);
		overlays.add(focusOriObj);
		mapController.animateTo(focusOriObj.getCenter());		
	}

	public void moveToMe() 
	{
		if(previousObj!=null)
		{
			mapController.animateTo(previousObj.getCenter());
		}
		
	}


	
	
}
