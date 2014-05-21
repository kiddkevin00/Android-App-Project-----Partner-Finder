package com.google.cloud.backend.ui;

import java.util.HashMap;
import java.util.Properties;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.google.cloud.backend.R;
import com.google.cloud.backend.core.CloudBackend;
import com.google.cloud.backend.core.CloudEntity;

/**
 * Activity to show details of an Event
 */
public class ShowEventActivity extends Activity implements OnClickListener{

	//Variable declaration
	private EditText _nameEditText;
	private EditText _detailsEditText;
	private EditText _placeEditText;
	private EditText _dateEditText;
	private EditText _timeEditText;
	private EditText _noOfFrndsEditText;

	private Button _joinButton;
	private Button _cancelButton;

	private ProgressDialog progressDialog;

	protected String timeString;
	protected String dateString;
	protected String noOfFriends;
	private CloudBackend cloudBackend;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.showevent);

		//Get intent and data from EventListAdaptor 
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();

		//Get properties from bundle/serialized object
		@SuppressWarnings("unchecked")
		HashMap<String, Object> properties = (HashMap<String, Object>) bundle.getSerializable("eventInfo");

		//Getting data according to keys
		String name = (String) properties.get("name");
		String details = (String) properties.get("details");
		String place = (String) properties.get("place");
		String date = (String) properties.get("date");
		String time = (String) properties.get("time");
		String noOfFriends = (String) properties.get("nooffrnds");
		String propertyName = (String) properties.get("propertyName");



		//Initializing the UI components
		_nameEditText = (EditText) findViewById(R.id.nameEditText);
		_detailsEditText = (EditText) findViewById(R.id.detailsEditText);
		_placeEditText = (EditText) findViewById(R.id.placeEditText);
		_dateEditText = (EditText) findViewById(R.id.dateEditText);
		_timeEditText = (EditText) findViewById(R.id.timeEditText);
		_noOfFrndsEditText= (EditText) findViewById(R.id.nofFrndsEditText);


		//Initializing buttons
		_joinButton = (Button) findViewById(R.id.joinButton);
		_joinButton.setOnClickListener(this);

		_cancelButton = (Button) findViewById(R.id.cancelButton);
		_cancelButton.setOnClickListener(this);


		//Setting the values to UI components
		_nameEditText.setText(name);
		_detailsEditText.setText(details);
		_placeEditText.setText(place);
		_dateEditText.setText(date);
		_timeEditText.setText(time);
		_noOfFrndsEditText.setText(noOfFriends);

	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.createButton:

			break;

		case R.id.cancelButton:

			finish();
			break;

		}

	}

}
