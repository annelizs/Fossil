package fossil.house;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class TakePictureActivity extends Activity {

	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 0;


	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState); 
		setContentView(R.layout.main);



		Button btn = (Button) findViewById(R.id.btn);
		btn.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				//define the file-name to save photo taken by Camera activity
				String fileName = "newFossil.jpg";

				//create parameters for Intent with filename
				ContentValues values = new ContentValues();
				values.put(MediaStore.Images.Media.TITLE, fileName);

				//imageUri is the current activity attribute, define and save it for later usage (also in onSaveInstanceState)
				Uri imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

				//create new Intent

				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
				startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);

			}

		});
	
	}
	
	
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
		    if (resultCode == RESULT_OK) {
		        //use imageUri here to access the image
		    	
		    	Toast.makeText(this, "Great! Ready to enter some metadata?", Toast.LENGTH_SHORT);
		    	
		    	Log.e("TakePicture", "I got to the successful toast");
		    	
		    	
		            Intent intent = new Intent(TakePictureActivity.this, EnterMetadata.class);
		            startActivity(intent);
		          


		    } else if (resultCode == RESULT_CANCELED) {
		        Toast.makeText(this, "Picture was not taken", Toast.LENGTH_SHORT);
		    } else {
		        Toast.makeText(this, "Picture was not taken", Toast.LENGTH_SHORT);
		    }
		}
		}

	
}