package net.openwatch.acluaz.view;

import net.openwatch.acluaz.R;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;

public class ValidatedEditText extends EditText {
	private static final String TAG = "ValidatedEditText";
	
	public class preventBlankListener implements OnFocusChangeListener{
		private boolean hadFocus = true;

		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			Log.i(TAG, "hadFocus: " + String.valueOf(hadFocus) + " hasFocus: " + String.valueOf(hasFocus));
			if (hadFocus && !hasFocus) {
				if(((EditText) v).getText().toString().trim().length() == 0){
					Drawable error_icon = getResources().getDrawable(R.drawable.indicator_input_error);
					error_icon.setBounds(new Rect(0, 0, error_icon.getIntrinsicWidth(), error_icon.getIntrinsicHeight()));
					((EditText) v).setError(v.getResources()
							.getString(R.string.required_field_error), error_icon);
				}
			}
			hadFocus = hasFocus;
		}
	};

	public ValidatedEditText(Context context) {
		super(context);
		this.setOnFocusChangeListener(new preventBlankListener());
	}

	public ValidatedEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.setOnFocusChangeListener(new preventBlankListener());
	}

	public ValidatedEditText(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		this.setOnFocusChangeListener(new preventBlankListener());
	}

}
