package com.aha.privatemaps.utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.mime.MultipartEntity;

import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import com.aha.privatemaps.poi.POIData;

import android.util.Log;

public class UbikeHttpConnection {
	private static final String CONNECT_PATH = "http://www.youbike.com.tw/genxml9.php?radius=10&mode=0";
		private static final int CONNECT_TIMEOUT = 10000;
		private static final int WAIT_DATA_TIMEOUT = 10000;
	static public List<POIData> connectServer() throws ConnectTimeoutException{
		List<POIData> feedback = null;
		HttpPost httpRequest = new HttpPost(CONNECT_PATH);
		MultipartEntity entity = new MultipartEntity();
		try {
			//entity.addPart("data", new StringBody(object,Charset.forName("UTF-8")));
			httpRequest.setEntity(entity);

			HttpParams httpParameters = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParameters, CONNECT_TIMEOUT);
			HttpConnectionParams.setSoTimeout(httpParameters, WAIT_DATA_TIMEOUT);
			httpParameters.setParameter("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10_5_8; en-US) AppleWebKit/532.5 (KHTML, like Gecko) Chrome/4.0.249.0 Safari/532.5");


			DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);
			HttpResponse httpResponse = httpClient.execute(httpRequest);
			if (entity != null) { entity.consumeContent(); }
			switch(httpResponse.getStatusLine().getStatusCode())
			{
			case 200:
				BufferedReader reader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent(), "UTF-8"));
				StringBuilder builder = new StringBuilder();
				for (String line = null; (line = reader.readLine()) != null;) {
					builder.append(line);
				}
				String result = builder.toString();
				Log.d("feedback",result);
				XmlPullParserFactory factory;
				try {
					factory = XmlPullParserFactory.newInstance();
					factory.setNamespaceAware(true);
					XmlPullParser xpp = factory.newPullParser();
					xpp.setInput( new StringReader ( result ) );
					return getPOIData(xpp);
				} catch (XmlPullParserException e) {
					e.printStackTrace();
					return null;
				}
			default:
				Log.d("feedback","1");
				return null;

			}

		} catch (UnsupportedEncodingException e) { e.printStackTrace();
		} catch (ClientProtocolException e) { e.printStackTrace();
		} catch (IOException e) { e.printStackTrace(); 
		} catch (NumberFormatException e){ e.printStackTrace(); 
		} 


		return feedback;
	}

	private static List<POIData> getPOIData(XmlPullParser xmlParser)
	{
		List<POIData> feedback = new ArrayList<POIData>();
		double prevlat =0.0;
		double prevlng =0.0;
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
					//排除重複的資料
					if(data.lat == prevlat && data.lng ==prevlng){
						continue;
					}else if(data.lat!=0.0)
					{
						prevlat = data.lat;
						prevlng = data.lng;
						feedback.add(data);
					}
				}


			}
			return feedback;
		} catch (XmlPullParserException e) {			
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
