package com.example.mathew.bookscanner;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
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

//This class passes a book's isbn value to goodreads api and displays details about the book

public class goodreads extends AppCompatActivity {
    String isbn; //The barcode value
    String url; //The goodreads url combined with the isbn
    String authorId; //Unique ID of the author

    //Where details about the book wil be displayed
    TextView bookTitle;
    TextView bookDesc;
    TextView bookAuthor;
    ImageView bookImage;

    //Used to make the http connection
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
        bookAuthor = (TextView) findViewById(R.id.bookAuthor);
        url = "https://www.goodreads.com/search.xml?key=wdsSpDEw0RUMK64bcNPnWg&q=" + isbn;

        getResults();
    }


    private void getResults(){//This function makes an http connection with goodreads and displays certain values about the book
        mRequestQueue = Volley.newRequestQueue(this);

        stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i(TAG, response.toString());
                String xmlData = response;

                XmlToJson jsonData = new XmlToJson.Builder(xmlData).build(); //Goodreads data is in xml so it is changed to json to parse

                try {
                    JSONObject main = new JSONObject(jsonData.toString());
                    JSONObject GoodreadsResponse = main.getJSONObject("GoodreadsResponse");
                    JSONObject search = GoodreadsResponse.getJSONObject("search");
                    JSONObject results = search.getJSONObject("results");
                    JSONObject work = results.getJSONObject("work");
                    JSONObject best_book = work.getJSONObject("best_book");
                    JSONObject author = best_book.getJSONObject("author");

                    String name = author.getString("name"); //Name of the author
                    String bookImageURL = best_book.getString("image_url"); //URL of the books front cover image
                    String title = best_book.getString("title"); //Title of the book
                    String average_rating = work.getString("average_rating"); //Average Goodreads rating of thr book


                    //Display the values
                    bookTitle.setText(title);
                    bookAuthor.setText(name);
                    bookAuthor.setTextColor(Color.BLUE);
                    Picasso.get().load(bookImageURL).into(bookImage); //Picasso library used to display the image of the book


                } catch (Exception e){
                    e.printStackTrace();
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

    public void authorLookup(View v){
        Intent intent = new Intent(this, goodreadsAuthorSearch.class);
        intent.putExtra("EXTRA_AUTHOR_VALUE", bookAuthor.getText().toString()); //Passes barcode value from main activity to this activity
        startActivityForResult(intent, 0);
    }
}
