package net.openwatch.acluaz;

import net.openwatch.acluaz.fragment.DatePickerFragment;
import net.openwatch.acluaz.fragment.FormFragment;
import net.openwatch.acluaz.fragment.TimePickerFragment;

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
		// populateViews(recording, getActivity().getApplicationContext());
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
		toJson(R.id.form_container);

	}
}