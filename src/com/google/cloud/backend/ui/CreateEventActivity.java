package com.google.cloud.backend.ui;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.cloud.backend.R;
import com.google.cloud.backend.core.CloudBackend;
import com.google.cloud.backend.core.CloudBackendMessaging;
import com.google.cloud.backend.core.CloudCallbackHandler;
import com.google.cloud.backend.core.CloudEntity;

/**
 * 
 *Activity to create an event
 */
public class CreateEventActivity extends Activity implements OnClickListener,OnItemSelectedListener{

	//Variable declaration
	private EditText _nameEditText;
	private EditText _detailsEditText;
	private EditText _placeEditText;

	private Button _datePicker;
	private Button _timePicker;

	private Button _createButton;
	private Button _cancelButton;
	private Spinner spinner;

	private int day;
	private int month;
	private int year;
	private int hour;
	private int min;

	private ProgressDialog progressDialog;

	String items[] = new String[]{"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20"};
	protected String timeString;
	protected String dateString;
	protected String noOfFriends="1";
	private CloudBackend cloudBackend;
	private List<CloudEntity> mPosts;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.createevents);


		//Initializing UI components
		_nameEditText = (EditText) findViewById(R.id.nameEditText);
		_detailsEditText = (EditText) findViewById(R.id.detailsEditText);
		_placeEditText = (EditText) findViewById(R.id.placeEditText);
		_datePicker = (Button) findViewById(R.id.datePickrer);
		_datePicker.setOnClickListener(this);
		_timePicker = (Button) findViewById(R.id.timePicker);
		_timePicker.setOnClickListener(this);

		//Setting date format
		SimpleDateFormat sdf = new SimpleDateFormat("M/dd/yyyy");

		//Current date as Default date and time 7:00
		dateString = sdf.format(new Date());
		_datePicker.setText(dateString);

		timeString = "07:00";
		_timePicker.setText(timeString);

		_createButton = (Button) findViewById(R.id.createButton);
		_createButton.setOnClickListener(this);
		_cancelButton = (Button) findViewById(R.id.cancelButton);
		_cancelButton.setOnClickListener(this);

		//Initializing spinner values
		spinner = (Spinner) findViewById(R.id.spinner1);
		//ArrayAdaptor to create spinner with values
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(CreateEventActivity.this, android.R.layout.simple_spinner_item ,items);
		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(this);
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		//On click create button
		case R.id.createButton:

			//Showing progress dialog
			progressDialog = ProgressDialog.show(CreateEventActivity.this,"","Please wait..");
			progressDialog.setCancelable(true);

			//Getting values from GUI components
			String name = _nameEditText.getText().toString();
			String details = _detailsEditText.getText().toString();
			String place = _placeEditText.getText().toString();
			String date = dateString;
			String time = timeString;
			String noOfFrnds = noOfFriends;

			//Checks for null or zero values
			if(name.length()==0){
				ShowAlertDialog("", "Please enter name of the event", CreateEventActivity.this, false);
			}else if(details.length()==0){
				ShowAlertDialog("", "Please enter details of the event", CreateEventActivity.this, false);
			}if(place.length()==0){
				ShowAlertDialog("", "Please enter place of the event", CreateEventActivity.this, false);
			}if(date.length()==0){
				ShowAlertDialog("", "Please enter date of the event", CreateEventActivity.this, false);
			}if(time.length()==0){
				ShowAlertDialog("", "Please enter time of the event", CreateEventActivity.this, false);
			}if(noOfFrnds.length()==0){
				ShowAlertDialog("", "Please enter number of friends for the event", CreateEventActivity.this, false);
			}else{

				//Initializing CloudBackend class object to perform backend action
				cloudBackend = new CloudBackend();

				//Instance of CloudBackendMessaging to subscribe for cloud messages
				final CloudBackendMessaging cloudBackendMessaging = new CloudBackendMessaging(CreateEventActivity.this);
				cloudBackendMessaging.subscribeToCloudMessage("eventapp", new CloudCallbackHandler<List<CloudEntity>>() {


					@Override
					public void onComplete(List<CloudEntity> results) {
						//  mAnnounceTxt.setText(R.string.announce_success);
						mPosts = results;

						//animateArrival();
						// updateGuestbookView();
					}

					@Override
					public void onError(IOException exception) {
						//  mAnnounceTxt.setText(R.string.announce_fail);
						//animateArrival();
						//handleEndpointException(exception);
					}
				});

				//Creating CloudEntity with topic id 'eventapp'
				CloudEntity cloudEntity = cloudBackendMessaging.createCloudMessage("eventapp");//new CloudEntity("Guestbook");
				//Putting data element in key-value pair
				cloudEntity.put("name", name);
				cloudEntity.put("details", details);
				cloudEntity.put("place", place);
				cloudEntity.put("date", date);
				cloudEntity.put("time", time);
				cloudEntity.put("nooffrnds", noOfFrnds);
				cloudEntity.put("propertyName", "event");

				//Sending the cloud message to save the record 
				cloudBackendMessaging.sendCloudMessage(cloudEntity);

			

				//End the progress dialog
				progressDialog.dismiss();

				//Display event creation success using Toast
				Toast.makeText(CreateEventActivity.this, "Event created successfully!", Toast.LENGTH_LONG).show();

				//Intent to switch to SelectionActivity 
				Intent createEventIntent = new Intent(CreateEventActivity.this , SelectionActivity.class);
				startActivity(createEventIntent);
			}
			break;

		case R.id.cancelButton:
			//Finish the current activity to go back to previous activity
			finish();
			break;

		case R.id.datePickrer :

			showDialog(0);
			break;

		case R.id.timePicker :
			showDialog(1);
			break;
		}

	}

	/**
	 * Date picker dialog
	 */
	@Override
	@Deprecated
	protected Dialog onCreateDialog(int id) {
		if(id ==0){
			return new DatePickerDialog(this, datePickerListener, year, month, day);
		}else if(id ==1){
			return new TimePickerDialog(this, timePickerListener, hour, min, false);
		}
		return new DatePickerDialog(this, datePickerListener, year, month, day);
	}

	/**
	 * Date selection event
	 */
	private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int selectedYear,
				int selectedMonth, int selectedDay) {
			/*if(selectedDay<10){
				dateString =(selectedMonth+1) + " / 0" + selectedDay + " / "
						+ selectedYear;
			}
			else {
				dateString =(selectedMonth+1) + " / " + selectedDay + " / "
						+ selectedYear;
			}*/
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/M/dd", new Locale("US"));
			Calendar cal = Calendar.getInstance();
			
			cal.set(Calendar.YEAR, selectedYear);
			cal.set(Calendar.MONTH, selectedMonth);
			cal.set(Calendar.DAY_OF_MONTH, selectedDay);
		
			dateString = sdf.format(cal.getTime());
			_datePicker.setText(dateString);
		}
	};

	/**
	 * Time picker dialog
	 */
	private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {
		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			int hour;
			String am_pm;
			if (hourOfDay > 12) {
				hour = hourOfDay - 12;
				am_pm = "PM";
			} else {
				hour = hourOfDay;
				am_pm = "AM";
			}

			timeString = hour + " : " + minute;
			_timePicker.setText(hour + " : " + minute + " " + am_pm);
		}
	};



	/**
	 * Method called when an item is selected from spinner
	 */
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		// On selecting a spinner item
		noOfFriends = parent.getItemAtPosition(position).toString();

	}


	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub

	}

	/**
	 * Custom dialog to show a message
	 * @param title
	 * @param message
	 * @param context
	 * @param redirectToPreviousScreen
	 */
	public static void ShowAlertDialog(final String title, String message, final Context context, final boolean redirectToPreviousScreen) {
		try {

			AlertDialog.Builder builder = new AlertDialog.Builder(((Activity)context).getParent());
			builder.setMessage(message);
			builder.setTitle(title);
			builder.setCancelable(false);
			builder.setInverseBackgroundForced(true);
			builder.setPositiveButton("Ok",	new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface arg0, int arg1) {

				}
			});
			AlertDialog alert = builder.create();
			alert.show();
		} catch (Exception e) {
			Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
		}
	}


}
