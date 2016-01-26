package com.example.rabab.moviesfamily;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by rabab on 02/01/16.
 */



public class MoviesFamilyDBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "moviesfamily.db";
    public static final String TABLE_FAVORITEMOVIES = "favouritemovies";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_MOVIEID="movieid";
    public static final String COLUMN_TITLE="title";
    public static final String COLUMN_IMAGE="image";
    public static final String COLUMN_OVERVIEW="overview";
    public static final String COLUMN_VOTEAVERAGE="voteaverage";
    public static final String COLUMN_RELEASEDATE="releasedate";

;


    public MoviesFamilyDBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String query = "CREATE TABLE " + TABLE_FAVORITEMOVIES + " (" +

                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_MOVIEID + " TEXT," +
                COLUMN_TITLE + " TEXT," +
                COLUMN_OVERVIEW + " TEXT," +
                COLUMN_IMAGE + " TEXT," +
                COLUMN_VOTEAVERAGE + " TEXT," +
                COLUMN_RELEASEDATE + " TEXT" +
                ");";

        db.execSQL(query);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVORITEMOVIES);
        onCreate(db);

    }

    public void addMovie(GridItem movie){

        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_MOVIEID,movie.getId());
        contentValues.put(COLUMN_TITLE,movie.getTitle());
        contentValues.put(COLUMN_OVERVIEW,movie.getOverview());
        contentValues.put(COLUMN_IMAGE,movie.getImage());
        contentValues.put(COLUMN_VOTEAVERAGE, movie.getVote_average());
        contentValues.put(COLUMN_RELEASEDATE, movie.getRelease_date());

        SQLiteDatabase db =  getWritableDatabase();
        db.insert(TABLE_FAVORITEMOVIES, null, contentValues);
        db.close();


    }

    public void deleteMovie(int id){

        SQLiteDatabase db =  getWritableDatabase();
        db.execSQL("delete from " + TABLE_FAVORITEMOVIES + "where" + COLUMN_ID + "=" + id + ";");

    }


    public String selectMovies (){
        String result = "";
        SQLiteDatabase db =  getWritableDatabase();
        String query ="Select * from "+TABLE_FAVORITEMOVIES +" where 1";
       // Cursor c = db.rawQuery(db,null);
        Cursor c = db.query(TABLE_FAVORITEMOVIES,null,null, null, null, null, null);
        //Cursor cursor = db.query(WeatherContract.LocationEntry.TABLE_NAME, null, null, null, null, null, null);

      //  assertTrue("Error No record return from location query",cursor.moveToFirst());

      c.moveToFirst();
//        Log.e("data base test select and insert ", c.getString(1));
//        Log.e("data base", "select before while");
//        while (!c.isAfterLast()){
//            Log.e("data base","select before if");
//            if (c.getString(c.getColumnIndex(COLUMN_TITLE))!=null){
//                result += c.getString(c.getColumnIndex(COLUMN_TITLE));
//                Log.e("data base","select after if");
//            }
//        }
//while (c.moveToNext()){
//    result = c.getString(c.getColumnIndex(COLUMN_TITLE));
//    Log.e("data base while " , result);
//}
        db.close();

        return result;
    }

    public ArrayList<GridItem> getAllFvouriteMovies() {
      //  List<GridItem> movies = new LinkedList<GridItem>();
        ArrayList<GridItem> movies  = new ArrayList<>() ;

        // 1. build the query
        String query = "SELECT  * FROM " + TABLE_FAVORITEMOVIES;

        // 2. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // 3. go over each row, build book and add it to list
        GridItem movie = null;
        if (cursor.moveToFirst()) {
            do {
                movie = new GridItem();
                movie.setId(cursor.getString(1));
                movie.setTitle(cursor.getString(2));
                movie.setOverview(cursor.getString(3));
                movie.setImage(cursor.getString(4));
                movie.setVote_average(cursor.getString(5));
                movie.setRelease_date(cursor.getString(6));

                // Add book to books
                movies.add(movie);
            } while (cursor.moveToNext());
        }

        return movies;
    }


    public GridItem selectWhereID(String idOfMovie){

        GridItem movie = new GridItem();



        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM "+TABLE_FAVORITEMOVIES+" WHERE TRIM("+COLUMN_MOVIEID+") = '"+idOfMovie.trim()+"'", null);
            if (cursor.moveToFirst()) {
                movie.setId(cursor.getString(1));
               // Log.v("select", movie.getId());
            }



        return movie;
    }


}
