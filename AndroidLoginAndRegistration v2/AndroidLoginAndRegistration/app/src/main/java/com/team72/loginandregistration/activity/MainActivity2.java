package com.team72.loginandregistration.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import java.util.HashMap;

import com.team72.loginandregistration.R;
import com.team72.loginandregistration.helper.SQLiteHandler2;
import com.team72.loginandregistration.helper.SessionManager2;

public class MainActivity2 extends Activity {

	private TextView txtName;
	private TextView txtEmail;
	private Button btnLogout;

	private SQLiteHandler2 db;
	private SessionManager2 session;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main2);

		txtName = (TextView) findViewById(R.id.name);
		txtEmail = (TextView) findViewById(R.id.email);
		btnLogout = (Button) findViewById(R.id.btnLogout);


		db = new SQLiteHandler2(getApplicationContext());


		session = new SessionManager2(getApplicationContext());

		if (!session.isLoggedIn()) {
			logoutUser();
		}


		HashMap<String, String> user = db.getUserDetails();

		String name = user.get("name");
		String email = user.get("email");


		txtName.setText(name);
		txtEmail.setText(email);


		btnLogout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				logoutUser();
			}
		});
	}


	private void logoutUser() {
		session.setLogin(false);

		db.deleteUsers();


		Intent intent = new Intent(MainActivity2.this, LoginActivity2.class);
		startActivity(intent);
		finish();
	}
}
