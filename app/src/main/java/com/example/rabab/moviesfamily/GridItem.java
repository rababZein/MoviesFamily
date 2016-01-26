package com.example.rabab.moviesfamily;

/**
 * Created by rabab on 18/12/15.
 */
public class GridItem {
    private String id;
    private String title ;
    private String image;
    private String overview;
    private String vote_average;
    private String release_date;
    private String content;
    private String auther;

    public String getAuther() {
        return auther;
    }

    public void setAuther(String auther) {
        this.auther = auther;
    }




    public String getId() {
        return id;
    }

    public String getTitle (){

        return title;

    }

    public String getImage(){

        return image;

    }

    public String getOverview() {
        return overview;
    }

    public String getVote_average() {
        return vote_average;
    }

    public String getRelease_date() {
        return release_date;
    }

    public String getContent() {
        return content;
    }

    public void setTitle(String yourTitle){

       this.title=yourTitle;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setImage(String yourImage){
        this.image=yourImage;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public void setVote_average(String vote_average) {
        this.vote_average = vote_average;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
