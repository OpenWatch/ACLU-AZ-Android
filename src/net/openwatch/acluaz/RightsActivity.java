//  Created by David Brodsky
//  Copyright (c) 2013 OpenWatch FPC. All rights reserved.
//
//  This file is part of ACLU-AZ-Android.
//
//  ACLU-AZ-Android is free software: you can redistribute it and/or modify
//  it under the terms of the GNU General Public License as published by
//  the Free Software Foundation, either version 3 of the License, or
//  (at your option) any later version.
//
//  ACLU-AZ-Android is distributed in the hope that it will be useful,
//  but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//  GNU General Public License for more details.
//
//  You should have received a copy of the GNU General Public License
//  along with ACLU-AZ-Android.  If not, see <http://www.gnu.org/licenses/>.
package net.openwatch.acluaz;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextSwitcher;
import android.widget.ViewSwitcher;

public class RightsActivity extends SherlockActivity implements ViewSwitcher.ViewFactory {
	private static final String TAG = "RightsActivity";

	TextSwitcher rights_content;
	ScrollView rights_container;
	
	enum STATE { NONE, GENERAL, CAR, STREET, HOME, JAIL };
	
	STATE state = STATE.NONE;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rights);
		// Show the Up button in the action bar.
		this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		rights_content = (TextSwitcher) findViewById(R.id.rights_content);
		rights_content.setFactory(this);
		rights_content.setInAnimation(AnimationUtils.loadAnimation(this,
                android.R.anim.slide_in_left));
		rights_content.setOutAnimation(AnimationUtils.loadAnimation(this,
                android.R.anim.slide_out_right));
		
		rights_container = (ScrollView) findViewById(R.id.rights_container);
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
		if(state != STATE.GENERAL){
			scrollUpTextContainer();
			rights_content.setText(Html.fromHtml(getString(R.string.general_rights)));
			state = STATE.GENERAL;
		}
	}

	public void carClicked(View v) {
		if(state != STATE.CAR){
			scrollUpTextContainer();
			rights_content.setText(Html.fromHtml(getString(R.string.car_rights)));
			state = STATE.CAR;
		}
	}

	public void streetClicked(View v) {
		if(state != STATE.STREET){
			scrollUpTextContainer();
			rights_content.setText(Html.fromHtml(getString(R.string.street_rights)));
			state = STATE.STREET;
		}
	}

	public void homeClicked(View v) {
		if(state != STATE.HOME){
			scrollUpTextContainer();
			rights_content.setText(Html.fromHtml(getString(R.string.home_rights)));
			state = STATE.HOME;
		}
	}

	public void jailClicked(View v) {
		if(state != STATE.JAIL){
			scrollUpTextContainer();
			rights_content.setText(Html.fromHtml(getString(R.string.jail_rights)));
			state = STATE.JAIL;
		}
	}

	@Override
	public View makeView() {
		return View.inflate(this, R.layout.rights_content_textview, null);
	}
	
	private void scrollUpTextContainer(){
		if(rights_container != null)
			rights_container.scrollTo(0, 0);
	}
	

}
