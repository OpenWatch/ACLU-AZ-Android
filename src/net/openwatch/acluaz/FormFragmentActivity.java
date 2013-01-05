package net.openwatch.acluaz;

/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

import net.openwatch.acluaz.constants.Constants;
import net.openwatch.acluaz.fragment.FormFragment;
import net.openwatch.acluaz.fragment.IncidentFormFragment;
import net.openwatch.acluaz.fragment.PersonalFormFragment;
import net.openwatch.acluaz.http.OWServiceRequests;
import net.openwatch.acluaz.location.DeviceLocation;
import net.openwatch.acluaz.location.DeviceLocation.LocationResult;
import net.openwatch.acluaz.sharedpreferences.SharedPreferencesManager;

/**
 * Demonstrates combining a TabHost with a ViewPager to implement a tab UI
 * that switches between tabs and also allows the user to perform horizontal
 * flicks to move between the tabs.
 */
public class FormFragmentActivity extends SherlockFragmentActivity {
	private static final String TAG = "FormFragmentActivity";
    TabHost mTabHost;
    ViewPager  mViewPager;
    TabsAdapter mTabsAdapter;
    
    LayoutInflater inflater;
    
    public static int display_width = -1;
    
    private boolean did_submit = false; // if this is false, save incident data to prefs
        
    private ArrayList<FormFragment> attached_fragments = new ArrayList<FormFragment>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_tabs_pager);
		// Show the Up button in the action bar.
		this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);		
        getDisplayWidth();
        mTabHost = (TabHost)findViewById(android.R.id.tabhost);
        mTabHost.setup();
        mViewPager = (ViewPager)findViewById(R.id.pager);
        mTabsAdapter = new TabsAdapter(this, mTabHost, mViewPager);
        
        inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mTabsAdapter.addTab(mTabHost.newTabSpec(getString(R.string.personal_tab)).setIndicator(inflateCustomTab(getString(R.string.personal_tab))), PersonalFormFragment.class, null);
        mTabsAdapter.addTab(mTabHost.newTabSpec(getString(R.string.incident_tab)).setIndicator(inflateCustomTab(getString(R.string.incident_tab))), IncidentFormFragment.class, null);
        
        /*
        mTabsAdapter.addTab(mTabHost.newTabSpec(getString(R.string.personal_tab)).setIndicator(inflateCustomTab(getString(R.string.personal_tab))),
                PersonalFormFragment.class, null);
        
        mTabsAdapter.addTab(mTabHost.newTabSpec(getString(R.string.event_tab)).setIndicator(inflateCustomTab(getString(R.string.event_tab))),
                RemoteFeedFragmentActivity.RemoteRecordingsListFragment.class, null);
        */
        if (savedInstanceState != null) {
            mTabHost.setCurrentTabByTag(savedInstanceState.getString("tab"));
        }
        
        DeviceLocation location = new DeviceLocation();
        location.getLocation(getApplicationContext(), new LocationResult(){

			@Override
			public void gotLocation(Location location) {
				if(FormFragmentActivity.this != null && FormFragmentActivity.this.findViewById(R.id.gps_toggle) != null){
					FormFragmentActivity.this.findViewById(R.id.gps_toggle).setTag(R.id.view_tag, location);
					//Log.i(TAG, "Tagged location_input with : " + FormFragmentActivity.this.findViewById(R.id.gps_toggle).getTag(R.id.view_tag).toString());
				}
			}
        	
        }, true);
    }
    
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getSupportMenuInflater().inflate(R.menu.activity_form, menu);
		return true;
	}
    
    @Override
    public boolean onPrepareOptionsMenu (Menu menu){
    	
		if (this.getIntent().hasExtra(Constants.INTERNAL_DB_ID)) {
	    	//menu.removeItem(R.id.menu_clear_personal);
			if(menu.findItem(R.id.menu_submit_form) != null)
				menu.findItem(R.id.menu_submit_form).setTitle(R.string.menu_amend_form);
		}
		return true;
    }
    
    
    
    
    @Override
    public boolean onOptionsItemSelected (MenuItem item){
    	switch(item.getItemId()){
    	/*
	    	case R.id.menu_clear_personal:
	    		AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    		builder.setTitle(getString(R.string.clear_personal_info_dialog_title))
	    		.setMessage(getString(R.string.clear_personal_info_dialog_msg))
	    		.setPositiveButton(getString(R.string.clear_personal_info_ok_btn), new OnClickListener(){

					@Override
					public void onClick(DialogInterface dialog, int which) {
						SharedPreferencesManager.clearPrefsAndForm(FormFragmentActivity.this, getPersonalFormFragment(), Constants.PERSONAL_PREFS);
						dialog.dismiss();
					}
	    			
	    		}).setNegativeButton(getString(R.string.clear_personal_info_cancel_btn), new OnClickListener(){

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
	    			
	    		}).show();
	    		
	    		break;
	    */
	    	case R.id.menu_submit_form:
	    		if(!validateFormFragments()){
	    			showFormIncompleteDialog();
	    			break;
	    		}
	    			
	    		JSONObject json = new JSONObject();
	    		JSONObject user_json = new JSONObject();
	    		JSONObject report_json = new JSONObject();
	    		attached_fragments.get(0).toJson((ViewGroup) this.findViewById(R.id.personal_form_container), user_json);
	    		attached_fragments.get(1).toJson((ViewGroup) this.findViewById(R.id.incident_form_container), report_json);
				try {
					json.put(getString(R.string.user_tag), user_json);
					json.put(getString(R.string.report_tag), report_json);
					Log.i(TAG, "pre json to Database: " + json.toString());
		    		if (this.getIntent().hasExtra(Constants.INTERNAL_DB_ID)){
		    			FormFragment.updateIncidentInDatabase(getApplicationContext(), json, this.getIntent().getExtras().getInt(Constants.INTERNAL_DB_ID));
		    			json = FormFragment.addUuidToJson(getApplicationContext(), json, this.getIntent().getExtras().getInt(Constants.INTERNAL_DB_ID));
		    			OWServiceRequests.postReport(getApplicationContext(), json, this.getIntent().getExtras().getInt(Constants.INTERNAL_DB_ID));
		    		}else{
		    			int db_id = FormFragment.insertIncidentInDatabase(getApplicationContext(), json);
		    			json = FormFragment.addUuidToJson(getApplicationContext(), json, db_id);
		    			OWServiceRequests.postReport(getApplicationContext(), json, db_id);
		    		}
		    		did_submit = true;
		    		Intent i = new Intent(this, MainActivity.class);
		    		startActivity(i);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    		break;
	    	case android.R.id.home:
    			NavUtils.navigateUpFromSameTask(this);
    			break;
    	}
    	return true;
    }
    
    private boolean validateFormFragments(){
    	for(int x=0; x<attached_fragments.size(); x++){
    		if(!attached_fragments.get(x).validateForm(attached_fragments.get(x).getFormContainer()) )
    			return false;
    	}
    	return true;
    }
    
    private FormFragment getPersonalFormFragment(){
    	//TODO: Do this better
    	if(attached_fragments.size() == 2)
    		return attached_fragments.get(0);
    	return null;
    }
    
    private FormFragment getIncidentFormFragment(){
    	//TODO: Do this better
    	if(attached_fragments.size() == 2)
    		return attached_fragments.get(1);
    	return null;
    }
    
    public void onAttachFragment (Fragment fragment){
    	if(FormFragment.class.isInstance(fragment))
    		attached_fragments.add((FormFragment)fragment);
    }
    
    private void showFormIncompleteDialog(){
    	
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(getString(R.string.form_incomplete_dialog_title))
		.setMessage(getString(R.string.form_incomplete_dialog_msg))
		.setPositiveButton(getString(R.string.dialog_ok), new OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
			
		}).show();
    	
    }
    
    @Override
    public void onPause(){
    	Log.i(TAG, "onPause");
    	if(!did_submit){
    		// save incident prefs
    		FormFragment incidentFrag = this.getIncidentFormFragment();
    		incidentFrag.writeJsonToPrefs(Constants.INCIDENT_PREFS, incidentFrag.toJson((ViewGroup) incidentFrag.getView().findViewById(R.id.incident_form_container), null));
    	}else{ // if report was submitted, clear incident prefs
    		SharedPreferencesManager.clearPrefs(getApplicationContext(), Constants.INCIDENT_PREFS);
    	}
    	super.onPause();
    
    }
    
    private View inflateCustomTab(String tab_title){
    	LinearLayout tab = (LinearLayout) inflater.inflate(R.layout.tab_indicator, (ViewGroup) this.findViewById(android.R.id.tabs), false);
		((TextView)tab.findViewById(R.id.title)).setText(tab_title);
		return tab;
	}

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("tab", mTabHost.getCurrentTabTag());
    }

    /**
     * This is a helper class that implements the management of tabs and all
     * details of connecting a ViewPager with associated TabHost.  It relies on a
     * trick.  Normally a tab host has a simple API for supplying a View or
     * Intent that each tab will show.  This is not sufficient for switching
     * between pages.  So instead we make the content part of the tab host
     * 0dp high (it is not shown) and the TabsAdapter supplies its own dummy
     * view to show as the tab content.  It listens to changes in tabs, and takes
     * care of switch to the correct paged in the ViewPager whenever the selected
     * tab changes.
     */
    public static class TabsAdapter extends FragmentPagerAdapter
            implements TabHost.OnTabChangeListener, ViewPager.OnPageChangeListener {
        private final Context mContext;
        private final TabHost mTabHost;
        private final ViewPager mViewPager;
        private final ArrayList<TabInfo> mTabs = new ArrayList<TabInfo>();

        static final class TabInfo {
            private final String tag;
            private final Class<?> clss;
            private final Bundle args;

            TabInfo(String _tag, Class<?> _class, Bundle _args) {
                tag = _tag;
                clss = _class;
                args = _args;
            }
        }

        static class DummyTabFactory implements TabHost.TabContentFactory {
            private final Context mContext;

            public DummyTabFactory(Context context) {
                mContext = context;
            }

            @Override
            public View createTabContent(String tag) {
                View v = new View(mContext);
                v.setMinimumWidth(0);
                v.setMinimumHeight(0);
                return v;
            }
        }

        public TabsAdapter(FragmentActivity activity, TabHost tabHost, ViewPager pager) {
            super(activity.getSupportFragmentManager());
            mContext = activity;
            mTabHost = tabHost;
            mViewPager = pager;
            mTabHost.setOnTabChangedListener(this);
            mViewPager.setAdapter(this);
            mViewPager.setOnPageChangeListener(this);
        }

        public void addTab(TabHost.TabSpec tabSpec, Class<?> clss, Bundle args) {
        	tabSpec.setContent(new DummyTabFactory(mContext));
            String tag = tabSpec.getTag();

            TabInfo info = new TabInfo(tag, clss, args);
            mTabs.add(info);
            mTabHost.addTab(tabSpec);
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mTabs.size();
        }

        @Override
        public Fragment getItem(int position) {
            TabInfo info = mTabs.get(position);
            return Fragment.instantiate(mContext, info.clss.getName(), info.args);
        }

        @Override
        public void onTabChanged(String tabId) {
            int position = mTabHost.getCurrentTab();
            mViewPager.setCurrentItem(position);
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            // Unfortunately when TabHost changes the current tab, it kindly
            // also takes care of putting focus on it when not in touch mode.
            // The jerk.
            // This hack tries to prevent this from pulling focus out of our
            // ViewPager.
            TabWidget widget = mTabHost.getTabWidget();
            int oldFocusability = widget.getDescendantFocusability();
            widget.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
            mTabHost.setCurrentTab(position);
            widget.setDescendantFocusability(oldFocusability);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    }
    
    /**
     * Measure display width so the view pager can implement its 
     * custom behavior re: paging on the map view
     */
    private void getDisplayWidth(){
    	Display display = getWindowManager().getDefaultDisplay();
        display_width = display.getWidth();
    }
}
