package com.aha.privatemaps.maputility;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import org.apache.http.conn.ConnectTimeoutException;





import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.aha.privatemaps.R;
import com.aha.privatemaps.poi.POIData;
import com.aha.privatemaps.poi.POIManager;
import com.aha.privatemaps.utility.UbikeHttpConnection;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class MyMapFragment extends MapFragment implements Observer
{
	private Marker personalMarker = null;

	@Override
	public void update(Observable observable, Object obj) {
		if(map!=null)
		{
			if(observable instanceof POIManager)
			{
				Bundle bundle = (Bundle)obj;			
				LatLng myLatLng = new LatLng(bundle.getDouble("lat"),bundle.getDouble("lng"));
				StringBuilder sb = new StringBuilder();
				sb.append("Lat:").append(bundle.getDouble("lat"))
				.append(" Lng:").append(bundle.getDouble("lng"));
				//.append(" Time:").append(Utility.convertUNIXtoDate());
				CameraPosition myPosition = new CameraPosition.Builder().target(myLatLng).zoom(15).bearing(0).tilt(30).build();
				//Refresh map 
				if(personalMarker == null)
				{
					personalMarker = map.addMarker(new MarkerOptions().position(myLatLng).title("Me").snippet(sb.toString())
							.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
					map.animateCamera(CameraUpdateFactory.newCameraPosition(myPosition));
				}else{
					personalMarker.setPosition(myPosition.target);
				}
				personalMarker.showInfoWindow();						
			}
		}
	}
	public static final String ARG_PLANET_NUMBER = "planet_number";
	static final LatLng NKUT = new LatLng(23.979548, 120.696745);
	static final LatLng NKUT2 = new LatLng(23.979548, 122.696745);
	private GoogleMap map;
	private List<POIData> data;
	public void initial(){
		map = getMap();
		map.setInfoWindowAdapter(new MyInfoWindowAdapter());
		new InitialTask().execute(null,null,null);
		//		int i =0;
		//		double centerLat =0.0;
		//		double centerLng =0.0;
		//		try {
		//			data = UbikeHttpConnection.connectServer(null);		
		//			map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(centerLat/i,centerLng/i),12));
		//		} catch (ConnectTimeoutException e) {
		//			POIManager poiManager = POIManager.getInstance(getResources().getXml(R.xml.genxml));
		//			data = poiManager.getPOIs();
		//			e.printStackTrace();
		//			Toast.makeText(getActivity(), "網路連線有問題", Toast.LENGTH_SHORT).show();
		//		}
		//		if(data!=null)
		//		{
		//			for(POIData d : data)
		//			{
		//				i++;
		//				LatLng p = new LatLng(d.lat,d.lng);
		//				centerLat += d.lat;
		//				centerLng += d.lng;
		//				map.addMarker(new MarkerOptions().position(p).title(i+":"+d.addressZh).snippet("Lat:"+d.lat+",Lng:"+d.lng));
		//			}
		//		}else{
		//			Toast.makeText(getActivity(), "沒有資料", Toast.LENGTH_SHORT).show();
		//		}



	}


	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initial();
	}

	//	@Override
	//	public void onDestroyView() {
	//		super.onDestroyView();
	//		MapFragment f = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
	//		if (f != null) 
	//			getFragmentManager().beginTransaction().remove(f).commit();
	//	}
	class MyInfoWindowAdapter implements InfoWindowAdapter{

		private final View myContentsView;

		MyInfoWindowAdapter(){
			myContentsView = getActivity().getLayoutInflater().inflate(R.layout.custom_window_info, null);
		}

		@Override
		public View getInfoContents(Marker marker) {

			TextView tvTitle = ((TextView)myContentsView.findViewById(R.id.title));
			tvTitle.setText(marker.getTitle());
			TextView tvSnippet = ((TextView)myContentsView.findViewById(R.id.snippet));
			tvSnippet.setText(marker.getSnippet());

			return myContentsView;
		}

		@Override
		public View getInfoWindow(Marker marker) {
			// TODO Auto-generated method stub
			return null;
		}

	}
	class InitialTask extends AsyncTask<Void, Void, Void> {
		int i =0;
		double centerLat =0.0;
		double centerLng =0.0;
		@Override
		protected Void doInBackground(Void... countTo) {
			try {
				data = UbikeHttpConnection.connectServer();		
			} catch (ConnectTimeoutException e) {
				//POIManager poiManager = POIManager.getInstance(getResources().getXml(R.xml.genxml));
				//data = poiManager.getPOIs();
				e.printStackTrace();
				//Toast.makeText(getActivity(), "網路連線有問題", Toast.LENGTH_SHORT).show();
			}
			return null;
		}


		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			if(data!=null)
			{
				for(POIData d : data)
				{
					i++;
					LatLng p = new LatLng(d.lat,d.lng);
					centerLat += d.lat;
					centerLng += d.lng;

					StringBuilder sb = new StringBuilder();
					sb.append("Address:").append(d.addressZh).append("\n")
					.append("Lat:").append(d.lat).append("\n")
					.append("Lng:").append(d.lng).append("\n")
					.append("Avaliable:").append(d.tot).append("\n")
					.append("Total:").append(d.sus).append("\n")
					.append("Type:").append(d.icon_type).append("\n");
					map.addMarker(new MarkerOptions().position(p).title(i+":"+d.nameZh).snippet(sb.toString()));


				}
				map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(centerLat/i,centerLng/i),12));
			}else{
				Toast.makeText(getActivity(), "沒有資料", Toast.LENGTH_SHORT).show();
			}


		}

	}



}
