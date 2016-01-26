package com.example.rabab.moviesfamily;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;



/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    private final  String LOG_TAG = FetchMovies.class.getSimpleName();
    public GridView mGridView;
    public ProgressBar mProgressBar;
    public GridViewAdapter mGridAdapter;
    public ArrayList<GridItem> mGridData;

    private static final String SELECTED_KEY = "selected_position";
    private int mPosition = ListView.INVALID_POSITION;


    public int listPosition = 0;

    public Parcelable parcelable;
    private static Parcelable mListViewScrollPos = null;
    //DB
    MoviesFamilyDBHandler dbHandler;

    private ArrayAdapter<String> movieAdpter;

    public MainActivityFragment() {
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onStart() {
        super.onStart();
        Log.v("here", "");
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String  sort = prefs.getString(getString(R.string.pref_units_key), getString(R.string.pref_units_popularity));
        if(sort.equals("favorite")) {
            dbHandler = new MoviesFamilyDBHandler(getActivity(),null,null,1);
            ArrayList<GridItem> G = new ArrayList<GridItem>();
            G= dbHandler.getAllFvouriteMovies();
            if (G != null && !G.isEmpty() ) {
//
//                if(mGridAdapter != null && !mGridAdapter.isEmpty()) {
//                    mGridAdapter.clear();
//                }
                mGridAdapter = new GridViewAdapter(getActivity(), R.layout.grid_item_layout, G);
                mGridView.setAdapter(mGridAdapter);
                //mGridAdapter.setGridData(G);
            }else {
                if (mGridAdapter != null && !mGridAdapter.isEmpty()) {
                    mGridAdapter.clear();
                }
                Log.v("favorite on start","there are no favorite");
            }
        }else {

            mGridData = new ArrayList<>();
            mGridAdapter = new GridViewAdapter(getActivity(), R.layout.grid_item_layout, mGridData);
            mGridView.setAdapter(mGridAdapter);

            FetchMovies fetchMovies = new FetchMovies();
            fetchMovies.execute(sort);
            Log.e("xx", sort);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        mGridView = (GridView) rootView.findViewById(R.id.gridView);
        mProgressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);


//        InfoActivityFragment displayFrag = (InfoActivityFragment) getFragmentManager()
//                .findFragmentById(R.id.movie_detail_container);
//        if (displayFrag != null) {
//            GridItem item = (GridItem) mGridView.getItemAtPosition(1);
//            displayFrag.updateContent(item);
//            Log.v("herrrrrrre",item.getId());
//        }else {
//            GridItem item = (GridItem) mGridView.getItemAtPosition(1);
//            displayFrag.updateContent(item);
//            Log.v("herrrrrrre","jjjjjjjjjjjjjjjjj");
//        }

        mGridView.smoothScrollToPosition(1);

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                mPosition = position;
                InfoActivityFragment displayFrag = (InfoActivityFragment) getFragmentManager()
                        .findFragmentById(R.id.movie_detail_container);
                GridItem item = (GridItem) parent.getItemAtPosition(position);
                if (displayFrag == null) {
                    Intent intent = new Intent(getActivity(), InfoActivity.class);
                    intent.putExtra("id", item.getId());
                    intent.putExtra("title", item.getTitle());
                    intent.putExtra("image", item.getImage());
                    intent.putExtra("overview", item.getOverview());
                    intent.putExtra("release_data", item.getRelease_date());
                    intent.putExtra("vote_average", item.getVote_average());
                    startActivity(intent);
                } else {
                    if (item.getTitle() != null)
                        displayFrag.updateContent(item);

                }


            }
        });

        if (savedInstanceState != null && savedInstanceState.containsKey(SELECTED_KEY)) {

            Log.v("rotate", String.valueOf(savedInstanceState.getInt(SELECTED_KEY)));

            mGridView.smoothScrollToPosition(2);

        }

        return rootView;
    }

    @Override
        public void onSaveInstanceState(Bundle outState) {

                   if (mPosition != GridView.INVALID_POSITION) {
                        outState.putInt(SELECTED_KEY, mPosition);
                    }
                super.onSaveInstanceState(outState);
       }





    public void getMovieDataFromJson(String moviesJsonStr)
            throws JSONException {
        final String OWM_ID="id";
        final String OWM_RESULTS = "results";
        final String OWM_OVERVIEW = "overview";
        final String OWM_VOTE_AVERAGE = "vote_average";
        final String OWM_ORIGINAL_TITLE = "original_title";
        final String OWM_POSTER_PATH = "poster_path";
        final String OWN_RELEASE_DATE = "release_date";


        JSONObject forecastJson = new JSONObject(moviesJsonStr);
        JSONArray resultsArray = forecastJson.getJSONArray(OWM_RESULTS);

        GridItem item;
        if (mGridData != null && !mGridData.isEmpty()) {
            mGridData.clear();
        }

        for(int i = 0; i < resultsArray.length(); i++) {

            String id;
            String original_title;
            String overview;
            String vote_average;
            String poster_path;
            String release_date;

            item = new GridItem();
            JSONObject movies = resultsArray.getJSONObject(i);
            id = movies.getString(OWM_ID);
            item.setId(id);
            overview = movies.getString(OWM_OVERVIEW);
            item.setOverview(overview);
            original_title = movies.getString(OWM_ORIGINAL_TITLE);
            item.setTitle(original_title);
            vote_average = movies.getString(OWM_VOTE_AVERAGE);
            item.setVote_average(vote_average);
            poster_path = "http://image.tmdb.org/t/p/w185/" + movies.getString(OWM_POSTER_PATH);
            item.setImage(poster_path);
            release_date = movies.getString(OWN_RELEASE_DATE);
            item.setRelease_date(release_date);
            mGridData.add(item);
        }

        for (GridItem s : mGridData) {
            Log.v(LOG_TAG, "Movies entry: " + s.getTitle());
            Log.v(LOG_TAG, "Movies entry: " + s.getImage());
        }

    }


    public class FetchMovies extends AsyncTask<String, Void, Integer> {


        @Override
        protected void onPostExecute(Integer result) {
            if (result == 1) {
                mGridAdapter.setGridData(mGridData);
            } else {
                Toast.makeText(getContext(), "Failed to fetch data!", Toast.LENGTH_SHORT).show();
            }
            mProgressBar.setVisibility(View.GONE);

        }

        @Override
        protected Integer doInBackground(String... params) {

            Integer result = 0;
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String moviesJsonStr = null;

            String appid="23367f8a2064dd359f24c91e6991f106";


            try{

                final String BASE_URL="http://api.themoviedb.org/3/discover/movie?";
                final String SORT_PARAM="sort_by";
                final String APPID_PARAM="api_key";

                Uri builduri = Uri.parse(BASE_URL).buildUpon()
                        .appendQueryParameter(SORT_PARAM, params[0])
                        .appendQueryParameter(APPID_PARAM, appid)
                        .build();

                URL url = new URL(builduri.toString());
                urlConnection = (HttpURLConnection) url.openConnection();
                Log.v(LOG_TAG, "Build URL " + url.toString());
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return result;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {

                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    return result;
                }

                moviesJsonStr = buffer.toString();
                Log.v(LOG_TAG,"movies json string "+moviesJsonStr);



            }catch (Exception e){

                Log.e("PlaceholderFragment", "Error ", e);

                return result;

            }finally{
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("PlaceholderFragment", "Error closing stream", e);
                    }
                }
            }


            try {


                getMovieDataFromJson(moviesJsonStr);
                result=1;
                return result;
            } catch (JSONException e) {


                Log.e(LOG_TAG, e.getMessage(), e);
                e.getStackTrace();
            }

            return result;
        }
    }



}
