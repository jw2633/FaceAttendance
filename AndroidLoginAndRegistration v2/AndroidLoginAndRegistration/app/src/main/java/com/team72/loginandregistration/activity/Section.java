package com.team72.loginandregistration.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.team72.loginandregistration.R;
import com.team72.loginandregistration.helper.SQLiteHandler;
import com.team72.loginandregistration.helper.SessionManager;

public class Section extends AppCompatActivity implements View.OnClickListener{
    private TextView title2 = null;
    private Button btn_classA, btn_classB, btn_logout;

    String professor = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_section);

        title2 = (TextView) findViewById(R.id.title2) ;
        btn_classA = (Button) findViewById(R.id.btn_classA) ;
        btn_classB = (Button) findViewById(R.id.btn_classB) ;
        btn_logout = (Button) findViewById(R.id.btn_logout) ;
        btn_classA.setOnClickListener(this);
        btn_classB.setOnClickListener(this);
        btn_logout.setOnClickListener(this);

        Bundle bundle = getIntent().getExtras();
//        if(bundle==null){
//            Toast.makeText(this,"No data received",Toast.LENGTH_LONG).show();
//        } else{
            professor = bundle.getString("professor");
            title2.setText("Welcome " + professor);

//        }


    }

    public void onClick(View v){
        Intent it = new Intent(Section.this, select_mode.class);
        Bundle bd = new Bundle();
        switch (v.getId()) {
            case R.id.btn_classA:

                bd.putString("professor", professor);
                bd.putString("className", "classA");
                it.putExtras(bd);
                startActivity(it);

                break;

            case R.id.btn_classB:

                bd.putString("professor", professor);
                bd.putString("className", "classB");
                it.putExtras(bd);
                startActivity(it);

                break;
            case R.id.btn_logout:
                logoutUser();
                break;

        }
    }
    private void logoutUser() {
        SessionManager session = new SessionManager(getApplicationContext());

        SQLiteHandler db = new SQLiteHandler(getApplicationContext());


        session.setLogin(false);

        db.deleteUsers();


        Intent intent = new Intent(Section.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

}
