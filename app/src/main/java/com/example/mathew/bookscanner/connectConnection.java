package com.example.mathew.bookscanner;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

/**
 * Created by Mathew on 18-May-18.
 */

public class connectConnection extends AppCompatActivity {
    String isbn;
    String url;
    TextView bookTitle;
    TextView bookDesc;
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
        url = "https://www.googleapis.com/books/v1/volumes?q=isbn:" + isbn;

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

                        String bookName = volumeInfo.getString("title");
                        String bookDescription = volumeInfo.getString("description");


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
