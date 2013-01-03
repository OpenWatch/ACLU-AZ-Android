package net.openwatch.acluaz.fragment;

import net.openwatch.acluaz.Constants;
import net.openwatch.acluaz.R;
import net.openwatch.acluaz.R.id;
import net.openwatch.acluaz.R.layout;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

public class PersonalFormFragment extends FormFragment {

	private static final String TAG = "PersonalFormFragment";

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
		new fillFormFromPrefsTask((ViewGroup) (ViewGroup) this.getView().findViewById(R.id.form_container)).execute(Constants.PERSONAL_PREFS);
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
		
		writeJsonToPrefs(Constants.PERSONAL_PREFS, toJson((ViewGroup) this.getView().findViewById(R.id.form_container)));
	}
}