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
import android.widget.TextView;

public class RightsActivity extends SherlockActivity {
	private static final String TAG = "RightsActivity";

	TextView rights_content;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rights);
		// Show the Up button in the action bar.
		this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		rights_content = (TextView) findViewById(R.id.rights_content);
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

	public void generalClicked(View v) {
		rights_content.setText(R.string.general_rights);
	}

	public void carClicked(View v) {
		rights_content.setText(R.string.car_rights);
	}

	public void streetClicked(View v) {
		rights_content.setText(R.string.street_rights);
	}

	public void homeClicked(View v) {
		rights_content.setText(R.string.home_rights);
	}

	public void jailClicked(View v) {
		rights_content.setText(R.string.jail_rights);
	}

}
