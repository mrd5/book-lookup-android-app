package com.example.mathew.bookscanner;

/**
 * Created by Mathew on 17-May-18.
 */

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;


//This class uses the phone camera to scan for, and get a barcode value
public class ScanBarcodeActivity extends AppCompatActivity {
    SurfaceView cameraPreview;//Will show what the camera sees while scanning for codes

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_barcode); //Shows camera preview while scanning

        cameraPreview = (SurfaceView) findViewById(R.id.cameraPreview);
        createCameraSource();
    }


    private void createCameraSource() {
        BarcodeDetector barcodeDetector = new BarcodeDetector.Builder(this).build(); //BarcodeDetector object

        final CameraSource cameraSource = new CameraSource.Builder(this, barcodeDetector).setAutoFocusEnabled(true).setRequestedPreviewSize(1600, 1024).build(); //CameraSource object
        //setAutoFocusEnabled auto focuses the camera on load
        //1600, 1024 for setRequestedPreviewSize ->Able to scan small barcodes or scan them from a distance

        cameraPreview.getHolder().addCallback(new SurfaceHolder.Callback() {//SurfaceView callback, to start and stop the camera source on surfaceCreated() and surfaceDestroyed()
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {//Camera permission check is needed, start method throws IOException. Permission added in manifest file
                    if (ActivityCompat.checkSelfPermission(ScanBarcodeActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    cameraSource.start(cameraPreview.getHolder()); //Start the camera source
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height){

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder){

            }
        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {//gets detected barcodes in receiveDetections method
            @Override
            public void release(){

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections){//gets barcode
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if (barcodes.size() > 0){
                    Intent intent = new Intent();
                    intent.putExtra("barcode", barcodes.valueAt(0));//stores in intent
                    setResult(CommonStatusCodes.SUCCESS, intent);
                    finish();
                }
            }
        });
    }

}
