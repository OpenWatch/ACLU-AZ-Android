package net.openwatch.acluaz.fragment;

import java.util.Calendar;
import java.util.GregorianCalendar;

import net.openwatch.acluaz.constants.Constants;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.TextView;
import android.widget.TimePicker;

public class TimePickerFragment extends DialogFragment implements
		TimePickerDialog.OnTimeSetListener {

	private int text_field_id = -1;


	public TimePickerFragment(int text_field_id) {
		this.text_field_id = text_field_id;
	}
	

	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setRetainInstance(true);
	 }

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Use the current time as the default values for the picker
		final Calendar c = Calendar.getInstance();
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minute = c.get(Calendar.MINUTE);

		// Create a new instance of TimePickerDialog and return it
		return new TimePickerDialog(getActivity(), this, hour, minute,
				DateFormat.is24HourFormat(getActivity()));
	}

	public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
		// Do something with the time chosen by the user
		if (text_field_id == -1)
			return;
		Calendar c = Calendar.getInstance();
		GregorianCalendar gc = new GregorianCalendar(c.get(c.YEAR),
				c.get(c.MONTH), c.get(c.DAY_OF_MONTH), hourOfDay, minute);

		if (this.getActivity().findViewById(text_field_id) != null)
			((TextView) this.getActivity().findViewById(text_field_id))
					.setText(Constants.time_formatter.format(gc.getTime()));

	}

	// By default, the app will crash if the
	// hosting activity is destroyed while the dialogfragment is displayed i.e:
	// during screen reotation
	@Override
	public void onDestroyView() {
		if (getDialog() != null && getRetainInstance())
			getDialog().setOnDismissListener(null);
		super.onDestroyView();
	}
}