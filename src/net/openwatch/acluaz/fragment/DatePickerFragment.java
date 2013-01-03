package net.openwatch.acluaz.fragment;

import java.util.Calendar;
import java.util.GregorianCalendar;

import net.openwatch.acluaz.constants.Constants;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;
import android.widget.TextView;

public class DatePickerFragment extends DialogFragment implements
		DatePickerDialog.OnDateSetListener {
	
	private int text_field_id = -1;
	
	public DatePickerFragment(int text_field_id){
		this.text_field_id = text_field_id;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Use the current date as the default date in the picker
		final Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);

		// Create a new instance of DatePickerDialog and return it
		return new DatePickerDialog(getActivity(), this, year, month, day);
	}

	public void onDateSet(DatePicker view, int year, int month, int day) {
		if(text_field_id == -1)
			return;
		
		Calendar c = Calendar.getInstance();
		GregorianCalendar gc = new GregorianCalendar(year, month,day);
		((TextView) this.getActivity().findViewById(text_field_id)).setText(Constants.date_formatter.format(gc.getTime()));
	}
}
