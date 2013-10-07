package com.aha.privatemaps.maputility;

import java.util.List;

import android.graphics.drawable.Drawable;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class MapManager {
	public GoogleMap instance;
	private MapController mapController;
	//private List<Overlay> overlays;
	private List<Marker> markers;
    private double minLatitude = Double.MAX_VALUE;
    private double maxLatitude = Double.MIN_VALUE;
    private double minLongitude = Double.MAX_VALUE;
    private double maxLongitude = Double.MIN_VALUE;
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
    
	public MapManager(GoogleMap map,double lat,double lng,int size){
		instance = map;
		//mapController =mapController mapView.getController();
		//mapController.setCenter(new GeoPoint(lat,lng));
		//overlays = mapView.getOverlays();
		//userIcon = ui;
//		mapController.setZoom(size);
		//地圖位置初始化
		map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(25.037798, 121.565170), 14));
		previousOriObj = null;
		focusOriObj = null;
//		this.targetIcon = targetIcon;
	}

	public void setCurrentPoistion(double lat,double lng)
	{
		//overlays.remove(previousObj);
		//GeoPoint point = new GeoPoint((int)(lat*1e6),(int)(lng*1e6));
		//mapController.animateTo(point);		
		//previousObj=new MyItemizedOverlay(userIcon,point,"","");
		//overlays.add(previousObj);
		LatLng p = new LatLng(lat,lng);	
		Marker nkut = instance.addMarker(new MarkerOptions().position(p));
		markers.add(nkut);
		instance.animateCamera(CameraUpdateFactory.newLatLngZoom(p, 16));
		
		
		
	}
	public void putPOI(Marker m)
	{		
		markers.add(m);
	}

	
	
	public void resetOverlay()
	{
		previousObj = null;
		counter = 0;
		instance.clear();
	    minLatitude = Integer.MAX_VALUE;
	    maxLatitude = Integer.MIN_VALUE;
	    minLongitude = Integer.MAX_VALUE;
	    maxLongitude = Integer.MIN_VALUE;
	}
	public void setMapBoundsToPois(LatLng point) 
	{
	    // If there is only on one result
	    // directly animate to that location
	    
	    if (counter== 1) { // animate to the location
	        //mapController.animateTo(point);
	    	instance.animateCamera(CameraUpdateFactory.newLatLngZoom(point, 16));
	    } else {
	        // find the lat, lon span


	        // Find the boundaries of the item set
            double lat = point.latitude; 
            double lon = point.longitude;

            maxLatitude = Math.max(lat, maxLatitude);
            minLatitude = Math.min(lat, minLatitude);
            maxLongitude = Math.max(lon, maxLongitude);
            minLongitude = Math.min(lon, minLongitude);
	    }
	} // end of the method
	public void moveGoodScale(){
        // leave some padding from corners
        // such as 0.1 for hpadding and 0.2 for vpadding
        maxLatitude = maxLatitude + (maxLatitude-minLatitude)*hpadding;
        minLatitude = minLatitude - (maxLatitude-minLatitude);

        maxLongitude = maxLongitude + (maxLongitude-minLongitude)*vpadding;
        minLongitude = minLongitude - (maxLongitude-minLongitude)*vpadding;

        // Calculate the lat, lon spans from the given pois and zoom
       // mapController.zoomToSpan(Math.abs(maxLatitude - minLatitude), Math.abs(maxLongitude - minLongitude));
        instance.setPadding(12, 15, 10, 15);
        // Animate to the center of the cluster of points
        
        instance.animateCamera(CameraUpdateFactory.newLatLng(new LatLng((maxLatitude + minLatitude) / 2, (maxLongitude + minLongitude) / 2)));
//        mapController.animateTo(new LatLng(
//              (maxLatitude + minLatitude) / 2, (maxLongitude + minLongitude) / 2));
        Log.i("lat","Lat:"+(maxLatitude + minLatitude) / 2/1e6+",lng:"+(maxLongitude + minLongitude) / 2/1e6);
	}
/*
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
	*/
/*
	public void moveToMe() 
	{
		if(previousObj!=null)
		{
			mapController.animateTo(previousObj.getCenter());
		}
		
	}
*/

	
	
}
