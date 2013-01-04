package net.openwatch.acluaz;

import java.io.File;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

import net.openwatch.acluaz.constants.Constants;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.webkit.WebView;
import android.support.v4.app.NavUtils;

public class PDFViewActivity extends SherlockActivity {
	private static final String TAG = "PDFViewActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if(this.getIntent().hasExtra(Constants.ASSET_PATH)){
			String path = this.getIntent().getExtras().getString(Constants.ASSET_PATH);
			WebView webview = new WebView(this);
			webview.getSettings().setBuiltInZoomControls(true);
			Log.i(TAG, path);
			//webview.loadUrl(path);
			Log.i(TAG, "file:///android_asset/" + Constants.PDF_ASSETS_DIR + File.separator + path);
			webview.loadUrl("file:///android_asset/" + Constants.PDF_ASSETS_DIR + File.separator + path);
			setContentView(webview);
		}else{
			setContentView(R.layout.activity_pdfview);
			// this should never happen but TODO: indicate failure to load asset
		}
		// Show the Up button in the action bar.
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getSupportMenuInflater().inflate(R.menu.activity_pdfview, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
