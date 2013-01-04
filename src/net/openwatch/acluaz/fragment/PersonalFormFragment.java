package net.openwatch.acluaz.fragment;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;

import net.openwatch.acluaz.R;
import net.openwatch.acluaz.R.id;
import net.openwatch.acluaz.R.layout;
import net.openwatch.acluaz.constants.Constants;
import net.openwatch.acluaz.fragment.FormFragment.fillFormFromPrefsTask;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
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
                R.layout.autocomplete_item, Constants.US_STATES);
		AutoCompleteTextView actv = ((AutoCompleteTextView) v.findViewById(R.id.state_input));
		actv.setThreshold(1);
		actv.setAdapter(adapter);
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
			this.fillFormFromDatabase((ViewGroup) this.getView().findViewById(R.id.personal_form_container), i.getExtras().getInt(Constants.INTERNAL_DB_ID));
		}
		else
			new fillFormFromPrefsTask((ViewGroup) this.getView().findViewById(R.id.personal_form_container)).execute(Constants.PERSONAL_PREFS);
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
		Log.i("PersonalFrag", "onPause");
		writeJsonToPrefs(Constants.PERSONAL_PREFS, toJson((ViewGroup) this.getView().findViewById(R.id.personal_form_container), null));
		super.onPause();
	}
}