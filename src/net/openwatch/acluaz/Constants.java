package net.openwatch.acluaz;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

public class Constants {

	public static final String PERSONAL_PREFS = "PERSONAL_PREFS";

	public static final String[] US_STATES = { "Alabama", "Alaska", "Arizona",
			"Arkansas", "California", "Colorado", "Connecticut", "Delaware",
			"District of Columbia", "Florida", "Georgia", "Hawaii", "Idaho",
			"Illinois", "Indiana", "Iowa", "Kansas", "Kentucky", "Louisiana",
			"Maine", "Maryland", "Massachusetts", "Michigan", "Minnesota",
			"Mississippi", "Missouri", "Montana", "Nebraska", "Nevada",
			"New Hampshire", "New Jersey", "New Mexico", "New York",
			"North Carolina", "North Dakota", "Ohio", "Oklahoma", "Oregon",
			"Pennsylvania", "Rhode Island", "South Carolina", "South Dakota",
			"Tennessee", "Texas", "Utah", "Vermont", "Virginia", "Washington",
			"West Virginia", "Wisconsin", "Wyoming" };
	
	// Machine-readable:
	public static SimpleDateFormat datetime_formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
	// Human facing:
	public static SimpleDateFormat time_formatter = new SimpleDateFormat("kk:mm a", Locale.US);
	public static SimpleDateFormat date_formatter = new SimpleDateFormat("MMM dd, yyyy", Locale.US);

	static{
		datetime_formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
	}
}
