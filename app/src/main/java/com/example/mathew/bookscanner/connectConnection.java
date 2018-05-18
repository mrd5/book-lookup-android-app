package com.example.mathew.bookscanner;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Mathew on 18-May-18.
 */

public class connectConnection extends AppCompatActivity {
    String isbn;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        isbn = getIntent().getStringExtra("EXTRA_BARCODE_VALUE");


        getResults();
    }

    private void getResults(){
        try{
            URL url = new URL("https://www.googleapis.com/books/v1/volumes?q=isbn:" + isbn);
            URLConnection request = url.openConnection();
            request.connect();


        } catch (IOException e){
            e.printStackTrace();
        }

    }
}
