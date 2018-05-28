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
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

import fr.arnaudguyon.xmltojsonlib.XmlToJson;

/**
 * Created by Mathew on 22-May-18.
 */

public class goodreads extends AppCompatActivity {
    String isbn;
    String url;
    TextView bookTitle;
    TextView bookDesc;
    ImageView bookImage;
    String name;

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
        url = "https://www.goodreads.com/search.xml?key=wdsSpDEw0RUMK64bcNPnWg&q=" + isbn;

        getResults();
    }


    private void getResults(){
        mRequestQueue = Volley.newRequestQueue(this);

        stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i(TAG, response.toString());
                String xmlData = response;

                XmlToJson jsonData = new XmlToJson.Builder(xmlData).build();

                try {
                    JSONObject main = new JSONObject(jsonData.toString());
                    JSONObject GoodreadsResponse = main.getJSONObject("GoodreadsResponse");
                    JSONObject search = GoodreadsResponse.getJSONObject("search");
                    JSONObject results = search.getJSONObject("results");
                    JSONObject work = results.getJSONObject("work");
                    JSONObject best_book = work.getJSONObject("best_book");
                    JSONObject author = best_book.getJSONObject("author");

                    String name = author.getString("name");
                    String bookImageURL = best_book.getString("image_url");
                    String title = best_book.getString("title");
                    String average_rating = work.getString("average_rating");

                    bookTitle.setText(title);
                    Picasso.get().load(bookImageURL).into(bookImage);
                    book

                } catch (Exception e){
                    e.printStackTrace();
                }

                //bookDesc.setText(jsonData.toString());


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
