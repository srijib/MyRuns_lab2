/**
 * CS 65 14S
 * Professor: Andrew Campbell
 * 
 * @author Paul Champeau
 * Title: MyRuns_Lab2
 */
package edu.dartmouth.cs.myruns_lab2;
//This is a blank activity used to emulate the screen that
//will be displayed once we complete the automatic and
//GPS activity input methods
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

public class BlankActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_blank);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.blank, menu);
		return true;
	}
	
	public void onSaveClicked(View v){
		Intent intent = new Intent(BlankActivity.this,
				MainActivity.class);
		startActivity(intent);
	}
	
	public void onCancelClicked(View v){
		Intent intent = new Intent(BlankActivity.this,
				MainActivity.class);
		startActivity(intent);
	}

}
