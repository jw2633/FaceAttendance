package com.test.faceid;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.wonderkiln.camerakit.CameraView;

public class addStudent_Camera extends AppCompatActivity {
    private CameraView cameraView;
    private Button upload, btnToggleCamera, btnTakePhoto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addstudent_camera);
        cameraView = (CameraView) findViewById(R.id.cameraView);
        btnToggleCamera = (Button) findViewById(R.id.btnToggleCamera);
        btnTakePhoto = (Button) findViewById(R.id.btnTakePhoto);
        btnToggleCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraView.toggleFacing();
            }
        });

        btnTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraView.captureImage();
            }
        });
    }



}


