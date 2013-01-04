package net.openwatch.acluaz;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import net.openwatch.acluaz.constants.Constants;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class RightsActivity extends SherlockActivity {
	private static final String TAG = "RightsActivity";
	
	static enum LANGUAGE { ENGLISH, SPANISH }
	
	LANGUAGE language = LANGUAGE.ENGLISH;
	
	// Relate language name to asset directory
	final HashMap<LANGUAGE, String> LANGUAGE_MAP = new HashMap<LANGUAGE, String>(){
		{
			put(LANGUAGE.ENGLISH, "eng");put(LANGUAGE.SPANISH, "esp");
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rights);
		// Show the Up button in the action bar.
		this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		CompoundButton toggle = (CompoundButton) this.findViewById(R.id.language_toggle);
		toggle.setOnCheckedChangeListener(new OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if(!isChecked)
					language = LANGUAGE.SPANISH;
				else
					language = LANGUAGE.ENGLISH;
			}
			
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getSupportMenuInflater().inflate(R.menu.activity_rights, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void onSb1070KyrClicked(View v){
		/*
		AssetFileDescriptor descriptor;
		try {
			descriptor = getAssets().openFd(Constants.PDF_ASSETS_DIR + File.separator + LANGUAGE_MAP.get(language) + File.separator + Constants.SB1070_KYR);
			Log.i(TAG, descriptor.toString());
			showAsset(descriptor.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		*/
		showAsset(LANGUAGE_MAP.get(language) + File.separator + Constants.SB1070_KYR);
	}
	
	public void onSb1070InfoClicked(View v){
		showAsset(LANGUAGE_MAP.get(language) + File.separator + Constants.SB1070_INFOGRAPHIC);
	}
	
	public void onBustCardClicked(View v){
		showAsset(LANGUAGE_MAP.get(language) + File.separator + Constants.BUST_CARD);
	}
	
	private void showAsset(String asset_path){
		if(asset_path == null)
			return;
		
		Intent i = new Intent(RightsActivity.this, PDFViewActivity.class);
		i.putExtra(Constants.ASSET_PATH, asset_path);
		startActivity(i);
	}

}
