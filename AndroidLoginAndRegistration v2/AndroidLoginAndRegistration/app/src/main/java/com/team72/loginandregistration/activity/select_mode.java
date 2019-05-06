package com.team72.loginandregistration.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.team72.loginandregistration.R;


public class select_mode extends AppCompatActivity implements View.OnClickListener{
    private TextView title3 = null;
    private TextView txt_studentID = null;
    private TextView txt_studentName = null;
    private EditText input_studentID = null;
    private EditText input_studentName = null;
    private Button btn_check, btn_record, btn_upload, btn_confirm, btn_student;

    String professor = "";
    String className = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_mode);

        title3 = (TextView) findViewById(R.id.title3) ;
        txt_studentID = (TextView) findViewById(R.id.txt_studentID) ;
        txt_studentName = (TextView) findViewById(R.id.txt_studentName) ;
        input_studentID = (EditText) findViewById(R.id.input_studentID) ;
        input_studentName = (EditText) findViewById(R.id.input_studentName) ;
        btn_check = (Button) findViewById(R.id.btn_check) ;
        btn_record = (Button) findViewById(R.id.btn_record) ;
        btn_upload = (Button) findViewById(R.id.btn_upload) ;
        btn_confirm = (Button) findViewById(R.id.btn_confirm) ;
        btn_student = (Button) findViewById(R.id.btn_student) ;
        btn_check.setOnClickListener(this);
        btn_record.setOnClickListener(this);
        btn_upload.setOnClickListener(this);
        btn_student.setOnClickListener(this);
//store these values instead of expecting them from previous source
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

            case R.id.btn_student:

                Intent it_student = new Intent(select_mode.this, RegisterActivity2.class);
                Bundle bd_student = new Bundle();
                bd_student.putString("professor", professor);
                bd_student.putString("className", className);
                bd_student.putString("mode", "record");
                it_student.putExtras(bd_student);
                startActivity(it_student);

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
}
