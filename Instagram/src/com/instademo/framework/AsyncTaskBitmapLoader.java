package com.instademo.framework;

import java.lang.ref.WeakReference;
import java.util.HashMap;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ImageView;

public class AsyncTaskBitmapLoader extends AsyncTask<String, Void, Bitmap> {
	private static HashMap<String, Bitmap> cached;

	private final WeakReference<ImageView> referencedImage;

	private String url;

	/**
	 * Downloads bitmap and sets to ImageView
	 */
	public AsyncTaskBitmapLoader(ImageView imageView) {
		referencedImage = new WeakReference<ImageView>(imageView);

		if (cached == null) {
			cached = new HashMap<String, Bitmap>();
		}
	}

	protected Bitmap doInBackground(String... params) {
		url = params[0];

		if (cached.containsKey(url)) {
			return cached.get(url);
		}

		return InstagramWebView.retriveBitmap(url);
	}

	@Override
	protected void onPostExecute(Bitmap bitmap) {
		cached.put(url, bitmap);

		if (isCancelled()) {
			return;
		}

		if (referencedImage != null) {
			ImageView imageView = referencedImage.get();
			if (imageView != null) {

				imageView.setImageBitmap(bitmap);
			}
		}
	}

	public boolean searchCache(String url) {
		if (referencedImage != null) {
			ImageView imageView = referencedImage.get();
			if (imageView != null) {

				if (cached.containsKey(url)) {

					imageView.setImageBitmap(cached.get(url));

					return true;
				}
			}
		}

		return false;
	}

}