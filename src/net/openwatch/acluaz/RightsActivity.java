package net.openwatch.acluaz;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.text.Html;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.TextSwitcher;
import android.widget.ViewSwitcher;

public class RightsActivity extends SherlockActivity implements ViewSwitcher.ViewFactory {
	private static final String TAG = "RightsActivity";

	TextSwitcher rights_content;
	
	enum STATE { NONE, GENERAL, CAR, STREET, HOME, JAIL };
	
	STATE state = STATE.NONE;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rights);
		// Show the Up button in the action bar.
		this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		rights_content = (TextSwitcher) findViewById(R.id.rights_content);
		rights_content.setFactory(this);
		rights_content.setInAnimation(AnimationUtils.loadAnimation(this,
                android.R.anim.slide_in_left));
		rights_content.setOutAnimation(AnimationUtils.loadAnimation(this,
                android.R.anim.slide_out_right));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getSupportMenuInflater().inflate(R.menu.activity_rights, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void generalClicked(View v) {
		if(state != STATE.GENERAL){
			rights_content.setText(Html.fromHtml(getString(R.string.general_rights)));
			state = STATE.GENERAL;
		}
	}

	public void carClicked(View v) {
		if(state != STATE.CAR){
			rights_content.setText(Html.fromHtml(getString(R.string.car_rights)));
			state = STATE.CAR;
		}
	}

	public void streetClicked(View v) {
		if(state != STATE.STREET){
			rights_content.setText(Html.fromHtml(getString(R.string.street_rights)));
			state = STATE.STREET;
		}
	}

	public void homeClicked(View v) {
		if(state != STATE.HOME){
			rights_content.setText(Html.fromHtml(getString(R.string.home_rights)));
			state = STATE.HOME;
		}
	}

	public void jailClicked(View v) {
		if(state != STATE.JAIL){
			rights_content.setText(Html.fromHtml(getString(R.string.jail_rights)));
			state = STATE.JAIL;
		}
	}

	@Override
	public View makeView() {
		return View.inflate(this, R.layout.rights_content_textview, null);
	}

}
