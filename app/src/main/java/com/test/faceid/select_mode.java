package com.test.faceid;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class select_mode extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;

    private Toolbar mToolbar;
    private NavigationView navigationView;

    private TextView title3 = null;
    private TextView txt_studentID = null;
    private TextView txt_studentName = null;
    private EditText input_studentID = null;
    private EditText input_studentName = null;
    private Button btn_check, btn_record, btn_upload, btn_confirm;

    String professor = "";
    String className = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_select_mode);

        mToolbar = (Toolbar) findViewById(R.id.nav_action);
        setSupportActionBar(mToolbar);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);


        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        title3 = (TextView) findViewById(R.id.title3) ;
        txt_studentID = (TextView) findViewById(R.id.txt_studentID) ;
        txt_studentName = (TextView) findViewById(R.id.txt_studentName) ;
        input_studentID = (EditText) findViewById(R.id.input_studentID) ;
        input_studentName = (EditText) findViewById(R.id.input_studentName) ;
        btn_check = (Button) findViewById(R.id.btn_check) ;
        btn_record = (Button) findViewById(R.id.btn_record) ;
        btn_upload = (Button) findViewById(R.id.btn_upload) ;
        btn_confirm = (Button) findViewById(R.id.btn_confirm) ;
//        btn_check.setOnClickListener((View.OnClickListener) this);
//        btn_record.setOnClickListener((View.OnClickListener) this);
//        btn_upload.setOnClickListener((View.OnClickListener) this);



        Bundle bundle = getIntent().getExtras();
        professor = bundle.getString("professor");
        className = bundle.getString("className");
        title3.setText(professor+": "+className);


        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String studentID = input_studentID.getText().toString();
                String studentName = input_studentName.getText().toString();

                if (studentID.equals("") || studentName.equals("")){
                    Toast toast = Toast.makeText(getApplicationContext(), "Please input student information!", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else{
                    Intent it = new Intent(select_mode.this, face_reco.class);
                    Bundle bd = new Bundle();
                    bd.putString("professor", professor);
                    bd.putString("className", className);
                    bd.putString("mode", "upload");
                    bd.putString("studentID", studentID);
                    bd.putString("studentName", studentName);
                    it.putExtras(bd);
                    startActivity(it);

                }

            }
        });

    }

    public void onClick(View v){
        switch (v.getId()) {
            case R.id.btn_check:
                Intent it2 = new Intent(select_mode.this, attendance.class);
                Bundle bd2 = new Bundle();
                bd2.putString("professor", professor);
                bd2.putString("className", className);
                it2.putExtras(bd2);
                startActivity(it2);

                break;

            case R.id.btn_record:

                Intent it = new Intent(select_mode.this, face_reco.class);
                Bundle bd = new Bundle();
                bd.putString("professor", professor);
                bd.putString("className", className);
                bd.putString("mode", "record");
                it.putExtras(bd);
                startActivity(it);

                break;

            case R.id.btn_upload:

                txt_studentID.setVisibility(View.VISIBLE);
                input_studentID.setVisibility(View.VISIBLE);
                txt_studentName.setVisibility(View.VISIBLE);
                input_studentName.setVisibility(View.VISIBLE);
                btn_confirm.setVisibility(View.VISIBLE);

                break;

        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull final MenuItem item) {
        Log.i("MenuItem", "InNavigationdrawer");
        int id = item.getItemId();
        if (id == R.id.nav_login) {
            startActivity(new Intent(this, login.class));
            this.finish();

//            Intent navLogin = new Intent(Section.this, login.class);
//            Section.this.startActivity(navLogin);
//            return true;
        }

        if (id == R.id.nav_section) {
            startActivity(new Intent(this, Section.class));

//            Intent navLogin = new Intent(Section.this, login.class);
//            Section.this.startActivity(navLogin);
//            return true;
        }

        if (id == R.id.nav_camera) {
            startActivity(new Intent(this, Camera.class));

//            Intent navCamera = new Intent(Section.this, Camera.class);
//            Section.this.startActivity(navCamera);
//            return true;
        }
        return  false;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(mToggle.onOptionsItemSelected(item)){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
