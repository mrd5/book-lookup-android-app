package com.example.mathew.bookscanner;

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
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Mathew on 22-May-18.
 */

public class goodreads extends AppCompatActivity {
    String isbn;
    String url;
    TextView bookTitle;
    TextView bookDesc;
    ImageView bookImage;
    private RequestQueue mRequestQueue;
    private StringRequest stringRequest;
    private static final String TAG = connectConnection.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goodreads_lookup);

        isbn = getIntent().getStringExtra("EXTRA_BARCODE_VALUE");
        bookTitle = (TextView) findViewById(R.id.bookTitle);
        bookDesc = (TextView) findViewById(R.id.bookDesc);
        bookImage = (ImageView) findViewById(R.id.bookImage);
        url = "https://www.goodreads.com/search.xml?key=&q=" + isbn;

        getResults();
    }


    private void getResults(){
        mRequestQueue = Volley.newRequestQueue(this);

        stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i(TAG, response.toString());
                String jsonData = response;

                try {
                    JSONObject main = new JSONObject(jsonData);

                    if (main.getInt("totalItems") == 0){
                        bookTitle.setText("Book not found!");
                        bookDesc.setText("");
                    }
                    else{
                        JSONArray items = main.getJSONArray("items");
                        JSONObject book = items.getJSONObject(0);

                        JSONObject volumeInfo = book.getJSONObject("volumeInfo");
                        JSONObject imageLinks = volumeInfo.getJSONObject("imageLinks");

                        String smallThumbnail = imageLinks.getString("thumbnail");
                        String bookName = volumeInfo.getString("title");
                        String bookDescription = volumeInfo.getString("description");

                        Picasso.get().load(smallThumbnail).into(bookImage);
                        bookTitle.setText(bookName);
                        bookDesc.setText(bookDescription);
                    }

                } catch (Exception ex){
                    ex.printStackTrace();
                }

            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                Log.i(TAG, error.toString());
            }
        });

        mRequestQueue.add(stringRequest);



    }
}
