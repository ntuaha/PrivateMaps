package com.aha.privatemaps.poi;


public class POIData {
	public String addressZh = null;
	public String addressEn = null;
	public String nameEn = null;
	public String nameZh = null;
	public double lat= 0.0;
	public double lng= 0.0;
	public int tot = 0; // unknown variable  可租借的車
	public int sus = 0; // unknown variable  最大可停放車數
	public double distance=0.0;
	public String mday="";
	public int icon_type= -1; // unknown variable  1:有車 2:無車
	public float distanceToMe = Float.MAX_VALUE;
}
