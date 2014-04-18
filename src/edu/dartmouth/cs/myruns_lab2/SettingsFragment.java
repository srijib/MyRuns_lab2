/**
 * CS 65 14S
 * Professor: Andrew Campbell
 * 
 * @author Paul Champeau
 * Title: MyRuns_Lab2
 */
package edu.dartmouth.cs.myruns_lab2;

import android.preference.PreferenceFragment;
import android.os.Bundle;


public class SettingsFragment extends PreferenceFragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Load the preferences from an XML resource
		addPreferencesFromResource(R.layout.fragment_settings);
	}

}
