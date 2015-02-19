package com.example.nilesh.redbus;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.nilesh.adapter.ReposAdapter;
import com.example.nilesh.model.ReposModel;
import com.example.nilesh.util.Constant;
import com.example.nilesh.util.Utilities;
import com.example.nilesh.util.VolleySingleton;
import com.google.gson.Gson;

import org.json.JSONObject;


public class MainActivity extends ActionBarActivity {

    private EditText searchQuery;
    private ListView reposList;
    private ReposAdapter adapter;
    private ProgressBar loading;
    private TextView noReposFound;
    private ReposModel data;
    private Toolbar toolBar;

    public static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        searchQuery = (EditText) findViewById(R.id.searchQuery);
        reposList = (ListView) findViewById(R.id.reposList);
        loading = (ProgressBar) findViewById(R.id.loading);
        noReposFound = (TextView) findViewById(R.id.noReposFound);
        toolBar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //called when user taps on search button
    public void searchRepos(View v)
    {
        hideKeyboard();
        if(Utilities.isDeviceOnline(getApplicationContext())) {
            String query = searchQuery.getText().toString();
            //show alert dialog if user taps on search without entering any name else make request
            if (query.length() == 0)
                showErrorAlert();
            else
                makeRequest(query);
        }else{
            Toast.makeText(getApplicationContext(), "Device is offline.", Toast.LENGTH_LONG).show();
        }
    }

    //shows alert message to the user
    private void showErrorAlert()
    {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(MainActivity.this)
                .setTitle("Invalid request")
                .setMessage("Name cannot be blank")
                .setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog alert = alertBuilder.create();
        alert.show();
    }

    //making reuqest to API with the user provided query
    public void makeRequest(String query)
    {
        loading.setVisibility(View.VISIBLE);

        // creating a json request
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Method.GET,
                Constant.SEARCH_REPOS_URL + query, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                   loading.setVisibility(View.INVISIBLE);
                   //creating POJO object from Json using GSON API
                   data = new Gson().fromJson(response.toString(), ReposModel.class);

                   //showing no repository found if total count is zero, otherwise displays the list
                   if(data.getTotalCount()==0)
                   {
                       noReposFound.setVisibility(View.VISIBLE);
                       reposList.setVisibility(View.INVISIBLE);
                   }
                   else
                   {
                       noReposFound.setVisibility(View.INVISIBLE);
                       adapter = new ReposAdapter(getApplicationContext(), data.getItems());
                       reposList.setVisibility(View.VISIBLE);
                       reposList.setAdapter(adapter);
                   }
            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError arg0) {
                Log.e("TAG", "Error"+arg0);
            }
        });

        //adding to the queue for further processing
        VolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }

    private void hideKeyboard()
    {
        InputMethodManager imm = (InputMethodManager)getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(searchQuery.getWindowToken(), 0);
    }

}
