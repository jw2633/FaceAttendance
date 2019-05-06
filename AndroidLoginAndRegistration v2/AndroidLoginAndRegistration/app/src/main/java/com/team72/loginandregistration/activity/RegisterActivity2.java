package com.team72.loginandregistration.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import com.team72.loginandregistration.R;
import com.team72.loginandregistration.app.AppConfig2;
import com.team72.loginandregistration.app.AppController2;
import com.team72.loginandregistration.helper.SQLiteHandler2;
import com.team72.loginandregistration.helper.SessionManager;

//import info.androidhive.studentinformation.R;
//import info.androidhive.studentinformation.app.AppConfig2;
//import info.androidhive.studentinformation.app.AppController2;
//import info.androidhive.studentinformation.helper.SQLiteHandler2;
//import info.androidhive.studentinformation.helper.SessionManager2;

public class RegisterActivity2 extends Activity {
    private static final String TAG = RegisterActivity2.class.getSimpleName();
    private Button btnRegister;
    private Button btnLinkToSection;
    private EditText inputFullName;
    private EditText inputLastName;
    private EditText inputEmail;
    //private EditText inputPassword;
    //private EditText inputRe_Password;
    private ProgressDialog pDialog;
    private SQLiteHandler2 db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);

        inputFullName = (EditText) findViewById(R.id.fname);
        inputLastName = (EditText) findViewById(R.id.lname);
        inputEmail = (EditText) findViewById(R.id.email);
        //inputPassword = (EditText) findViewById(R.id.password);
        //inputRe_Password = (EditText) findViewById(R.id.re_password);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnLinkToSection = (Button) findViewById(R.id.btnLinkToSectionScreen);


        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);




        db = new SQLiteHandler2(getApplicationContext());


//        if (session.isLoggedIn()) {
//
//            Intent intent = new Intent(RegisterActivity2.this,
//                    MainActivity2.class);
//            startActivity(intent);
//            finish();
//        }

        // Register Button Click event
        btnRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String fname = inputFullName.getText().toString().trim();
                String lname = inputLastName.getText().toString().trim();
                String email = inputEmail.getText().toString().trim();
                //String password = inputPassword.getText().toString().trim();
                //String re_password = inputRe_Password.getText().toString().trim();

                if (!fname.isEmpty() && !lname.isEmpty() && !email.isEmpty()) {
                    registerUser(fname, lname, email);
                }
                //else if(!password.equals(re_password)){
                    //Toast.makeText(getApplicationContext(),
                            //"Passwords do not match! Please try again.", Toast.LENGTH_LONG)
                            //.show();

                else {
                    Toast.makeText(getApplicationContext(),
                            "Please enter your details!", Toast.LENGTH_LONG)
                            .show();
                }
            }
            });


        btnLinkToSection.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        select_mode.class);
                startActivity(i);
                finish();
            }
        });

    }

    private void loginSuccessfulAction(){
        SessionManager sessionManager1 = new SessionManager(getApplicationContext());

        Intent intent = new Intent(RegisterActivity2.this, select_mode.class);
        Bundle bd = new Bundle();
        bd.putString("professor", sessionManager1.getLoginUsername());
        intent.putExtras(bd);
        startActivity(intent);

        startActivity(intent);
        finish();
    }

    private void registerUser(final String fname, final String lname, final String uname) {

        String tag_string_req = "req_register";

        pDialog.setMessage("Registering ...");
        showDialog();

        StringRequest strReq = new StringRequest(Method.POST,
                AppConfig2.URL_REGISTER, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {

                        JSONObject user = jObj.getJSONObject("user");
                        String fname = user.getString("fname");
                        String lname = user.getString("lname");
                        String uname = user.getString("uname");




//                        db.addUser(fname, lname, email, created_at);

                        Toast.makeText(getApplicationContext(), "User successfully registered. Try login now!", Toast.LENGTH_LONG).show();


                        Intent intent = new Intent(
                                RegisterActivity2.this,
                                LoginActivity2.class);
                        startActivity(intent);
                        finish();
                    } else {


                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                params.put("fname", fname);
                params.put("lname", lname);
                params.put("uname", uname);
                //params.put("password", password);
                //params.put("re_password", re_password);

                return params;
            }

        };


        AppController2.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}
