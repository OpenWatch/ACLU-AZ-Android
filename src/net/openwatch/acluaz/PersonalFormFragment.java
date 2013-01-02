package net.openwatch.acluaz;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

public class PersonalFormFragment extends Fragment {

	private static final String TAG = "LocalRecordingInfoFragment";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.fragment_personal_form,
				container, false);
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_dropdown_item_1line, Constants.US_STATES);
		AutoCompleteTextView actv = ((AutoCompleteTextView) v.findViewById(R.id.state_input));
		actv.setThreshold(1);
		actv.setAdapter(adapter);
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

	}
}