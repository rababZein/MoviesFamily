package com.example.rabab.moviesfamily;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

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
public class InfoActivityFragment extends Fragment {

    private final  String LOG_TAG = InfoActivity.class.getSimpleName();

    private TextView titleTextView;
    private TextView overviewTextView;
    private TextView release_dataTextView;
    private TextView vote_averageTextView;
    private ImageView imageView;
    private TextView urlVideoTextView;

    //DB
    MoviesFamilyDBHandler dbHandler;

    public reviewAdapter mGridAdapter;
    public ArrayList<ReviewItem> mGridData;
    public ListView mListView;

    public ListView videoListView;
    public ArrayList<VideoItem> videoData;
    public VideoAdapter videoAdapter;
    public String id ;
    public String title ;
    public String image ;
    public String overview;
    public String release_data;
    public String vote_average ;

    public Button addfavorite;

    private static final String MOVIE_SHARE_HASHTAG = "#moviefamilyApp";
    private String firstVideo = "" ;

    public InfoActivityFragment() {
    }


    public void updateContent (GridItem item){

        id = item.getId();
        title = item.getTitle();
        overview = item.getOverview();
        image = item.getImage();
        release_data=item.getRelease_date();
        vote_average = item.getVote_average();
        titleTextView.setText(Html.fromHtml(title));
        overviewTextView.setText(Html.fromHtml(overview));
        release_dataTextView.setText(Html.fromHtml(release_data));
        vote_averageTextView.setText(Html.fromHtml(vote_average));
        Picasso.with(getActivity()).load(image).into(imageView);
        mGridData = new ArrayList<>();
        mGridAdapter = new reviewAdapter(getActivity(), R.layout.review_item, mGridData);
        mListView.setAdapter(mGridAdapter);
        ReviewMovie reviewMovie = new ReviewMovie();
        reviewMovie.execute(id);
        videoData = new ArrayList<>();
        videoAdapter = new VideoAdapter(getActivity(),R.layout.video_item,videoData);
        videoListView.setAdapter(videoAdapter);
        VideoMovie videoMovie = new VideoMovie();
        videoMovie.execute(id);
        Log.v("cccccccccccccccccccc",firstVideo);
        dbHandler = new MoviesFamilyDBHandler(getActivity(),null,null,1);
        GridItem checkIfMovieExistsInFavorite =   dbHandler.selectWhereID(id);
        if(checkIfMovieExistsInFavorite != null ){
            if(checkIfMovieExistsInFavorite.getId() != null) {
                addfavorite.setText("Already in Favorite");
                addfavorite.setEnabled(false);
            }
        }

        addfavorite.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                Button b = (Button) v;
                GridItem movie = new GridItem();
                movie.setTitle(title);
                movie.setImage(image);
                movie.setId(id);
                movie.setOverview(overview);
                movie.setRelease_date(release_data);
                movie.setVote_average(vote_average);
                dbHandler.addMovie(movie);
                b.setText("Success add to Favorite");
                b.setEnabled(false);
            }
        });

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (container==null){
            return null;
        }
        View rootView = inflater.inflate(R.layout.fragment_info, container, false);
        titleTextView = (TextView) rootView.findViewById(R.id.title);
        overviewTextView = (TextView) rootView.findViewById(R.id.overview);
        release_dataTextView = (TextView) rootView.findViewById(R.id.release_date);
        vote_averageTextView = (TextView) rootView.findViewById(R.id.vote_average);
        imageView = (ImageView) rootView.findViewById(R.id.grid_item_image);
        if(getActivity().getIntent().getExtras() != null) {
            id = getActivity().getIntent().getStringExtra("id");
            title = getActivity().getIntent().getStringExtra("title");
            image = getActivity().getIntent().getStringExtra("image");
            overview = getActivity().getIntent().getStringExtra("overview");
            release_data = getActivity().getIntent().getStringExtra("release_data");
            vote_average = getActivity().getIntent().getStringExtra("vote_average");
            titleTextView.setText(Html.fromHtml(title));
            overviewTextView.setText(Html.fromHtml(overview));
            release_dataTextView.setText(Html.fromHtml(release_data));
            vote_averageTextView.setText(Html.fromHtml(vote_average));
            Picasso.with(getActivity()).load(image).into(imageView);

       }

        mListView = (ListView) rootView.findViewById(R.id.reviewMovie);
        mGridData = new ArrayList<>();
        mGridAdapter = new reviewAdapter(getActivity(), R.layout.review_item, mGridData);
        mListView.setAdapter(mGridAdapter);
        if(getActivity().getIntent().getExtras() != null) {
            ReviewMovie reviewMovie = new ReviewMovie();
            reviewMovie.execute(id);
        }
        videoListView = (ListView) rootView.findViewById(R.id.videoMovie);
        videoData = new ArrayList<>();
        videoAdapter = new VideoAdapter(getActivity(),R.layout.video_item,videoData);
        videoListView.setAdapter(videoAdapter);

        if(getActivity().getIntent().getExtras() != null) {
            VideoMovie videoMovie = new VideoMovie();
            videoMovie.execute(id);

        }

        addfavorite = (Button) rootView.findViewById(R.id.addfavorite);
        if(getActivity().getIntent().getExtras() != null) {
            dbHandler = new MoviesFamilyDBHandler(getActivity(),null,null,1);

            GridItem checkIfMovieExistsInFavorite =   dbHandler.selectWhereID(id);

            if(checkIfMovieExistsInFavorite != null ){
                if(checkIfMovieExistsInFavorite.getId() != null) {
                    addfavorite.setText("Already in Favorite");
                    addfavorite.setEnabled(false);
                }

            }
            addfavorite.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v)
                {
                    Button b = (Button) v;
                    GridItem movie = new GridItem();
                    movie.setTitle(getActivity().getIntent().getStringExtra("title"));
                    movie.setImage(getActivity().getIntent().getStringExtra("image"));
                    movie.setId(getActivity().getIntent().getStringExtra("id"));
                    movie.setOverview(getActivity().getIntent().getStringExtra("overview"));
                    movie.setRelease_date(getActivity().getIntent().getStringExtra("release_data"));
                    movie.setVote_average(getActivity().getIntent().getStringExtra("vote_average"));
                    dbHandler.addMovie(movie);
                    Log.e("data base ", dbHandler.selectMovies());
                    b.setText("Success add to Favorite");
                    b.setEnabled(false);
                }
            });
        }

        return rootView;
    }




    public class VideoMovie extends AsyncTask<String,Void,Integer> {

        private final String LOG_TAG = VideoMovie.class.getSimpleName();
        public String yarab;

        @Override
        protected Integer doInBackground(String... params) {
            Integer result = 0;
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String moviesJsonStr = null;
            String appid="23367f8a2064dd359f24c91e6991f106";
            try{

                final String BASE_URL="http://api.themoviedb.org/3/movie/"+params[0]+"/videos";
                final String APPID_PARAM="api_key";

                Uri builduri = Uri.parse(BASE_URL).buildUpon()
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
                    // Nothing to do.
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
                Log.v(LOG_TAG,"videos json string "+moviesJsonStr);



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
                getVideosDataFromJson(moviesJsonStr);
                result=1;
                return result;
            } catch (JSONException e) {

                Log.e(LOG_TAG, e.getMessage(), e);
                e.getStackTrace();
            }
            return result;
        }


        @Override
        protected void onPostExecute(Integer result) {
            if (result == 1) {
                //Test Video Url
                for (VideoItem s : videoData) {
                    Log.v(LOG_TAG, "Video entry onPostExecute: " + s.getKey());
                    Log.v(LOG_TAG, "Video entry onPostExecute: " + s.getvideoId());
                    yarab=s.getKey();
                }
                videoAdapter.setGridData(videoData);
            }
        }
    }

    private void getVideosDataFromJson(String moviesJsonStr) throws JSONException{
        final String OWM_RESULTS = "results";
        final String OWM_KEY = "key";
        final String OWM_ID="id";
        final String OWM_NAME="name";

        JSONObject videoJson = new JSONObject(moviesJsonStr);
        JSONArray resultsArray = videoJson.getJSONArray(OWM_RESULTS);

        VideoItem item;

        videoData.clear();

        for(int i = 0; i < resultsArray.length(); i++) {
            String id;
            String key;
            String name;
            item = new VideoItem();
            JSONObject movies = resultsArray.getJSONObject(i);
            id = movies.getString(OWM_ID);
            item.setvideoId(id);
            key = movies.getString(OWM_KEY);
            item.setKey(key);
            name = movies.getString(OWM_NAME);
            item.setName(name);
            videoData.add(item);
        }
        int count = 1;
        for (VideoItem s : videoData) {
            // for share
            if(count==1) {
                firstVideo = "www.youtube.com/v=" + s.getKey();
                //yarab = firstVideo;
            }
            count++;
        }


    }


    public class ReviewMovie extends AsyncTask<String,Void,Integer>{
        private final  String LOG_TAG = ReviewMovie.class.getSimpleName();

        @Override
        protected Integer doInBackground(String... params) {
            Integer result = 0;
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String moviesJsonStr = null;
            String appid="23367f8a2064dd359f24c91e6991f106";
            try{

                final String BASE_URL="http://api.themoviedb.org/3/movie/"+params[0]+"/reviews";
                final String APPID_PARAM="api_key";

                Uri builduri = Uri.parse(BASE_URL).buildUpon()
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
                getReviewsDataFromJson(moviesJsonStr);
                result=1;
                return result;
            } catch (JSONException e) {


                Log.e(LOG_TAG, e.getMessage(), e);
                e.getStackTrace();
            }
            return result;

        }

        @Override
        protected void onPostExecute(Integer result) {
            if (result == 1) {
                for (ReviewItem s : mGridData) {
                    Log.v(LOG_TAG, "Review entry onPostExecute: " + s.getAuthor());
                    Log.v(LOG_TAG, "Review entry onPostExecute: " + s.getContent());
                }
                mGridAdapter.setGridData(mGridData);
            }
        }
    }

    public void getReviewsDataFromJson(String moviesJsonStr)throws JSONException {


        final String OWM_RESULTS = "results";
        final String OWM_AUTHOR = "author";
        final String OWM_CONTENT="content";
        final String OWM_ID="id";

        JSONObject forecastJson = new JSONObject(moviesJsonStr);
        JSONArray resultsArray = forecastJson.getJSONArray(OWM_RESULTS);

        ReviewItem item;

        mGridData.clear();

        for(int i = 0; i < resultsArray.length(); i++) {
            String id;
            String content;
            String author;
            item = new ReviewItem();
            JSONObject movies = resultsArray.getJSONObject(i);

            id = movies.getString(OWM_ID);
            item.setId(id);

            content = movies.getString(OWM_CONTENT);
            item.setContent(content);

            author=movies.getString(OWM_AUTHOR);
            item.setAuthor(author);

            mGridData.add(item);
        }

        for (ReviewItem s : mGridData) {
            Log.v(LOG_TAG, "Review entry: " + s.getAuthor());
            Log.v(LOG_TAG, "Review entry: " + s.getContent());
        }



    }





    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        getActivity().getMenuInflater().inflate(R.menu.menu_share, menu);
        if(getActivity().getIntent().getExtras() != null) {
                getActivity().getMenuInflater().inflate(R.menu.menu_main, menu);
        }
        MenuItem menuItem =  menu.findItem(R.id.action_share);
        ShareActionProvider  mShareActionProvider ;
        mShareActionProvider = new ShareActionProvider(getContext());
        mShareActionProvider.setShareIntent(createShareForecastIntent());
        MenuItemCompat.setActionProvider(menuItem, mShareActionProvider);
    }


    private Intent createShareForecastIntent(){
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, firstVideo + " " + MOVIE_SHARE_HASHTAG);
        return shareIntent;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent = new Intent(getActivity(),SettActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }



}
