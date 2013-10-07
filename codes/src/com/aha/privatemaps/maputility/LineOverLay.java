package com.aha.privatemaps.maputility;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;



import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;

public class LineOverLay extends Overlay{
	private GeoPoint p1;
	private GeoPoint p2;
//	private int mode=1;

	LineOverLay(GeoPoint gp1,GeoPoint gp2,int mode){
		p1 = gp1;
		p2 = gp2;
//		this.mode=mode;
	}
	public boolean draw(Canvas canvas, MapView mapView, boolean shadow, long when)
	{
		Projection projection = mapView.getProjection();
		Paint paint = new Paint();
		Point point2 = new Point();
		paint.setAntiAlias(true);
		paint.setColor(Color.BLACK);
		Point point = new Point();
		projection.toPixels(p1,point);
		projection.toPixels(p2,point2);	
		paint.setColor(Color.BLACK);
		paint.setStrokeWidth(5);
		paint.setAlpha(150);
		canvas.drawLine(point.x, point.y, point2.x, point2.y, paint);
		return super.draw(canvas,mapView,shadow,when);
	}


}
