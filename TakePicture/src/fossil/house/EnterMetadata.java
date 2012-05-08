package fossil.house;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import com.couchbase.client.CouchbaseClient;

public class EnterMetadata extends Activity {


	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState); 
		setContentView(R.layout.metadata);
		
		
		//Display the image the person just took
		//define the file-name to save photo taken by Camera activity
		String fileName = "newFossil.jpg";

		//create parameters for Intent with filename
		ContentValues values = new ContentValues();
		values.put(MediaStore.Images.Media.TITLE, fileName);

		//imageUri is the current activity attribute, define and save it for later usage (also in onSaveInstanceState)
		Uri imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
		
		Log.e("enterMetadata", "I set the imageUri");
		
		ImageView imageView = (ImageView) findViewById(R.id.newFossil);				
		imageView.setImageURI(imageUri);

		Log.e("enterMetadata", imageUri.toString());
		Log.e("enterMetadata", "I displayed the image");
		
		// Acquire a reference to the system Location Manager
		LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

		// Define a listener that responds to location updates
		LocationListener locationListener = new LocationListener() {
			public void onLocationChanged(Location location) {
				// Called when a new location is found by the network location provider.
				putLocationInTextField(location);
				
				Log.e("locationChanger", "supposedly the location will be changed at some point here. We'll see.");
			}

			public void putLocationInTextField(Location location) {

				TextView textView = (TextView) findViewById(R.id.location);				
				textView.setText(location.toString());				
				
			}

			public void onStatusChanged(String provider, int status, Bundle extras) {}

			public void onProviderEnabled(String provider) {}

			public void onProviderDisabled(String provider) {}
		};
		

		// Register the listener with the Location Manager to receive location updates
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

		
		Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		if(lastKnownLocation !=  null)
		{
		TextView textView = (TextView) findViewById(R.id.location);				
		textView.setText(lastKnownLocation.toString());
		}
		
		//wtf why can't I instantiate
		//Anyway, use the calendar class to get the time/date
		Calendar c = Calendar.getInstance();

		TextView textView = (TextView) findViewById(R.id.date);				
		textView.setText(c.getTime().toString());

		
		

		Button btn = (Button) findViewById(R.id.send_metadata);
		btn.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{


				EditText editText = (EditText) findViewById(R.id.name);				
				Editable editable = editText.getText();
				String name = editable.toString();

				editText = (EditText) findViewById(R.id.fossilName);				
				editable = editText.getText();				
				String fossilName = editable.toString();

				editText = (EditText) findViewById(R.id.notes);				
				editable = editText.getText();				
				String notes = editable.toString();





				JSONObject json = new JSONObject();

				try {
					json.put("name", name);
					//					json.put("location", location);
					//					json.put("date", date);
					json.put("fossilName", fossilName);
					json.put("notes", notes);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				String jsonFile = json.toString();


				try{


					HttpClient httpclient = new DefaultHttpClient();
					HttpPost httppost = new HttpPost("http://elizabeth.iriscouch.com/fossil_pictures/");

					StringEntity se = new StringEntity(jsonFile);  
					se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
					httppost.setEntity(se);
					HttpResponse result = httpclient.execute(httppost);

					Log.e("EnterMetadata", "Status: "+ result.getStatusLine().getStatusCode());



				}catch(Exception e){
					Log.e("log_tag", "Error converting result "+e.toString());
				}

				showDialog(0);


			}
		});
	}

	@Override
	protected Dialog onCreateDialog(int id)
	{

		return new AlertDialog.Builder(this)
		.setTitle("Your fossil has been successfully submitted to the Database!")
		.setPositiveButton("Submit Another Fossil", new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int whichButton)
			{
				Intent intent = new Intent(EnterMetadata.this, TakePictureActivity.class);
				startActivity(intent);
			}
		})
		.setNegativeButton("All Done", new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int whichButton)
			{

				Intent intent = new Intent(Intent.ACTION_MAIN);
				intent.addCategory(Intent.CATEGORY_HOME);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);

			}
		})
		.create();
	}






}










