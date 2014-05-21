package com.google.cloud.backend.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.google.cloud.backend.R;

/**
 * Login activity
 */
public class MainActivity extends Activity {

	//Variable declaration
	private EditText _username ;
	private EditText _password ;
	private Button _loginButton ;
	public static String loginUser;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loginactivity);
		
		_username = (EditText) findViewById(R.id.editText1);
		_password = (EditText) findViewById(R.id.editText2);
		
		_loginButton = (Button) findViewById(R.id.button1);
		
		_loginButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				loginUser = _username.getText().toString();
				Intent intent = new Intent(MainActivity.this,SelectionActivity.class);
				startActivity(intent);
			}
		});
	}

	

}
