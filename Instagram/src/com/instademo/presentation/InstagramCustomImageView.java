package com.instademo.presentation;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.ImageView;

public class InstagramCustomImageView extends ImageView {

	private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
		@Override
		public boolean onScale(ScaleGestureDetector detector) {
			scl *= detector.getScaleFactor();
			scl = Math.max(minimumZoom, Math.min(scl, maximumZoom));

			matrixTransformation.setScale(scl, scl);
			matrixTransformation.postTranslate(-306 * scl, -306 * scl);

			invalidate();
			return true;
		}
	}
	private static float minimumZoom = 0.5f, maximumZoom = 3f;
	private ScaleGestureDetector scaleGestureDetector;
	private float scl = 1f;

	private Matrix matrixTransformation;
	private int pointerId = -1;
	private float lastX, lastY;

	private int offsetX = 0, offsetY = 0;
	private int width, height;

	private int imageWidth, imageHeight;

	public InstagramCustomImageView(Context context) {
		super(context);
	}

	public InstagramCustomImageView(Context context, AttributeSet set) {
		super(context, set);
	}

	public InstagramCustomImageView(Context context, AttributeSet set, int defStyle) {
		super(context, set, defStyle);
	}

	public void fitToScreen() {
		int edgeX;
		if (scl * imageWidth >= width)
			edgeX = (int) ((scl * imageWidth - width) / 2);
		else
			edgeX = (int) ((width - scl * imageWidth) / 2);

		if (offsetX > edgeX)
			offsetX = edgeX;
		else if (offsetX < -edgeX)
			offsetX = -edgeX;

		int edgeY;
		if (scl * imageHeight <= height)
			edgeY = (int) ((scl * imageHeight - height) / 2);
		else
			edgeY = (int) ((height - scl * imageHeight) / 2);

		if (offsetY < edgeY)
			offsetY = edgeY;
		else if (offsetY > -edgeY)
			offsetY = -edgeY;
	}

	@Override
	public void onDraw(Canvas canvas) {
		BitmapDrawable draw = (BitmapDrawable) this.getDrawable();
		if (draw != null) {
			canvas.translate(width / 2 + offsetX, height / 2 + offsetY);
			canvas.drawBitmap(draw.getBitmap(), matrixTransformation, null);
		}
	}
	
	
	

	@Override
	public boolean onTouchEvent(MotionEvent event) {
				if (scaleGestureDetector != null)
			scaleGestureDetector.onTouchEvent(event);
		final int action = MotionEventCompat.getActionMasked(event);
		switch (action) {
		case MotionEvent.ACTION_DOWN: {
			final int pointerIndex = MotionEventCompat.getActionIndex(event);
			lastX = MotionEventCompat.getX(event, pointerIndex);
			lastY = MotionEventCompat.getY(event, pointerIndex);
			pointerId = MotionEventCompat.getPointerId(event, pointerIndex);
			break;
		}

		case MotionEvent.ACTION_MOVE: {
			final int pointerIndex = MotionEventCompat.findPointerIndex(event, pointerId);
			if (scaleGestureDetector != null && !scaleGestureDetector.isInProgress()) {
				final float dx = MotionEventCompat.getX(event, pointerIndex) - lastX;
				final float dy = MotionEventCompat.getY(event, pointerIndex) - lastY;
				offsetX += dx;
				offsetY += dy;
				lastX = MotionEventCompat.getX(event, pointerIndex);
				lastY = MotionEventCompat.getY(event, pointerIndex);

				invalidate();
			}

			break;
		}

		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL: {
			pointerId = -1;
			break;
		}

		case MotionEvent.ACTION_POINTER_UP: {
			final int pointerIndex = MotionEventCompat.getActionIndex(event);
			final int pointerID = MotionEventCompat.getPointerId(event, pointerIndex);
			if (pointerID == pointerId) {
				final int newPointerIndex = (pointerIndex == 0 ? 1 : 0);
				lastX = MotionEventCompat.getX(event, newPointerIndex);
				lastY = MotionEventCompat.getY(event, newPointerIndex);
				pointerId = MotionEventCompat.getPointerId(event, newPointerIndex);
			}
			break;
		}
		}

		fitToScreen();
		return true;
	}

	@Override
	public void setImageBitmap(Bitmap bm) {
		super.setImageBitmap(bm);
		imageWidth = bm.getWidth();
		imageHeight = bm.getHeight();
		setupPinchZoom();
	}

	public void setupPinchZoom() {
		DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
		width = displayMetrics.widthPixels;
		height = displayMetrics.heightPixels;
		if (width > height) {
			scl = (float) height / imageHeight;
		} else {
			scl = (float) width / imageWidth;
		}
		if (scl > maximumZoom)
			maximumZoom = scl;
		if (scl < minimumZoom)
			minimumZoom = scl;
		matrixTransformation = new Matrix();
		matrixTransformation.setScale(scl, scl);
		matrixTransformation.postTranslate(-306 * scl, -306 * scl);
		scaleGestureDetector = new ScaleGestureDetector(getContext(), new ScaleListener());
	}
}
