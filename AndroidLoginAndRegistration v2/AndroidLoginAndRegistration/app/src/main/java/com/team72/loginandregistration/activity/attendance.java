package com.team72.loginandregistration.activity;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.team72.loginandregistration.R;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Properties;


public class attendance extends AppCompatActivity implements View.OnClickListener{
    private EditText input_studentID = null;
    private Button btn_search;
    private TextView txt_attendance;

    String professor = "";
    String className = "";

    private static Session session = null;
    private static ChannelExec channelExec;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendance);

        input_studentID = (EditText)findViewById(R.id.input_studentID2);
        txt_attendance = (TextView) findViewById(R.id.txt_attendance);
        txt_attendance.setMovementMethod(ScrollingMovementMethod.getInstance());
        btn_search = (Button) findViewById(R.id.btn_search) ;
        btn_search.setOnClickListener(this);

        Bundle bundle = getIntent().getExtras();
        professor = bundle.getString("professor");
        className = bundle.getString("className");
    }

    public void onClick(View v){
        switch (v.getId()) {
            case R.id.btn_search:

                String studentID = input_studentID.getText().toString();

                if (studentID.equals("")){
                    Toast toast = Toast.makeText(getApplicationContext(), "Please input studentID!", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else{
                    StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
                    try {
                        versouSshUtil("burrow.sice.indiana.edu", "team72", "chez+rasp=whish", 22);

                        String cmd = "python android_connect/check_att.py "+professor+" "+className+" "+studentID;
                        String aaa = runCmd(cmd);

                        txt_attendance.setText(aaa);

                    }catch(Exception e) {
                        txt_attendance.setText(e.toString());
                    }


                }


                break;

        }
    }

    public static void versouSshUtil(String host, String userName, String password, int port) throws Exception {
        JSch jsch = new JSch();
        session = jsch.getSession(userName, host, port);
        session.setPassword(password);
        Properties config = new Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);
        session.connect();

    }


    public String runCmd(String cmd) throws Exception {
        channelExec = (ChannelExec) session.openChannel("exec");
        channelExec.setCommand(cmd);
        channelExec.setInputStream(null);
        channelExec.setErrStream(System.err);
        channelExec.connect();
        InputStream in = channelExec.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in, Charset.forName("UTF-8")));
        String result = "";
        String buf = null;
        while ((buf = reader.readLine()) != null) {
            result += buf;
        }
        reader.close();
        channelExec.disconnect();

        return result;

    }

}
