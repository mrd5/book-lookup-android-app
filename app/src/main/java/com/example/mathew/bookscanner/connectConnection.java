package com.example.mathew.bookscanner;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

/**
 * Created by Mathew on 18-May-18.
 */

//This class passes the book's isbn through to the google books API and displays details about the book
public class connectConnection extends AppCompatActivity {
    String isbn; //barcode value
    String url; //

    //Where details will be displayed
    TextView bookTitle;
    TextView bookDesc;
    ImageView bookImage;

    //Used to make the http connection to googleapis.com
    private RequestQueue mRequestQueue;
    private StringRequest stringRequest;
    private static final String TAG = connectConnection.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lookup_barcode);

        isbn = getIntent().getStringExtra("EXTRA_BARCODE_VALUE");
        bookTitle = (TextView) findViewById(R.id.bookTitle);
        bookDesc = (TextView) findViewById(R.id.bookDesc);
        bookImage = (ImageView) findViewById(R.id.bookImage);
        url = "https://www.googleapis.com/books/v1/volumes?q=isbn:" + isbn;

        getResults();
    }

    private void getResults(){//This function makes the http connection and displays book values on the phone
        mRequestQueue = Volley.newRequestQueue(this);

        stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i(TAG, response.toString());
                String jsonData = response;

                try {
                    JSONObject main = new JSONObject(jsonData);

                    if (main.getInt("totalItems") == 0){//If the book isn't in the google books database
                        bookTitle.setText("Book not found!");
                        bookDesc.setText("");
                    }
                    else{//If the book is in the database
                        JSONArray items = main.getJSONArray("items");
                        JSONObject book = items.getJSONObject(0);

                        JSONObject volumeInfo = book.getJSONObject("volumeInfo");
                        JSONObject imageLinks = volumeInfo.getJSONObject("imageLinks");

                        String smallThumbnail = imageLinks.getString("thumbnail"); //URL of the book cover
                        String bookName = volumeInfo.getString("title"); //Title of the book
                        String bookDescription = volumeInfo.getString("description"); //Description of the book

                        //Display the values
                        Picasso.get().load(smallThumbnail).into(bookImage); //Picasso library used to easily display the image of the book
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

    public void goodreads(View v){
        Intent intent = new Intent(this, goodreads.class);
        intent.putExtra("EXTRA_BARCODE_VALUE", isbn); //Passes barcode value from main activity to this activity
        startActivityForResult(intent, 0);
    }
}
