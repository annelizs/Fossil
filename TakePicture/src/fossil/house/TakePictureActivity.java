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
		
//		Button btn2 = (Button) findViewById(R.id.btn);
//		btn2.setOnClickListener(new View.OnClickListener()
//		{
//			public void onClick(View v)
//			{
//				//ASK USER IF HE/SHE WANTS TO GET A PICTURE FROM HIS/HER IMAGE GALLERY
//				
//				//Something like this:
//				public class BrowsePicture extends Activity {
//
//				    //YOU CAN EDIT THIS TO WHATEVER YOU WANT
//				    private static final int SELECT_PICTURE = 1;
//
//				    private String selectedImagePath;
//				    //ADDED
//				    private String filemanagerstring;
//
//				    public void onCreate(Bundle savedInstanceState) {
//				        super.onCreate(savedInstanceState);
//				        setContentView(R.layout.main);
//
//				        ((Button) findViewById(R.id.Button01))
//				        .setOnClickListener(new OnClickListener() {
//
//				            public void onClick(View arg0) {
//
//				                // in onCreate or any event where your want the user to
//				                // select a file
//				                Intent intent = new Intent();
//				                intent.setType("image/*");
//				                intent.setAction(Intent.ACTION_GET_CONTENT);
//				                startActivityForResult(Intent.createChooser(intent,
//				                        "Select Picture"), SELECT_PICTURE);
//				            }
//				        });
//				    }
//
//				    //UPDATED
//				    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//				        if (resultCode == RESULT_OK) {
//				            if (requestCode == SELECT_PICTURE) {
//				                Uri selectedImageUri = data.getData();
//
//				                //OI FILE Manager
//				                filemanagerstring = selectedImageUri.getPath();
//
//				                //MEDIA GALLERY
//				                selectedImagePath = getPath(selectedImageUri);
//
//				                //DEBUG PURPOSE - you can delete this if you want
//				                if(selectedImagePath!=null)
//				                    System.out.println(selectedImagePath);
//				                else System.out.println("selectedImagePath is null");
//				                if(filemanagerstring!=null)
//				                    System.out.println(filemanagerstring);
//				                else System.out.println("filemanagerstring is null");
//
//				                //NOW WE HAVE OUR WANTED STRING
//				                if(selectedImagePath!=null)
//				                    System.out.println("selectedImagePath is the right one for you!");
//				                else
//				                    System.out.println("filemanagerstring is the right one for you!");
//				            }
//				        }
//				    }
//
//				    //UPDATED!
//				    public String getPath(Uri uri) {
//				        String[] projection = { MediaStore.Images.Media.DATA };
//				        Cursor cursor = managedQuery(uri, projection, null, null, null);
//				        if(cursor!=null)
//				        {
//				            //HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
//				            //THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
//				            int column_index = cursor
//				            .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//				            cursor.moveToFirst();
//				            return cursor.getString(column_index);
//				        }
//				        else return null;
//				    }
//
//			}
//		});
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