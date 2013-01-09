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
package net.openwatch.acluaz.fragment;

import java.util.Date;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;

import net.openwatch.acluaz.R;
import net.openwatch.acluaz.constants.Constants;
import net.openwatch.acluaz.sharedpreferences.SharedPreferencesManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.EditText;

public class IncidentFormFragment extends FormFragment {

	private static final String TAG = "IncidentFormFragment";
	private ViewGroup container;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_incident_form, container,
				false);
		final FragmentManager fm = this.getFragmentManager();
		v.findViewById(R.id.date_input).setOnFocusChangeListener(
				new OnFocusChangeListener() {
					@Override
					public void onFocusChange(View v, boolean hasFocus) {
						if (hasFocus) {
							DialogFragment newFragment = new DatePickerFragment(
									R.id.date_input);
							newFragment.show(fm, "datePicker");
						}
					}
				});
		v.findViewById(R.id.time_input).setOnFocusChangeListener(
				new OnFocusChangeListener() {

					@Override
					public void onFocusChange(View v, boolean hasFocus) {
						if (hasFocus) {
							DialogFragment newFragment = new TimePickerFragment(
									R.id.time_input);
							newFragment.show(fm, "timePicker");
						}
					}

				});
		this.container = (ViewGroup) v;
		return v;
	}

	@Override
	public void onResume() {
		super.onResume();
		Log.i(TAG, "onResume");
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {

	}
	
	private void setDateAndTimeFields(){
		/*
		Date now = new Date();
		((EditText)this.getView().findViewById(R.id.date_input)).setText(Constants.date_formatter.format(now));
		//TESTING
		Log.i(TAG, "pre-populated current date: " + ((EditText)this.getView().findViewById(R.id.date_input)).getText().toString());
		((EditText)this.getView().findViewById(R.id.time_input)).setText(Constants.time_formatter.format(now));
		*/
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		// Pre-populate the form from preferences or database
		// depending on intent
		setDateAndTimeFields();
		Intent i = this.getActivity().getIntent();
		if (i.hasExtra(Constants.INTERNAL_DB_ID)) {
			this.fillFormFromDatabase(
					getFormContainer(),
					i.getExtras().getInt(Constants.INTERNAL_DB_ID));
					presentFormAsExistingReport();
		} else
			new fillFormFromPrefsTask(getFormContainer()).execute(Constants.INCIDENT_PREFS);
	}
	
	private void presentFormAsExistingReport(){
		this.getView().findViewById(R.id.gps_toggle).setVisibility(View.GONE);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
	}
	
	@Override
	public void onPause() {
		Log.i("IncidentFrag", "onPause");
		super.onPause();
	}
	
	@Override
	public ViewGroup getFormContainer(){
		return (ViewGroup)this.getView().findViewById(R.id.incident_form_container);
	}
}