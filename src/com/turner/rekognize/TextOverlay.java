package com.turner.rekognize;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.os.Looper;
import android.view.View;

public class TextOverlay extends View {
	
	private Paint mPaint;
	private String mText;
	
	public TextOverlay(Context context) {
		super(context);
		
		mPaint = new Paint();
		mText = "";
	}
	
	protected void onDraw(Canvas canvas) {
		//Paint paint = new Paint();
		mPaint.setColor(Color.TRANSPARENT);
		mPaint.setStyle(Style.FILL);
		canvas.drawPaint(mPaint);
		
		mPaint.setColor(Color.WHITE);
		mPaint.setTextAlign(Align.CENTER);
		mPaint.setTextSize(60);
		canvas.drawText(mText, 320, 180, mPaint);
	}
	
	public void setText(String text) {
		mText = text;
		
		// Need to call the appropriate invalidate method based on whether or
		// not we're on the main thread
		if (Looper.getMainLooper().getThread() == Thread.currentThread()) {
			invalidate();
		}
		else {
			postInvalidate();
		}
		
	}
}
