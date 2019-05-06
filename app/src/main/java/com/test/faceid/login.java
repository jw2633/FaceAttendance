package com.test.faceid;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Properties;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;




public class login extends AppCompatActivity implements View.OnClickListener{

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;

    private Toolbar mToolbar;


    Button b1,b2;
    private EditText input_username = null;
    private EditText input_password = null;

    private static Session session = null;
    private static ChannelExec channelExec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        b1 = (Button)findViewById(R.id.button1);
        input_username = (EditText)findViewById(R.id.usernameInput);
        input_password = (EditText)findViewById(R.id.passwordInput);
        b2 = (Button)findViewById(R.id.button2);
        b1.setOnClickListener(this);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

//        switch (item.getItemId()){
//            case R.id.nav_login:
//
//                Intent myIntent = new Intent(this, login.class);
//
//                //start new activity
//                startActivity(myIntent);
//
//                break;
//
//            case R.id.nav_section:
//
//                Intent myIntent2 = new Intent(this, Section.class);
//
//                //start new activity
//                startActivity(myIntent2);
//
//                break;
//
//            case R.id.nav_camera:
//
//                Intent myIntent3 = new Intent(this, Camera.class);
//
//                //start new activity
//                startActivity(myIntent3);
//
//                break;
//        }

//        if(mToggle.onOptionsItemSelected(item)){
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    public void onClick(View v){
        switch (v.getId()) {
            case R.id.button1:



                String username = input_username.getText().toString();
                String password = input_password.getText().toString();

                String rightPW = "";

                StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
                try {
                    versouSshUtil("burrow.sice.indiana.edu", "team72", "chez+rasp=whish", 22);
                    rightPW = runCmd("android_connect/checkPW.php", username);

                }catch(Exception e) {
                    e.printStackTrace();
                }


                if (rightPW.equals(password)){
                    // startActivity(new Intent("com.test.faceid.Section"));
                    Intent it = new Intent(login.this, Section.class);
                    Bundle bd = new Bundle();
                    bd.putString("professor", username);
                    it.putExtras(bd);
                    startActivity(it);

                }else if (rightPW.equals("User doesn't exist")) {
                    Toast toast = Toast.makeText(getApplicationContext(), "User doesn't exist!", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), "Wrong Password!", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }


                break;

        }
    }
    public static void versouSshUtil(String host, String userName, String password, int port) throws Exception{
        JSch jsch = new JSch();
        session = jsch.getSession(userName, host, port);
        session.setPassword(password);
        Properties config = new Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);
        session.connect();

    }

    public String runCmd(String phpFilePath, String username) throws Exception{
        String cmd = "php " + phpFilePath + " " + username;
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
