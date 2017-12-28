
package com.example.android.sunshine;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.sunshine.ForecastAdapter.ForecastAdapterOnClickHandler;

import com.example.android.sunshine.utilities.NetworkUtils;
import com.example.android.sunshine.utilities.OpenWeatherJsonUtils;

import java.net.URL;


public class MainActivity extends AppCompatActivity implements ForecastAdapterOnClickHandler , LoaderManager.LoaderCallbacks<String[]> {

    private static final String TAG = MainActivity.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private ForecastAdapter mForecastAdapter;

    private TextView mErrorMessageDisplay;

    private ProgressBar mLoadingIndicator;
    final  static String key = "key";
    final static int loader_id = 22;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);


        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_forecast);


        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);


        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        mRecyclerView.setLayoutManager(layoutManager);


        mRecyclerView.setHasFixedSize(true);


        mForecastAdapter = new ForecastAdapter(this);


        mRecyclerView.setAdapter(mForecastAdapter);


        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);





        getSupportLoaderManager().initLoader(loader_id,null,MainActivity.this);
    }











    @Override
    public void onClick(String weatherForDay) {
        Context context = this;
        Class destinationClass = DetailActivity.class;
        Intent intentToStartDetailActivity = new Intent(context, destinationClass);
        intentToStartDetailActivity.putExtra(Intent.EXTRA_TEXT, weatherForDay);
        startActivity(intentToStartDetailActivity);
    }


    private void showWeatherDataView() {

        mErrorMessageDisplay.setVisibility(View.INVISIBLE);

        mRecyclerView.setVisibility(View.VISIBLE);
    }


    private void showErrorMessage() {

        mRecyclerView.setVisibility(View.INVISIBLE);

        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    @Override
    public Loader<String[]> onCreateLoader(int id, final Bundle args) {


        return new AsyncTaskLoader<String[]>(this) {
            String[] weatherdata = null;

            @Override
            protected void onStartLoading() {
           if(weatherdata!=null){
               deliverresult(weatherdata);

           }
           else {
               mLoadingIndicator.setVisibility(View.VISIBLE);
                forceLoad();
           }
            }

            @Override
            public String[] loadInBackground() {
               SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

                String location = sharedPreferences.getString(getString(R.string.location_key),getString(R.string.location_defvalue));
                String unit = sharedPreferences.getString(getString(R.string.unit_key),getString(R.string.unit_defvalue));
                URL weatherRequestUrl = NetworkUtils.buildUrl(location,unit);

                try {

                    String jsonWeatherResponse = NetworkUtils
                            .getResponseFromHttpUrl(weatherRequestUrl);

                    String[] simpleJsonWeatherData = OpenWeatherJsonUtils
                            .getSimpleWeatherStringsFromJson(MainActivity.this, jsonWeatherResponse);

                    return simpleJsonWeatherData;

                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
            public void deliverresult(String[] data){
                weatherdata = data;
                super.deliverResult(data);

            }



        };
    }

    @Override
    public void onLoadFinished(Loader<String[]> loader, String[] data) {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        if (data != null) {
            showWeatherDataView();
            mForecastAdapter.setWeatherData(data);
        } else {
            showErrorMessage();
        }
    }

    @Override
    public void onLoaderReset(Loader<String[]> loader) {

    }








    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.forecast, menu);



        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();


        if (id == R.id.action_refresh) {
            mForecastAdapter.setWeatherData(null);

            getSupportLoaderManager().restartLoader(loader_id, null, this);
            return true;
        }


        if(id == R.id.action_settings){
            Intent intent = new Intent(this,SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.action_exit){
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}