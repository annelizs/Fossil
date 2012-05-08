package fossil.house;
import java.sql.SQLException;
import java.util.Random;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;

public class FossilMain extends AppWidgetProvider 
{

	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) 
	{
		Log.e("fossilMain", "On update was called!");
		String result = "";

		try{
			HttpClient httpclient = new DefaultHttpClient();
			HttpGet httpget = new HttpGet("http://blanu.net/fossils.json");
			HttpResponse response = httpclient.execute(httpget);
			HttpEntity entity = response.getEntity();
			InputStream is = entity.getContent();
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			result=sb.toString();
			
		}catch(Exception e){
			Log.e("log_tag", "Error converting result "+e.toString());
		}

		//parse json data
		try{
			JSONArray jArray = new JSONArray(result);

			try 
			{
				Random rand = new Random();
				DisplayFossil(jArray.getJSONArray(rand.nextInt(jArray.length())), context, appWidgetManager);

			} 
			catch (SQLException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		catch(JSONException e)
		{
			Log.e("log_tag", "Error parsing data "+e.toString());
		}
	}


	private Bitmap downloadFile(String fileUrl)
	{
		Bitmap bmImg;

		URL myFileUrl =null; 
		try {
			myFileUrl= new URL(fileUrl);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			HttpURLConnection conn= (HttpURLConnection)myFileUrl.openConnection();
			conn.setDoInput(true);
			conn.connect();

			InputStream is = conn.getInputStream();

			bmImg = BitmapFactory.decodeStream(is); 
			Log.e("FossilMain", "downloaded the file!");

			Bitmap bmImgResize = Bitmap.createScaledBitmap(bmImg, 75, 75, false);
			return bmImgResize;


		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}


	public void DisplayFossil(JSONArray jArray, Context context, AppWidgetManager appWidgetManager) throws SQLException
	{
		RemoteViews remoteViews;
		ComponentName thisWidget;

		try {

			String commonName = jArray.getString(0);
			String latinName = jArray.getString(1);
			String location = jArray.getString(2);
			String image = jArray.getString(3);

			remoteViews = new RemoteViews(context.getPackageName(), R.layout.main);
			thisWidget = new ComponentName(context, FossilMain.class);

			Intent defineIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.paleocentral.org/"));
			PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, defineIntent, 0);
			remoteViews.setOnClickPendingIntent(R.id.fossilLayout, pendingIntent);

			remoteViews.setTextViewText(R.id.fossil_common_name, commonName);
			remoteViews.setTextViewText(R.id.fossil_latin_name, latinName);
			remoteViews.setTextViewText(R.id.location, location);
			Log.e("FossilMain", "I'm about to set teh image. Maybe I'll crash. WHO KNOWS");


			appWidgetManager.updateAppWidget(thisWidget, remoteViews);

			Bitmap bmImg = downloadFile(image);


			Bitmap proxy = Bitmap.createBitmap(bmImg.getWidth(), bmImg.getHeight(), Config.ARGB_8888);
			Canvas c = new Canvas(proxy);
			c.drawBitmap(bmImg, new Matrix(), null);


			remoteViews.setImageViewBitmap(R.id.fossil_image, proxy);

			Log.e("FossilMain", "I set the image!");

			appWidgetManager.updateAppWidget(thisWidget, remoteViews);

			Log.e("FossilMain", "If I make it this far, I'm made out of voodoo");



		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}




