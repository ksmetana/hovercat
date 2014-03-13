package com.turner.rekognize;

import java.io.IOException;
import java.util.List;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
//import android.view.SurfaceHolder.Callback;

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
	
	private static final String TAG = "ReKognize";
	
	private SurfaceHolder mHolder;
	private Camera mCamera;
	
	public CameraPreview(Context context, Camera camera) {
		super(context);
		mCamera = camera;
		Log.d(TAG, "CameraPreview constructor");
		
//		// This work-around is required to keep the preview display from being grossly distorted
//		// See: https://code.google.com/p/google-glass-api/issues/detail?id=232#c1 and
//		// http://stackoverflow.com/questions/19235477/google-glass-preview-image-scrambled-with-new-xe10-release
//		Camera.Parameters parameters = camera.getParameters();
//		parameters.setPreviewFpsRange(30000, 30000);
//		mCamera.setParameters(parameters);
		
		// Install a SurfaceHolder.Callback so we get notified when the underlying
		// surface is created and destroyed
		mHolder = getHolder();
		mHolder.addCallback(this);
		
		// deprecated setting, but required on Android versions prior to 3.0
		// do we need this??
		mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}
	
	public void surfaceCreated(SurfaceHolder holder) {
		// The surface is created, now tell  the camera where to draw the preview
		Log.d(TAG, "surfaceCreated()");
		try {
			mCamera.setPreviewDisplay(holder);
			mCamera.startPreview();
		}
		catch (IOException e) {
			Log.d(TAG, "Error setting camera preview: " + e.getMessage());
		}
	}
	
	public void surfaceDestroyed(SurfaceHolder holder) {
		// Empty - make sure to release camera preview in activity
		Log.d(TAG, "surfaceDestroyed()");
	}
	
	public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
		// If the preview can change or rotate, take care of that here.
		// Make sure to stop the preview before resizing or reformatting it.
		Log.d(TAG, "surfaceChanged()");
		
		if (mHolder.getSurface() == null) {
			// Preview surface does not exist
			return;
		}
		
		// Stop preview before making changes
		try {
			Log.d(TAG, "stopping preview");
			mCamera.stopPreview();
		}
		catch (Exception e) {
			// Ignore: tried to stop a non-existent preview
		}
		
		// Set preview size and make any resize, rotate, or reformatting
		// changes here
		
		// Start preview with new settings
		try {
			Log.d(TAG, "starting preview");
			mCamera.setPreviewDisplay(mHolder);
			mCamera.startPreview();
		}
		catch (Exception e) {
			Log.d(TAG, "Error starting camera preview: " + e.getMessage());
		}
	}
}
