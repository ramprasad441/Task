package com.instademo.presentation;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.example.instademo.R;
import com.instademo.framework.InstagramWebView;

public class InstaDemoActivity extends Activity {

	private static final String URL = "https://api.instagram.com/v1/tags/selfie/media/recent?access_token=1711995165.5c40135.8425b377a4644dcfa3bb5843a167e70f";
	private JSONObject information;
	private GridView imageGridView;
	private static int TILE_WIDTH = 220;
	int number = 0;
	RetrieveInstagramImages retriveImages;
	Context ctx;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		imageGridView = (GridView) findViewById(R.id.image_grid_view);

		retriveImages = new RetrieveInstagramImages(
				URL,
				this);
		retriveImages.execute();

		ctx = this;

		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		imageGridView.setNumColumns(metrics.widthPixels / TILE_WIDTH);

		imageGridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {

				Intent intent = new Intent(InstaDemoActivity.this, InstaDemoImageActivity.class);

				try {

					String url = information.getJSONArray("data")
							.getJSONObject(position).getJSONObject("images")
							.getJSONObject("standard_resolution")
							.getString("url");
					intent.putExtra("url", url);
				} catch (JSONException e) {
					intent.putExtra("url", "");
				}

				startActivity(intent);
			}
		});
		imageGridView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
			}
		});

	}

	private class RetrieveInstagramImages extends AsyncTask<Void, Void, Void> {
		private String instagramUrl;
		private Context ctx;

		public RetrieveInstagramImages(String instagramUrl, Context ctx) {
			super();
			this.instagramUrl = instagramUrl;
			this.ctx = ctx;
		}

		@Override
		protected Void doInBackground(Void... params) {
			information = InstagramWebView.makeWebServiceCall(instagramUrl);
			return null;
		}

		@Override
		protected void onPostExecute(Void unused) {
			imageGridView.setAdapter(new InstaDemoGridViewAdapter(ctx, information, number));
		}

	}

}