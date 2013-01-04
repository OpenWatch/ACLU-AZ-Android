package net.openwatch.acluaz.adapter;

import net.openwatch.acluaz.R;
import net.openwatch.acluaz.constants.DBConstants;
import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.View;
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
            
        	view_cache.date_col = cursor.getColumnIndexOrThrow(DBConstants.DATE);   
        	view_cache.location_col = cursor.getColumnIndexOrThrow(DBConstants.LOCATION);   
        	view_cache.submitted_col = cursor.getColumnIndexOrThrow(DBConstants.SUBMITTED);
        	view_cache._id_col = cursor.getColumnIndexOrThrow(DBConstants.ID);
            view.setTag(R.id.list_item_cache, view_cache);
        }
        
        view_cache.date.setText(cursor.getString(view_cache.date_col));
        view_cache.location.setText(cursor.getString(view_cache.location_col));
        if(cursor.getInt(view_cache.submitted_col) == 1){
        	view_cache.submitted.setTextColor(c.getResources().getColor(R.color.submitted));
        	view_cache.submitted.setText(R.string.incident_submitted);
        }else{
        	view_cache.submitted.setTextColor(c.getResources().getColor(R.color.not_submitted));
        	view_cache.submitted.setText(R.string.incident_not_submitted);
        }
        //Log.i("OWLocalRecordingAdapter", "got id: " + String.valueOf(cursor.getInt(view_cache._id_col)));
        view.setTag(R.id.list_item_model, cursor.getInt(view_cache._id_col));
	}
	
	// Cache the views within a ListView row item 
    static class ViewCache {
        TextView date;
        TextView location;
        TextView submitted;
        
        int date_col; 
        int location_col;
        int submitted_col;
        int _id_col;
    }

}
