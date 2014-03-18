package com.turner.rekognize;

import java.io.Serializable;

public class FaceRecognition implements Serializable {

	private boolean faceDetected;
	private float detectionConfidence;
	private String matchName;
	private float matchConfidence;
	
	public FaceRecognition() {
		super();
	}
	
	public boolean isFaceDetected() {
		return faceDetected;
	}

	public void setFaceDetected(boolean faceDetected) {
		this.faceDetected = faceDetected;
	}
	
	public float getDetectionConfidence() {
		return detectionConfidence;
	}

	public void setDetectionConfidence(float detectionConfidence) {
		this.detectionConfidence = detectionConfidence;
	}

	public String getMatchName() {
		return matchName;
	}

	public void setMatchName(String matchName) {
		this.matchName = matchName;
	}

	public float getMatchConfidence() {
		return matchConfidence;
	}

	public void setMatchConfidence(float matchConfidence) {
		this.matchConfidence = matchConfidence;
	}
	
}
