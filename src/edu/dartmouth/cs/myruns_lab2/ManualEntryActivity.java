/**
 * CS 65 14S
 * Professor: Andrew Campbell
 * 
 * @author Paul Champeau
 * Title: MyRuns_Lab2
 */package edu.dartmouth.cs.myruns_lab2;

import java.util.Calendar;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.app.DialogFragment;
import android.app.ListActivity;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

public class ManualEntryActivity extends ListActivity {
	private String item;
	TextView mDisplayDateTime;
	Calendar mDateAndTime = Calendar.getInstance();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_manual_entry);

		// Define the listener interface
		OnItemClickListener mListener = new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				//When clicked, display the corresponding dialog
				item = (String) ((TextView) view).getText();
				
				if (item.equals("Date")) onDateClicked();
				
				else if(item.equals("Time")) onTimeClicked();
				
				else if(item.equals("Duration")){
					dialog(R.string.Duration, 2);
				}
				else if(item.equals("Distance")){
					dialog(R.string.Distance, 2);
				}
				else if(item.equals("Calories")){
					dialog(R.string.Calories, 2);
				}
				else if(item.equals("Heartrate")){
					dialog(R.string.Heartrate, 2);
				}
				else if(item.equals("Comment")){
					dialog(R.string.Comment, 1);
				}
			}
			};

		// Get the ListView and wired the listener
		ListView listView = getListView();
		listView.setOnItemClickListener(mListener);

	}
	
	
	public void onSaveClicked(View v){
		Intent intent = new Intent(ManualEntryActivity.this,
				MainActivity.class);
		startActivity(intent);
	}
	
	public void onCancelClicked(View v){
		Intent intent = new Intent(ManualEntryActivity.this,
				MainActivity.class);
		startActivity(intent);
	}
	//Create the dialog for a date picker
	public void onDateClicked() {

		DatePickerDialog.OnDateSetListener mDateListener = new DatePickerDialog.OnDateSetListener() {
			public void onDateSet(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {
				mDateAndTime.set(Calendar.YEAR, year);
				mDateAndTime.set(Calendar.MONTH, monthOfYear);
				mDateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
			}
		};

		new DatePickerDialog(ManualEntryActivity.this, mDateListener,
				mDateAndTime.get(Calendar.YEAR),
				mDateAndTime.get(Calendar.MONTH),
				mDateAndTime.get(Calendar.DAY_OF_MONTH)).show();

	}
	//Create the dialog for a time picker
	public void onTimeClicked() {

		TimePickerDialog.OnTimeSetListener mTimeListener = new TimePickerDialog.OnTimeSetListener() {
			public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
				mDateAndTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
				mDateAndTime.set(Calendar.MINUTE, minute);
			}
		};

		new TimePickerDialog(ManualEntryActivity.this, mTimeListener,
				mDateAndTime.get(Calendar.HOUR_OF_DAY),
				mDateAndTime.get(Calendar.MINUTE), true).show();

	}
	//Create a general purpose dialog for all other list items
public void dialog (int name, int input_type){
	
	
	final EditText input = new EditText(this);
	if(name == R.string.Comment){
		input.setHint(R.string.comment_hint);
	}
	input.setInputType(input_type);
	AlertDialog dialog = new AlertDialog.Builder(ManualEntryActivity.this)
    .setTitle(name)
    .setView(input)
    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int whichButton) {
           //do nothing 
        }
    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int whichButton) {
            // Do nothing.
        }
    }).show();
}

		
}		



