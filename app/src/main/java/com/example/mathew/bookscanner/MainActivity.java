package com.example.mathew.bookscanner;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;

public class MainActivity extends AppCompatActivity {

    TextView barcodeResult; //Shows barcode in main activity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        barcodeResult = (TextView) findViewById(R.id.textBarcode);
    }

    public void scanBarcode(View v){//Intent that will use the Google Barcode scanner API to get the book's barcode value
        Intent intent = new Intent(this, ScanBarcodeActivity.class);
        startActivityForResult(intent, 0);
    }

    public void searchBarcode(View v){//Uses the barcode value with Google Books API to return details about the book
        Intent intent = new Intent(this, connectConnection.class);
        intent.putExtra("EXTRA_BARCODE_VALUE", barcodeResult.getText().toString());//Passes barcode value from main activity to his activity
        startActivityForResult(intent, 0);
    }

    public void searchBarcodeGoodreads(View v){//Uses the barcode value with Goodreads API to return details about the book
        Intent intent = new Intent(this, goodreads.class);
        intent.putExtra("EXTRA_BARCODE_VALUE", barcodeResult.getText().toString()); //Passes barcode value from main activity to this activity
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){//This is the first method called after taking an image from camera
        if (requestCode == 0){
            if (resultCode == CommonStatusCodes.SUCCESS){
                if (data != null){//If there is a barcode
                    Barcode barcode = data.getParcelableExtra("barcode");
                    barcodeResult.setText(barcode.displayValue);

                    Intent intent = new Intent(this, connectConnection.class);
                    intent.putExtra("EXTRA_BARCODE_VALUE", barcodeResult.getText().toString());//Passes barcode value from main activity to his activity
                    startActivityForResult(intent, 0);


                } else {//If there is no barcode
                    barcodeResult.setText("");
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }



}
