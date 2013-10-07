package com.aha.privatemaps;




import com.aha.privatemaps.maputility.MyMapFragment;
import com.aha.privatemaps.poi.POIManager;
import com.aha.privatemaps.services.MessageHandler;
import com.aha.privatemaps.services.ServiceManager;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.res.Configuration;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;

public class MainActivity extends Activity {
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	private ServiceManager serviceManager;
	private CharSequence mDrawerTitle;
	private CharSequence mTitle;
	private String[] mPlanetTitles;
	FrameLayout inforfragment;

	private MyMapFragment mapFragment;
	private POIManager poiManager;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mTitle = mDrawerTitle = getTitle();
		mPlanetTitles = getResources().getStringArray(R.array.Drawer_List);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);

		// set a custom shadow that overlays the main content when the drawer opens
		//mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
		// set up the drawer's list view with items and click listener
		mDrawerList.setAdapter(new ArrayAdapter<String>(this,R.layout.drawer_list_item, mPlanetTitles));
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

		// enable ActionBar app icon to behave as action to toggle nav drawer
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		// ActionBarDrawerToggle ties together the the proper interactions
		// between the sliding drawer and the action bar app icon
		mDrawerToggle = new ActionBarDrawerToggle(
				this,                  /* host Activity */
				mDrawerLayout,         /* DrawerLayout object */
				R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
				R.string.drawer_open,  /* "open drawer" description for accessibility */
				R.string.drawer_close  /* "close drawer" description for accessibility */
				) {
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(mTitle);
				invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
			}

			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(mDrawerTitle);
				invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);
		
		setFragment();
		if (savedInstanceState == null) {
			selectItem(1);
		}




	}
	private void setFragment(){
		//mapFragment = new MyMapFragment.getInstance();
		mapFragment = new MyMapFragment();
		//poiManager = POIManager.getInstance(getResources().getXml(R.xml.genxml),MainActivity.this);
		//poiManager.addObserver(mapFragment);

		serviceManager = new ServiceManager(mMessenger);
		//serviceManager.doBindService(MainActivity.this);


	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	/* The click listner for ListView in the navigation drawer */
	private class DrawerItemClickListener implements ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			if(position==1){
				FragmentManager fragmentManager = getFragmentManager();
				fragmentManager.beginTransaction().replace(R.id.map,mapFragment ).commit();

				mDrawerLayout.closeDrawer(mDrawerList);
				
			}else{
				selectItem(position);
			}
		}

	}
	/*
	 *  做選擇DrawList要做的事情，傳入點擊的項目位置
	 */
	private void selectItem(int position) {
		// update the main content by replacing fragments
		Fragment fragment = new MapFragment_Test();
		Bundle args = new Bundle();
		args.putInt(MapFragment_Test.ARG_PLANET_NUMBER, position);
		fragment.setArguments(args);

		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction().replace(R.id.map, fragment).commit();

		// update selected item and title, then close the drawer
		mDrawerList.setItemChecked(position, true);
		setTitle(mPlanetTitles[position]);
		mDrawerLayout.closeDrawer(mDrawerList);
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}
	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 * 必須使用的函數  onPostCreate  onConfigurationChanged
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	/**
	 * 
	 */
	 private MessageHandler messageHandler = new MessageHandler() {


		 //Receive message and display information 
		 public void display(Message msg){
			 Bundle bundle = msg.getData();
			 double lat = bundle.getDouble("lat");
			 double lng = bundle.getDouble("lng");
			 long time = bundle.getLong("time");
			 //Log.d("Main",""+lat+","+lng);
			 //poiManager.updateCurrentPosition(MainActivity.this,lat, lng,time);		
		 }




	 };

	 final Messenger mMessenger = new Messenger(messageHandler);


	 protected void onPostResume() 
	 {
		 super.onPostResume();
		 Handler handle = new Handler();
		 handle.post(new Runnable(){
			 public void run(){
				 Message msg = Message.obtain(null, MessageHandler.MSG_RESUME);
				 msg.replyTo = mMessenger;
				 Messenger messager = serviceManager.getMessager();

				 try {
					 if(messager !=null)
						 messager.send(msg);
					 else
						 Log.d("Main","Service error, it should be solved. in onPostResume");
				 } catch (RemoteException e) {
					 Log.d("Main","Service error, it should be solved. in onPostResume");
					 e.printStackTrace();
				 }
			 }
		 });
	 }


	 @Override
	 protected void onPause() 
	 {
		 Message msg = Message.obtain(null, MessageHandler.MSG_PAUSE);
		 msg.replyTo = mMessenger;
		 Messenger messager = serviceManager.getMessager();
		 if(messager!=null){
			 try {
				 messager.send(msg);
			 } catch (RemoteException e) {
				 Log.d("Main","Service error, it should be solved. in onPostResume");
				 e.printStackTrace();
			 }
		 }
		 super.onPause();
	 }

	 protected void onDestroy() 
	 {

		 serviceManager.doBindService(MainActivity.this);
		 super.onDestroy();
	 }


}
