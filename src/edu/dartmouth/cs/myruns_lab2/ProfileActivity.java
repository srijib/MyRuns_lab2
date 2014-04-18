/**
 * CS 65 14S
 * Professor: Andrew Campbell
 * 
 * @author Paul Champeau
 * Title: MyRuns_Lab2
 */
package edu.dartmouth.cs.myruns_lab2;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import android.net.Uri;
import android.os.Bundle;
import android.app.DialogFragment;
import android.os.Environment;
import android.provider.MediaStore;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class ProfileActivity extends Activity {
	private static final String TAG = "CS65";
	private Uri mImageCaptureUri;
	public static final int REQUEST_CODE_TAKE_FROM_CAMERA = 0;
	public static final int REQUEST_CODE_GALLERY = 1;
	public static final int REQUEST_CODE_CROP_PHOTO = 2;
	private static final String URI_INSTANCE_STATE_KEY = "saved_uri";
	private ImageView mImageView;
	private boolean isTakenFromCamera = false;
	private static final String IMAGE_UNSPECIFIED = "image/*";
	private byte[] photo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.profile_activity);
		mImageView = (ImageView) findViewById(R.id.imageProfile);

		//If there is a saved state then extract the photo and load the data
		if (savedInstanceState != null){
			photo = savedInstanceState.getByteArray("photo");
			loadUserData();
		}
		//else just load the previously stored data
		else
		{
			loadUserData();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void onCancelClicked(View v) {
		Toast.makeText(getApplicationContext(),
				getString(R.string.cancel_message), Toast.LENGTH_SHORT).show();
		photo = null;
		finish();
	}

	public void onChangeClicked(View v){
		displayDialog(MyRunsDialogFragment.DIALOG_ID_PHOTO_PICKER);
	}

	public void onSaveClicked(View v) {

		// Save all information from the screen into a "shared preferences"
		// using private helper function

		saveUserData();

		Toast.makeText(getApplicationContext(),
				getString(R.string.save_message), Toast.LENGTH_SHORT).show();
		finish();

	}



	private void saveUserData() {

		Log.d(TAG, "saveUserData()");

		// Getting the shared preferences editor

		String mKey = getString(R.string.preference_name);
		SharedPreferences mPrefs = getSharedPreferences(mKey, MODE_PRIVATE);

		SharedPreferences.Editor mEditor = mPrefs.edit();
		mEditor.clear();

		//Save name info
		mKey = getString(R.string.preference_key_profile_name);
		String mValue = (String) ((EditText) findViewById(R.id.edit_name))
				.getText().toString();
		mEditor.putString(mKey, mValue);

		//Save email info
		mKey = getString(R.string.preference_key_profile_email);
		mValue = (String) ((EditText) findViewById(R.id.edit_email))
				.getText().toString();
		mEditor.putString(mKey, mValue);

		//Save phone info
		mKey = getString(R.string.preference_key_profile_phone);
		mValue = (String) ((EditText) findViewById(R.id.edit_phone))
				.getText().toString();
		mEditor.putString(mKey, mValue);

		//Save class info
		mKey = getString(R.string.preference_key_profile_class);
		mValue = (String) ((EditText) findViewById(R.id.edit_class))
				.getText().toString();
		mEditor.putString(mKey, mValue);
		//Save major info
		mKey = getString(R.string.preference_key_profile_major);
		mValue = (String) ((EditText) findViewById(R.id.edit_major))
				.getText().toString();
		mEditor.putString(mKey, mValue);

		mKey = getString(R.string.preference_key_profile_gender);

		RadioGroup mRadioGroup = (RadioGroup) findViewById(R.id.radioGender);
		int mIntValue = mRadioGroup.indexOfChild(findViewById(mRadioGroup
				.getCheckedRadioButtonId()));
		mEditor.putInt(mKey, mIntValue);

		// Commit all the changes into the shared preference
		mEditor.commit();
		//save the profile picture
		saveSnap();
	}

	private void loadUserData() {

		// We can also use log.d to print to the LogCat

		Log.d(TAG, "loadUserData()");

		// Load and update all profile views

		// Get the shared preferences - create or retrieve the activity
		// preference object

		String mKey = getString(R.string.preference_name);
		SharedPreferences mPrefs = getSharedPreferences(mKey, MODE_PRIVATE);

		// Load the user email
		mKey = getString(R.string.preference_key_profile_email);
		String mValue = mPrefs.getString(mKey, " ");
		((EditText) findViewById(R.id.edit_email)).setText(mValue);

		// Load the user name
		mKey = getString(R.string.preference_key_profile_name);
		mValue = mPrefs.getString(mKey, " ");
		((EditText) findViewById(R.id.edit_name)).setText(mValue);	

		// Load the user phone
		mKey = getString(R.string.preference_key_profile_phone);
		mValue = mPrefs.getString(mKey, " ");
		((EditText) findViewById(R.id.edit_phone)).setText(mValue);	

		// Load the user major
		mKey = getString(R.string.preference_key_profile_major);
		mValue = mPrefs.getString(mKey, " ");
		((EditText) findViewById(R.id.edit_major)).setText(mValue);	

		// Load the user class
		mKey = getString(R.string.preference_key_profile_class);
		mValue = mPrefs.getString(mKey, " ");
		((EditText) findViewById(R.id.edit_class)).setText(mValue);	

		mKey = getString(R.string.preference_key_profile_gender);

		int mIntValue = mPrefs.getInt(mKey, -1);
		// In case there isn't one saved before:
		if (mIntValue >= 0) {
			// Find the radio button that should be checked.
			RadioButton radioBtn = (RadioButton) ((RadioGroup) findViewById(R.id.radioGender))
					.getChildAt(mIntValue);
			// Check the button.
			radioBtn.setChecked(true);

		}

		loadSnap();
	}

	public void takePhoto(int item) {
		Intent intent;

		switch (item) {
		case MyRunsDialogFragment.ID_PHOTO_PICKER_FROM_CAMERA:
			// Take photo from camera，
			// Construct an intent with action
			// MediaStore.ACTION_IMAGE_CAPTURE
			intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			// Construct temporary image path and name to save the taken
			// photo
			mImageCaptureUri = Uri.fromFile(new File(Environment
					.getExternalStorageDirectory(), "tmp_"
							+ String.valueOf(System.currentTimeMillis()) + ".jpg"));
			intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,
					mImageCaptureUri);
			intent.putExtra("return-data", true);
			try {
				// Start a camera capturing activity
				// REQUEST_CODE_TAKE_FROM_CAMERA is an integer tag you
				// defined to identify the activity in onActivityResult()
				// when it returns
				startActivityForResult(intent, REQUEST_CODE_TAKE_FROM_CAMERA);

			} catch (ActivityNotFoundException e) {
				e.printStackTrace();
			}
			isTakenFromCamera=true;
			break;

		default:
			return;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_OK)
			return;

		switch (requestCode) {
		case REQUEST_CODE_TAKE_FROM_CAMERA:
			// Send image taken from camera for cropping
			cropImage();
			break;

		case REQUEST_CODE_CROP_PHOTO:
			// Update image view after image crop
			Bundle extras = data.getExtras();
			// Set the picture image in UI
			if (extras != null) {
				Bitmap temp = (Bitmap) extras.getParcelable("data");
				mImageView.setImageBitmap(temp);
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				temp.compress(Bitmap.CompressFormat.PNG, 100, stream);
				photo = stream.toByteArray();
			}

			// Delete temporary image taken by camera after crop.
			if (isTakenFromCamera) {
				File f = new File(mImageCaptureUri.getPath());
				if (f.exists())
					f.delete();
			}

			break;
			
		case REQUEST_CODE_GALLERY:
			mImageCaptureUri = data.getData();
			cropImage();
			break;
		}
	}

	// Crop and resize the image for profile
	private void cropImage() {
		// Use existing crop activity.
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(mImageCaptureUri, IMAGE_UNSPECIFIED);

		// Specify image size
		intent.putExtra("outputX", 100);
		intent.putExtra("outputY", 100);

		// Specify aspect ratio, 1:1
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("scale", true);
		intent.putExtra("return-data", true);
		// REQUEST_CODE_CROP_PHOTO is an integer tag you defined to
		// identify the activity in onActivityResult() when it returns
		startActivityForResult(intent, REQUEST_CODE_CROP_PHOTO);
	}

	private void loadSnap() {

		// Load profile photo from internal storage
		if (photo != null){
			Bitmap bmap = BitmapFactory.decodeByteArray(photo, 0, photo.length);
			mImageView.setImageBitmap(bmap);
		}
		else {
			try {
				FileInputStream fis = openFileInput(getString(R.string.profile_photo_file_name));
				Bitmap bmap = BitmapFactory.decodeStream(fis);
				mImageView.setImageBitmap(bmap);
				fis.close();
			} catch (IOException e) {
				// Default profile photo if no photo saved before.
				mImageView.setImageResource(R.drawable.default_profile);
			}
		}
	}

	private void saveSnap() {

		// Commit all the changes into preference file
		// Save profile image into internal storage.
		mImageView.buildDrawingCache();
		Bitmap bmap = mImageView.getDrawingCache();
		try {
			FileOutputStream fos = openFileOutput(
					getString(R.string.profile_photo_file_name), MODE_PRIVATE);
			bmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
			fos.flush();
			fos.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		// Save UI state changes to the savedInstanceState.
		// This bundle will be passed to onCreate and
		// onRestoreInstanceState if the process is
		// killed and restarted by the run time.
		savedInstanceState.putParcelable(URI_INSTANCE_STATE_KEY, mImageCaptureUri);

		//save photo byte array to the bundle
		savedInstanceState.putByteArray("photo", photo);
		super.onSaveInstanceState(savedInstanceState);
	}
	
	public void loadFromGallery(int item){
		Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		startActivityForResult(intent,REQUEST_CODE_GALLERY);
	}
	public void displayDialog(int id) {
		DialogFragment fragment = MyRunsDialogFragment.newInstance(id);
		fragment.show(getFragmentManager(),
				getString(R.string.dialog_fragment_tag_photo_picker));
	}
}








