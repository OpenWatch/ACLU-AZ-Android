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
package net.openwatch.acluaz.view;

import net.openwatch.acluaz.R;
import android.content.Context;
import android.graphics.Color;
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
		// TODO: get icon to display
		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			if (!hasFocus) {
				if(((EditText) v).getText().toString().trim().length() == 0){
					//Drawable error_icon = getResources().getDrawable(R.drawable.indicator_input_error);
					//error_icon.setBounds(new Rect(0, 0, error_icon.getIntrinsicWidth(), error_icon.getIntrinsicHeight()));
					((EditText) v).setError(v.getResources()
							.getString(R.string.required_field_error));
					
					//v.setBackgroundColor(getResources().getColor(R.color.not_submitted));
					//v.setBackgroundColor(Color.RED);
				}else{
					((EditText) v).setError(null);
					//v.setBackgroundColor(getResources().getColor(android.R.color.transparent));
					
				}
			}
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
