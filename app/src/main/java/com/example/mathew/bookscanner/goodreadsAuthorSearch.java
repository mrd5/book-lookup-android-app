package com.example.mathew.bookscanner;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import fr.arnaudguyon.xmltojsonlib.XmlToJson;

/**
 * Created by Mathew on 30-May-18.
 */

public class goodreadsAuthorSearch extends AppCompatActivity {

    TextView authorBooks;
    private RequestQueue mRequestQueue;
    private StringRequest stringRequest;
    private static final String TAG = goodreadsAuthorSearch.class.getName();

    String url;
    String authorId;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goodreads_author);

        authorBooks = (TextView) findViewById(R.id.authorBooks);
        authorId = getIntent().getStringExtra("EXTRA_AUTHOR_VALUE");
        url = "https://www.goodreads.com/author/show.xml?key=wdsSpDEw0RUMK64bcNPnWg&id=" + authorId;

        authorBooks.setText(url);
    }

    private void getResults() {//This function makes an http connection with goodreads and displays certain values about the book

        mRequestQueue = Volley.newRequestQueue(this);


    }


}
