package com.turner.rekognize;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.FileObserver;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import com.google.android.glass.media.CameraManager;
import com.google.android.glass.touchpad.Gesture;
import com.google.android.glass.touchpad.GestureDetector;

public class MainActivity extends Activity {
	
	//public static final int MEDIA_TYPE_IMAGE = 1;
	
	private static final String TAG = "ReKognize";
	
	private GestureDetector mGestureDetector;
	
	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
	//private Uri imageFileUri;
	
	private Camera mCamera;
	private CameraPreview mPreview;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "MainActivity - onCreate()");
		
		// Immersive experience
		//setContentView(R.layout.main);
		setContentView(R.layout.camera_preview);
		
		mCamera = getCameraInstance();
		setCameraParameters();
		
		// Create a CameraPreview view and set it as the content of this activity
		mPreview = new CameraPreview(this, mCamera);
		FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
		preview.addView(mPreview);
		
		mGestureDetector = createGestureDetector(this);
	}
	
	@Override
	protected void onResume() {
		Log.d(TAG, "MainActivity - onResume()");
		super.onResume();
		
		if (mCamera == null) {
			Log.d(TAG, "onResume - camera is null");
			
//			mCamera = getCameraInstance();
//			
//			// Create a CameraPreview view and set it as the content of this activity
//			mPreview = new CameraPreview(this, mCamera);
//			FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
//			preview.addView(mPreview);
		}
	}
	
	@Override
	protected void onPause() {
		Log.d(TAG, "MainActivity - onPause()");
		
		super.onPause();
		if (mCamera != null) {
			Log.d(TAG, "onPause - camera is not null");
			//mCamera.stopPreview();
			mCamera.release();
//			mCamera = null;
//			mPreview = null;
		}
	}
	
	@Override
	protected void onDestroy() {
		Log.d(TAG, "MainActivity - onPause()");
		
		super.onDestroy();
		if (mCamera != null) {
			Log.d(TAG, "onDestroy - camera is not null");
			//mCamera.stopPreview();
//			mCamera.release();
//			mCamera = null;
//			mPreview = null;
		}
	}
	
	private GestureDetector createGestureDetector(Context context) {
		GestureDetector gestureDetector = new GestureDetector(context);
		gestureDetector.setBaseListener(new GestureDetector.BaseListener() {
			@Override
			public boolean onGesture(Gesture gesture) {
				if (gesture == Gesture.TAP) {
					
					// Activate camera here
					Log.d(TAG, "tap gesture detected");
					takePicture();
					return true;
				}
				return false;
			}
		});
		
		return gestureDetector;
	}
	
	@Override
	public boolean onGenericMotionEvent(MotionEvent event) {
		if (mGestureDetector != null) {
			return mGestureDetector.onMotionEvent(event);
		}
		return false;
	}
	
	private void takePicture() {
		// Create Intent to take picture and return control to the calling application
		Log.d(TAG, "takePicture()");
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.d(TAG, "onActivityResult()");
		if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
			String picturePath = data.getStringExtra(CameraManager.EXTRA_PICTURE_FILE_PATH);
			processPictureWhenReady(picturePath);
		}
		
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	private void processPictureWhenReady(final String picturePath) {
		final File pictureFile = new File(picturePath);
		Log.d(TAG, "processPictureWhenReady()");
		
		if (pictureFile.exists()) {
			// The picture file is ready; process it
			String msg = String.format("picture file exists: %s", pictureFile.getPath());
			Log.d(TAG, msg);
			startActivity(new Intent(MainActivity.this, MainActivity.class));
			finish();
		}
		else {
			// The file does not exist yet
			Log.d(TAG, "picture file does not exist");
			
			final File parentDirectory = pictureFile.getParentFile();
			FileObserver observer = new FileObserver(parentDirectory.getPath()) {
				// Protect against additional pending events after CLOSE_WRITE is handled
				private boolean isFileWritten;
				
				@Override
				public void onEvent(int event, String path) {
					if (!isFileWritten) {
						// For safety, make sure that the file that was created in 
						// the directory is actually the one we're expecting
						File affectedFile = new File(parentDirectory, path);
						isFileWritten = (event == FileObserver.CLOSE_WRITE && affectedFile.equals(pictureFile));
						
						if (isFileWritten) {
							stopWatching();
							
							// The file is ready; recursively call processPictureWhenReady()
							// again (on the UI thread)
							runOnUiThread(new Runnable() {
								@Override
								public void run() {
									processPictureWhenReady(picturePath);
								}
							});
							
							Log.d(TAG, "picture file is written");
						}
					}
				}
			};
			
			observer.startWatching();
		}
	}
	
	/**
	 * Get an instance of Camera
	 */
	public static Camera getCameraInstance() {
		Camera c = null;
		try {
			c = Camera.open();
		}
		catch (Exception e) {
			// Camera is not available (in use or does not exist - the latter is not true on Glass)
		}
		
		return c;
	}
	
	private void setCameraParameters() {
		if (mCamera != null) {
			// This work-around is required to keep the preview display from being grossly distorted
			// See: https://code.google.com/p/google-glass-api/issues/detail?id=232#c1 and
			// http://stackoverflow.com/questions/19235477/google-glass-preview-image-scrambled-with-new-xe10-release
			Camera.Parameters parameters = mCamera.getParameters();
			parameters.setPreviewFpsRange(30000, 30000);
			mCamera.setParameters(parameters);
		}
	}
	
}
