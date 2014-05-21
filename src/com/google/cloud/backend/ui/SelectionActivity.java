package com.google.cloud.backend.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.google.cloud.backend.R;
import com.google.cloud.backend.sample.guestbook.GuestbookActivity;
/**
 * Selection activity to select option from three
 */
public class SelectionActivity extends Activity implements OnClickListener{

	//Variable declaration
	private Button _chatButton;
	private Button _findPartnerButton;
	private Button _createEvent;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.selection);

		//Initializing UI components
		_chatButton = (Button) findViewById(R.id.button1);
		_chatButton.setOnClickListener(this);
		_createEvent = (Button) findViewById(R.id.button2);
		_createEvent.setOnClickListener(this);

		_findPartnerButton = (Button) findViewById(R.id.button3);
		_findPartnerButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		//Switch to Chat activity
		case R.id.button1:
			Intent intent = new Intent(SelectionActivity.this, GuestbookActivity.class);
			startActivity(intent);
			break;

			//Switch to create event screen
		case R.id.button2 :
			Intent intent1 = new Intent(SelectionActivity.this ,CreateEventActivity.class);
			startActivity(intent1);

			break;

			//Switch to Events list activity
		case R.id.button3 :

			Intent intent2 = new Intent(SelectionActivity.this ,EventListActivity.class);
			startActivity(intent2);

		default:
			break;
		}
	}
}
