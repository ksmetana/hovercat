package com.turner.rekognize;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ResultsActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.results);
		
		Intent intent = getIntent();
		FaceRecognition faceRecognition = (FaceRecognition) intent.getSerializableExtra("com.turner.rekognize.FaceRecognitionBean");
		
		String faceDetected;
		
		TextView faceDetectedTextView = (TextView) findViewById(R.id.face_detected);
		TextView detectionConfidenceTextView = (TextView) findViewById(R.id.detection_confidence);
		TextView matchConfidenceTextView = (TextView) findViewById(R.id.match_confidence);
		TextView matchNameTextView = (TextView) findViewById(R.id.match_name);
		TextView noFaceDetectedTextView = (TextView) findViewById(R.id.no_face_detected);
		
		if (faceRecognition.isFaceDetected() == true) {
			faceDetected = "Face detected";
			faceDetectedTextView.setText(faceDetected.toString());
			
			String detectionConfidenceString = "Detection confidence: " + Float.toString(faceRecognition.getDetectionConfidence());
			detectionConfidenceTextView.setText(detectionConfidenceString);
			
			float matchConfidence = faceRecognition.getMatchConfidence();
			String matchConfidenceString;
 			if (matchConfidence > 66) {
				matchConfidenceString = "Probable match: " + Float.toString(matchConfidence);
			}
 			else if (matchConfidence > 32) {
 				matchConfidenceString = "Possible match: " + Float.toString(matchConfidence);
 			}
 			else {
 				matchConfidenceString = "Unlikely match: " + Float.toString(matchConfidence);
 			}
 			matchConfidenceTextView.setText(matchConfidenceString);
 			
 			String matchNameString = "Match: " + faceRecognition.getMatchName();
 			matchNameTextView.setText(matchNameString);
			
 			// Hide the "no face detected" text view
 			noFaceDetectedTextView.setVisibility(View.GONE);		
		}
		else {
			faceDetected = "No face detected";
			noFaceDetectedTextView.setText(faceDetected);
			
			// Hide text views
			faceDetectedTextView.setVisibility(View.GONE);
			detectionConfidenceTextView.setVisibility(View.GONE);
			matchConfidenceTextView.setVisibility(View.GONE);
			matchNameTextView.setVisibility(View.GONE);
		}
		
	}

}
