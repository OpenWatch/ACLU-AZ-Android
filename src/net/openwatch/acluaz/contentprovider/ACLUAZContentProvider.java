package net.openwatch.acluaz.contentprovider;

import net.openwatch.acluaz.constants.DBConstants;

import com.orm.androrm.DatabaseAdapter;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

public class ACLUAZContentProvider extends ContentProvider {
	
	private static final String TAG = "ACLUAZContentProvider";
	
	private static final String AUTHORITY = "net.openwatch.acluaz.contentprovider.ACLUAZContentProvider";
	
	public static final Uri AUTHORITY_URI = Uri.parse("content://" + AUTHORITY + "/");
	
	private final UriMatcher mUriMatcher;
	
	 private static final int INCIDENTS = 1;
     private static final int INCIDENT_ID = 2;

     
     // Externally accessed uris
     public static final Uri INCIDENT_URI = AUTHORITY_URI.buildUpon().appendPath(net.openwatch.acluaz.constants.DBConstants.INCIDENT_TABLENAME).build();
    
     public static Uri getIncidentUri(int model_id){
    	 return INCIDENT_URI.buildUpon().appendEncodedPath("/" + String.valueOf(model_id)).build();
     }

     public ACLUAZContentProvider(){
		// Create and initialize URI matcher.
	    mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
	    mUriMatcher.addURI(AUTHORITY, DBConstants.INCIDENT_TABLENAME, INCIDENTS);
	    mUriMatcher.addURI(AUTHORITY, DBConstants.INCIDENT_TABLENAME + "/#", INCIDENT_ID);
     }

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getType(Uri uri) {
		// not necessary for internal use
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// not implemented
		return null;
	}
	
	@Override
	public boolean onCreate() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public synchronized Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
		
		int uriType = mUriMatcher.match(uri);
		Cursor result = null;
		//adapter.query("Select")
		String select = "SELECT ";
		if(projection != null){
			for(int x=0; x< projection.length;x++){
				if(x+1 < projection.length)
					select += projection[x] + ", ";
				else
					select += projection[x] + " ";
			}
		}
		String where = "";
		if(selection != null && selectionArgs != null){
			for(int x=0;x<selectionArgs.length;x++){
				if(x + 1 < selectionArgs.length)
					where = selection.replaceFirst("?", selectionArgs[x] + " AND ");
				else
					where = selection.replaceFirst("?", selectionArgs[x]);
			}
		}
		if(where != "")
			where = " WHERE " + where;
		String sortby = " ";
		if(sortOrder != null)
			sortby = sortOrder;
		
		/*
		 * 
		uri	The URI to query. This will be the full URI sent by the client; if the client is requesting a specific record, the URI will end in a record number that the implementation should parse and add to a WHERE or HAVING clause, specifying that _id value.
		projection	The list of columns to put into the cursor. If null all columns are included.
		selection	A selection criteria to apply when filtering rows. If null then all rows are included.
		selectionArgs	You may include ?s in selection, which will be replaced by the values from selectionArgs, in order that they appear in the selection. The values will be bound as Strings.
		sortOrder	How the rows in the cursor should be sorted. If null then the provider is free to define the sort order.
		 */
		DatabaseAdapter adapter = DatabaseAdapter.getInstance(getContext().getApplicationContext());
		Log.i(TAG, adapter.getDatabaseName());
		switch(uriType){
			case INCIDENTS:
				Log.i(TAG, select + " FROM " + DBConstants.INCIDENT_TABLENAME + " " + where + " " + sortby);
				result = adapter.open().query(select + " FROM " + DBConstants.INCIDENT_TABLENAME + " " + where + " ORDER BY " + sortby);
				break;
			case INCIDENT_ID:
				Log.i(TAG, select + " FROM " + DBConstants.INCIDENT_TABLENAME + "WHERE mID="+uri.getLastPathSegment());
				result = adapter.open().query(select + " FROM " + DBConstants.INCIDENT_TABLENAME + " WHERE _id="+uri.getLastPathSegment());
				break;
			
		}
		if(result != null)
			result.setNotificationUri(getContext().getContentResolver(), uri);
		return result;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}


}
