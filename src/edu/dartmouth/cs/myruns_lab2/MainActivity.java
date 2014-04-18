/**
 * CS 65 14S
 * Professor: Andrew Campbell
 * 
 * @author Paul Champeau
 * Title: MyRuns_Lab2
 */
package edu.dartmouth.cs.myruns_lab2;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends Activity {

	private static final String TAB_KEY_INDEX = "tab_key";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// ActionBar
		ActionBar actionbar = getActionBar();
		actionbar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// create new tabs and and set up the titles of the tabs
		ActionBar.Tab mStartTab = actionbar.newTab().setText(
				getString(R.string.ui_tabname_start));
		ActionBar.Tab mHistoryTab = actionbar.newTab().setText(
				getString(R.string.ui_tabname_history));
		ActionBar.Tab mSettingsTab = actionbar.newTab().setText(
				getString(R.string.ui_tabname_settings));

		// create the fragments
		Fragment mStartFragment = new StartFragment();
		Fragment mSettingsFragment = new SettingsFragment();
		Fragment mHistoryFragment = new HistoryFragment();



		// bind the fragments to the tabs - set up tabListeners for each tab

		mStartTab.setTabListener(new MyTabsListener(mStartFragment,
				getApplicationContext()));
		mSettingsTab.setTabListener(new MyTabsListener(mSettingsFragment,
				getApplicationContext()));
		mHistoryTab.setTabListener(new MyTabsListener(mHistoryFragment,
				getApplicationContext()));


		// add the tabs to the action bar
		actionbar.addTab(mStartTab);
		actionbar.addTab(mHistoryTab);
		actionbar.addTab(mSettingsTab);


		// Crash the program -- example of debugging

		// Toast.makeText(getApplicationContext(),
		// "tab is " + savedInstanceState.getInt(TAB_KEY_INDEX, 0),
		// Toast.LENGTH_SHORT).show();

		// restore to navigation
		if (savedInstanceState != null) {
			Toast.makeText(getApplicationContext(),
					"tab is " + savedInstanceState.getInt(TAB_KEY_INDEX, 0),
					Toast.LENGTH_SHORT).show();

			actionbar.setSelectedNavigationItem(savedInstanceState.getInt(
					TAB_KEY_INDEX, 0));
		}

	}

	// onSaveInstanceState() is used to "remember" the current state when a
	// configuration change occurs such screen orientation change. This
	// is not meant for "long term persistence". We store the tab navigation

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		Toast.makeText(
				this,
				"onSaveInstanceState: tab is"
						+ getActionBar().getSelectedNavigationIndex(),
						Toast.LENGTH_SHORT).show();
		outState.putInt(TAB_KEY_INDEX, getActionBar()
				.getSelectedNavigationIndex());

	}

	public void OnStartClicked(View v){
		Spinner input_type = (Spinner)findViewById(R.id.spinnerInputType);
		String text = input_type.getSelectedItem().toString();

		if(text.equals("Manual Entry")){
			Intent intent = new Intent(MainActivity.this,
					ManualEntryActivity.class);
			startActivity(intent);
		}

		else{
			Intent blank_intent = new Intent(MainActivity.this,
					BlankActivity.class);
			startActivity(blank_intent);
		}

	}

public void OnSyncClicked(View v){
	//Do nothing for now
}

}

// TabListenr class for managing user interaction with the ActionBar tabs. The
// application context is passed in pass it in constructor, needed for the
// toast.

class MyTabsListener implements ActionBar.TabListener {
	public Fragment fragment;
	public Context context;

	public MyTabsListener(Fragment fragment, Context context) {
		this.fragment = fragment;
		this.context = context;

	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		Toast.makeText(context, "Reselected!", Toast.LENGTH_SHORT).show();

	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		Toast.makeText(context, "Selected!", Toast.LENGTH_SHORT).show();
		ft.replace(R.id.fragment_container, fragment);
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		Toast.makeText(context, "Unselected!", Toast.LENGTH_SHORT).show();
		ft.remove(fragment);
	}

}