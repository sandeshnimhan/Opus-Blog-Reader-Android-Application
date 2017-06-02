package com.sandeshnimhan.outtinterview;

import android.app.ProgressDialog;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import com.sandeshnimhan.outtinterview.MyDB;

public class MainActivity extends AppCompatActivity {

    /*
    * TAG to display class name/activity- Log on console
    * */
    private String TAG = MainActivity.class.getSimpleName();

    private ProgressDialog pDialog;
    private ListView lv;

    // URL to get posts JSON
    private static String url = "https://jsonplaceholder.typicode.com/posts";

    ArrayList<HashMap<String, String>> postList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        android.support.v7.app.ActionBar menu = getSupportActionBar();
        menu.setDisplayShowHomeEnabled(true);
        menu.setLogo(R.mipmap.outt_logo_icon_transparent);
        menu.setDisplayUseLogoEnabled(true);


        postList = new ArrayList<>();

        lv = (ListView) findViewById(R.id.list);

        new GetPosts().execute();
    }

    /**
     * Async task class to get json by making HTTP call
     */
    private class GetPosts extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog before execution
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Loading...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... param) {
            HttpHandler httpHandler = new HttpHandler();

            // Get request to url
            String jsonString = httpHandler.makeServiceCall(url);
            //Log.d("Debug", jsonString.toString());
            Log.e(TAG, "Returned json string from Http call: " + jsonString);
            /*if(jsonString.contains("<!DOCTYPE"))
                jsonString = jsonString.replace("<!DOCTYPE", "");*/
            if (jsonString != null) {
                try {

                    // Getting JSON Array node without name
                    JSONArray contacts = new JSONArray(jsonString/*.substring(3)*/);
                    /*JSONArray contacts = new JSONArray(jsonString.substring(jsonString.indexOf("["), jsonString.lastIndexOf("]") + 1));*/

                    // For loop all posts
                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);

                        String userId = c.getString("userId");
                        String id = c.getString("id");
                        String title = c.getString("title");
                        String body = c.getString("body");

                        // storing each post
                        HashMap<String, String> postHashMap = new HashMap<>();

                        postHashMap.put("userId", userId);
                        postHashMap.put("id", id);
                        postHashMap.put("title", title);
                        postHashMap.put("body", body);

                        /*Caching Data Locally*/
                        MyDB myDB = new MyDB(getApplicationContext());
                        myDB.createRecords(userId,id,title,body);
                        Cursor mCursor = myDB.selectRecords();
                        while (mCursor.moveToNext())
                        Log.v(TAG, "Record from DB: " + mCursor.toString());

                        // adding each post to global posts ArrayList
                        postList.add(postHashMap);
                    }
                } catch (final JSONException e) { // 'e' is final as it is accessed within inner class below
                    Log.e(TAG, "Unable to parse JSON Objects: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Unable to parse JSON Objects: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            } else {
                Log.e(TAG, "Unable to fetch json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Unable to fetch json from server",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog after execution
            if (pDialog.isShowing())
                pDialog.dismiss();

            /**
             * ListAdapter - view recycling
             * */
            ListAdapter listAdapter = new SimpleAdapter(
                    MainActivity.this, postList,
                    R.layout.list_item, new String[]{"userId", "id",
                    "title", "body"}, new int[]{R.id.userId,
                    R.id.id, R.id.title, R.id.body});

            lv.setAdapter(listAdapter);
        }

    }
}