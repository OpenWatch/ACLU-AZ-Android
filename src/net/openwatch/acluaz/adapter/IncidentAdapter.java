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
package net.openwatch.acluaz.adapter;

import net.openwatch.acluaz.R;
import net.openwatch.acluaz.constants.DBConstants;
import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class IncidentAdapter extends SimpleCursorAdapter {
	private Context c;

	public IncidentAdapter(Context context, Cursor c) {
		super(context, R.layout.incident_list_item, c, new String[]{}, new int[]{},0);
		this.c = context;
	}
	
	@Override
	public void bindView(View view, Context context, Cursor cursor){
		super.bindView(view, context, cursor);
		
		ViewCache view_cache = (ViewCache) view.getTag(R.id.list_item_cache);
        if (view_cache == null) {
        	view_cache = new ViewCache();
        	view_cache.date = (TextView) view.findViewById(R.id.date);
        	view_cache.location = (TextView) view.findViewById(R.id.location);
        	view_cache.submitted = (TextView) view.findViewById(R.id.submitted);
        	view_cache.container = (ViewGroup) view.findViewById(R.id.container);
            
        	view_cache.date_col = cursor.getColumnIndexOrThrow(DBConstants.DATE);   
        	view_cache.location_col = cursor.getColumnIndexOrThrow(DBConstants.LOCATION);   
        	view_cache.submitted_col = cursor.getColumnIndexOrThrow(DBConstants.SUBMITTED);
        	view_cache._id_col = cursor.getColumnIndexOrThrow(DBConstants.ID);
            view.setTag(R.id.list_item_cache, view_cache);
        }
        
        view_cache.date.setText(cursor.getString(view_cache.date_col));
        if(cursor.getString(view_cache.location_col).compareTo("") != 0){
        	view_cache.location.setText(cursor.getString(view_cache.location_col));
        	view_cache.location.setVisibility(View.VISIBLE);
        } else{
        	view_cache.location.setVisibility(View.GONE);
        }
        if(cursor.getInt(view_cache.submitted_col) == 1){
        	view_cache.submitted.setTextColor(c.getResources().getColor(R.color.submitted));
        	view_cache.submitted.setText(R.string.incident_submitted);
        }else{
        	view_cache.submitted.setTextColor(c.getResources().getColor(R.color.not_submitted));
        	view_cache.submitted.setText(R.string.incident_not_submitted);
        }
        //Log.i("OWLocalRecordingAdapter", "got id: " + String.valueOf(cursor.getInt(view_cache._id_col)));
        view.setTag(R.id.list_item_model, cursor.getInt(view_cache._id_col));
        
        if (cursor.getPosition() % 2 == 0){
            view_cache.container.setBackgroundResource(R.drawable.list_bg_1);
        } else {
            view_cache.container.setBackgroundResource(R.drawable.list_bg_2);
        }
	}
	
	// Cache the views within a ListView row item 
    static class ViewCache {
        ViewGroup container;
    	TextView date;
        TextView location;
        TextView submitted;
        
        int date_col; 
        int location_col;
        int submitted_col;
        int _id_col;
    }

}
