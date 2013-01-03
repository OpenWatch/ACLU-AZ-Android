package net.openwatch.acluaz.fragment;

import net.openwatch.acluaz.R;
import net.openwatch.acluaz.R.id;
import net.openwatch.acluaz.R.layout;
import net.openwatch.acluaz.constants.Constants;
import net.openwatch.acluaz.fragment.FormFragment.fillFormFromPrefsTask;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;

public class IncidentFormFragment extends FormFragment {

	private static final String TAG = "IncidentFormFragment";

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
							DialogFragment newFragment = new DatePickerFragment(R.id.date_input);
							newFragment.show(fm, "datePicker");
						}
					}
				});
		v.findViewById(R.id.time_input).setOnFocusChangeListener(
				new OnFocusChangeListener() {

					@Override
					public void onFocusChange(View v, boolean hasFocus) {
						if (hasFocus) {
							DialogFragment newFragment = new TimePickerFragment(R.id.time_input);
							newFragment.show(fm, "timePicker");
						}
					}

				});
		return v;
	}

	@Override
	public void onResume() {
		super.onResume();
		Log.i(TAG, "onResume");
		
		// Pre-populate the form from preferences or database 
		// depending on intent
		Intent i = this.getActivity().getIntent();
		if(i.hasExtra(Constants.INTERNAL_DB_ID) ){
			this.fillFormFromDatabase((ViewGroup) this.getView().findViewById(R.id.form_container), i.getExtras().getInt(Constants.INTERNAL_DB_ID));
		}
		else
			new fillFormFromPrefsTask((ViewGroup) this.getView().findViewById(R.id.form_container)).execute(Constants.INCIDENT_PREFS);
	}

	@Override
	public void onViewCreated(View view_arg, Bundle savedInstanceState) {

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
	}

	@Override
	public void onPause() {
		super.onPause();
		writeJsonToPrefs(Constants.INCIDENT_PREFS, toJson((ViewGroup) this.getView().findViewById(R.id.form_container), null));

	}
}