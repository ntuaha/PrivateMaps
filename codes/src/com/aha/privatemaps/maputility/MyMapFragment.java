package com.aha.privatemaps.maputility;


import java.util.List;
import java.util.Observable;
import java.util.Observer;



import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.aha.privatemaps.R;
import com.aha.privatemaps.poi.POIData;
import com.aha.privatemaps.poi.POIManager;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class MyMapFragment extends MapFragment implements Observer
{
	//	private volatile static MyMapFragment uniqueInstance;
	//
	//	private MapManager mapManager;
	//	private GoogleMap mapView;
	//	private List<POIData> data;
	//	public static MyMapFragment getInstance()
	//	{
	//		if (uniqueInstance == null)
	//		{
	//			synchronized (POIManager.class)
	//			{
	//				if(uniqueInstance == null)
	//				{
	//					uniqueInstance = new MyMapFragment();
	//				}
	//			}
	//		}
	//		return uniqueInstance;
	//	}
	//	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	//	{  
	//
	//		View view = inflater.inflate(R.layout.fragment_map, container, false);  
	//		mapView = getMap();
	//		return initial(view,inflater.getContext());  
	//	}  
	//	public View initial(View view,final Context context){
	//		POIManager poiManager = POIManager.getInstance(context.getResources().getXml(R.xml.genxml),context);
	//		data = poiManager.getPOIs();
	//		
	//		//mapView.setBuiltInZoomControls(false);
	//		//mapManager = new MapManager(mapView,25037798,121565170,15,context.getResources().getDrawable(R.drawable.user),context.getResources().getDrawable(R.drawable.target));
	//		for(POIData d : data)
	//		{
	//			mapManager.setMapBoundsToPois(d.overlay.getCenter());
	//			mapManager.putPOI(d.overlay);
	//			
	//		}
	//		mapManager.moveGoodScale();
	//
	//		return view;
	//	}
	//	
	//	@Override
	//	public void update(Observable observable, Object obj) {
	//		if(observable instanceof POIManager)
	//		{
	//			Bundle bundle = (Bundle)obj;			
	//			mapManager.setCurrentPoistion(bundle.getDouble("lat"), bundle.getDouble("lng"));
	//		}
	//	}
	//	public void moveToLocation(ItemizedOverlay overlay) {
	//		mapManager.alertPOI(overlay);
	//		
	//	}
	//	@Override
	//	public void onSaveInstanceState(Bundle outState) {
	//	    //No call for super(). Bug on API Level > 11.
	//	}
	//	public void moveToMe() {
	//		mapManager.moveToMe();
	//		
	//	}

	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub

	}
	public static final String ARG_PLANET_NUMBER = "planet_number";
	static final LatLng NKUT = new LatLng(23.979548, 120.696745);
	static final LatLng NKUT2 = new LatLng(23.979548, 122.696745);
	private GoogleMap map;
	private Context context;

		public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
				context = inflater.getContext();
			return  super.onCreateView(inflater,container,savedInstanceState);
		}
	private List<POIData> data;
	public void initial(){
		map = getMap();
		if(context!=null){
			Toast.makeText(context, "test",Toast.LENGTH_SHORT).show();
		}
		
		POIManager poiManager = POIManager.getInstance(getResources().getXml(R.xml.genxml));
		data = poiManager.getPOIs();
		int i =0;
		double centerLat =0.0;
		double centerLng =0.0;
		for(POIData d : data)
		{
			i++;
			LatLng p = new LatLng(d.lat,d.lng);
			centerLat += d.lat;
			centerLng += d.lng;
			map.addMarker(new MarkerOptions().position(p).title(i+"").snippet("Lat:"+d.lat+",Lng:"+d.lng));
			//mapManager.setMapBoundsToPois(d.overlay.getCenter());
			//mapManager.putPOI(d.overlay);
		}
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(centerLat/i,centerLng/i),12));
		
//		//if(map!=null){
//			//Marker nkut = map.addMarker(new MarkerOptions().position(NKUT).title("南開科技大學").snippet("數位生活創意系"));
//			map.addMarker(new MarkerOptions().position(NKUT).title("南開科技大學").snippet("數位生活創意系"));
//			map.moveCamera(CameraUpdateFactory.newLatLngZoom(NKUT, 16));
//		}else{
//
//		}
	}
	public void initial2(){
		map = getMap();
		if(map!=null){
			//Marker nkut = map.addMarker(new MarkerOptions().position(NKUT).title("南開科技大學").snippet("數位生活創意系"));
			map.addMarker(new MarkerOptions().position(NKUT2).title("南開科技大學").snippet("數位生活創意系"));
			map.moveCamera(CameraUpdateFactory.newLatLngZoom(NKUT2, 14));
		}else{

		}
	}
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initial();
	}

}
