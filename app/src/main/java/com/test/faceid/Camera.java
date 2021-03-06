package com.test.faceid;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.wonderkiln.camerakit.CameraKitError;
import com.wonderkiln.camerakit.CameraKitEvent;
import com.wonderkiln.camerakit.CameraKitEventListener;
import com.wonderkiln.camerakit.CameraKitImage;
import com.wonderkiln.camerakit.CameraKitVideo;
import com.wonderkiln.camerakit.CameraView;

import org.tensorflow.contrib.android.TensorFlowInferenceInterface;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Properties;
import com.jcraft.jsch.Session;




import android.support.design.widget.NavigationView;



public class Camera extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private CameraView cameraView;
    private ImageView imageViewResult;
    private TextView textViewResult;
    private Button btnDetectObject, btnToggleCamera;

    private static final String MODEL_FILE = "file:///android_asset/20180402-114759.pb";
    private static final String INPUT_NAME = "input:0";
    private static final String OUTPUT_NAME = "embeddings:0";
    private static final String PHASE_NAME = "phase_train:0";
    private static final String[] outputNames = new String[] {OUTPUT_NAME}; //???

    private static final int INPUT_SIZE = 160;
    private TensorFlowInferenceInterface inferenceInterface;

    private static Session session = null;
    private static ChannelExec channelExec;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;

    private Toolbar mToolbar;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_camera);

        mToolbar = (Toolbar) findViewById(R.id.nav_action);
        setSupportActionBar(mToolbar);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);


        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        cameraView = (CameraView) findViewById(R.id.cameraView);
        imageViewResult = (ImageView) findViewById(R.id.imageViewResult);
        textViewResult = (TextView) findViewById(R.id.textViewResult);
        textViewResult.setMovementMethod(new ScrollingMovementMethod()); //???
        btnToggleCamera = (Button) findViewById(R.id.btnToggleCamera);
        btnDetectObject = (Button) findViewById(R.id.btnDetectObject);


        cameraView.addCameraKitListener(new CameraKitEventListener() {
            @Override
            public void onEvent(CameraKitEvent cameraKitEvent) {

            }

            @Override
            public void onError(CameraKitError cameraKitError) {

            }

            @Override
            public void onImage(CameraKitImage cameraKitImage) {
                Bitmap bitmap = cameraKitImage.getBitmap();
                bitmap = Bitmap.createScaledBitmap(bitmap, INPUT_SIZE, INPUT_SIZE, false);
                imageViewResult.setImageBitmap(bitmap);
                float[] modelInput = imagePreprocess(bitmap);
                float[] results = TFpredict(modelInput);

                StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
                try {
                    versouSshUtil("burrow.sice.indiana.edu", "team72", "chez+rasp=whish", 22);

                    String cmd = "rm -rf android_connect/encoding.txt;";
                    for(int i=0; i<512; i++){
                        cmd = cmd + "python android_connect/encodingTXT.py " + String.valueOf(results[i]) + ";";
                    }

                    cmd = cmd + "python android_connect/faceRECO.py";
                    String aaa = runCmd(cmd);

                    textViewResult.setText(aaa);

                }catch(Exception e) {
                    textViewResult.setText(e.toString());
                }

            }

            @Override
            public void onVideo(CameraKitVideo cameraKitVideo) {

            }
        });

        //???
        btnToggleCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraView.toggleFacing();
            }
        });


        //???
        btnDetectObject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraView.captureImage();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        cameraView.start();
    }

    @Override
    protected void onPause() {
        cameraView.stop();
        super.onPause();
    }

    public float[] imagePreprocess(Bitmap bitmap){
        int height = bitmap.getHeight();
        int width = bitmap.getWidth();
        float imageMean = 127.5f;
        float imageStd = 128;
        int[] intImage = new int[INPUT_SIZE*INPUT_SIZE];
        float[] floatImage = new float[INPUT_SIZE*INPUT_SIZE*3];

        bitmap.getPixels(intImage,0,width,0,0,width,height);
        for (int i=0; i<intImage.length; i++){
            final int val = intImage[i];
            floatImage[i*3+0] = (((val >> 16) & 0xFF) - imageMean) / imageStd;
            floatImage[i*3+1] = (((val >> 8) & 0xFF) - imageMean) / imageStd;
            floatImage[i*3+2] = ((val & 0xFF) - imageMean) / imageStd;
        }

        return floatImage;
    }

    public float[] TFpredict(float[] modelInput){

        float[] outputs = new float[512];

        try{
            inferenceInterface = new TensorFlowInferenceInterface(getAssets(), MODEL_FILE);
            //feed
            inferenceInterface.feed(INPUT_NAME, modelInput, 1,INPUT_SIZE,INPUT_SIZE,3);
            boolean []phase = new boolean[1];
            phase[0] = false;
            inferenceInterface.feed(PHASE_NAME,phase);
            //run
            inferenceInterface.run(outputNames, false);
            //fetch
            inferenceInterface.fetch(OUTPUT_NAME, outputs);
        }catch(Exception e){
            Toast toast = Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }

        return outputs;

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


    public String runCmd(String cmd) throws Exception{
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
