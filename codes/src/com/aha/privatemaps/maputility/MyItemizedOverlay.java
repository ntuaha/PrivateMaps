package com.aha.privatemaps.maputility;

import java.util.ArrayList;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

@SuppressWarnings("rawtypes")
public class MyItemizedOverlay extends ItemizedOverlay 
{
	
    private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
    private Drawable marker;

   
    public MyItemizedOverlay(Drawable defaultMarker,GeoPoint p,String title,String words)
	{
        super(defaultMarker);
    	marker = defaultMarker;
    	mOverlays.add(new OverlayItem(p,title,words));
    	populate();
	}

    @Override
    protected OverlayItem createItem(int i) 
    {
    	return mOverlays.get(i);
    }

    @Override
    public int size() 
    {
    	return mOverlays.size();
    }

    public void addOverlay(OverlayItem overlay) 
    {
    	mOverlays.add(overlay);
    	populate();
    }       

	@Override
	public void draw(Canvas canvas, MapView mapView, boolean shadow)
	{
	        super.draw(canvas, mapView, shadow);
	        boundCenterBottom(marker); 
	}
	
}
