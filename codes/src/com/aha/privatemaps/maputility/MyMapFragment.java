package com.aha.privatemaps.maputility;


import java.util.Observable;
import java.util.Observer;



import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aha.privatemaps.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class MyMapFragment extends SupportMapFragment implements Observer
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

	public MyMapFragment() {
		// Empty constructor required for fragment subclasses
		// 留空給繼承用的class
	}
	static final LatLng NKUT = new LatLng(23.979548, 120.696745);
	private GoogleMap map;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_map, container, false);

		map = ((SupportMapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
		if(map!=null){
			//Marker nkut = map.addMarker(new MarkerOptions().position(NKUT).title("南開科技大學").snippet("數位生活創意系"));
			//map.moveCamera(CameraUpdateFactory.newLatLngZoom(NKUT, 16));
		}else{
			
		}
		return rootView;
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
}
