package com.instademo.presentation;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.example.instademo.R;
import com.instademo.framework.AsyncTaskBitmapLoader;

public class InstaDemoImageActivity extends Activity {

	InstagramCustomImageView image;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.image_activity);

		
		Intent intent = getIntent();
		String url = intent.getExtras().getString("url");

		if (url.length() > 0) {

			image = (InstagramCustomImageView) findViewById(R.id.insta_image);

			AsyncTaskBitmapLoader downloadTask = new AsyncTaskBitmapLoader(image);
			if (!downloadTask.searchCache(url))
				downloadTask.execute(url);

		}
	}
}

