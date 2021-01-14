package com.example.sportnews;

import android.util.Log;

import com.example.sportnews.logic.Logic;
import com.example.sportnews.pojo.Author;
import com.example.sportnews.pojo.Post;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public final class QueryUtils {


    private QueryUtils() {
    }

    public static ArrayList<Post> extractPostList(String JSON_RESPONSE) {
        ArrayList<Post> posts = new ArrayList<>();
        if(JSON_RESPONSE!=null) {
            try {
                JSONObject root = new JSONObject(JSON_RESPONSE);
                JSONObject r=root.getJSONObject("response");
                JSONArray postArray = r.optJSONArray("results");
                for (int i = 0; i < postArray.length(); i++) {
                    JSONObject postObj = postArray.optJSONObject(i);

                    String type = postObj.optString("type");
                    String title = postObj.optString("webTitle");
                    String url = postObj.optString("webUrl");
                    String sectionName = postObj.optString("sectionName");
                    String publishedAt = postObj.optString("webPublicationDate");

                    JSONArray tags=postObj.optJSONArray("tags");
                    Author author=new Author("Unknown Author",null);
                    if(tags!=null&&tags.length()>0){
                        String name=tags.getJSONObject(0).optString("webTitle");
                        String authUrl=tags.getJSONObject(0).optString("webUrl");
                        author=new Author(name,authUrl);
                    }

                    Post post= Logic.MackPost(type,publishedAt,url,title,sectionName,author);
                    posts.add(post);
                }

            } catch (JSONException e) {
                Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
            }
        }
        // Return the list of earthquakes
        return posts;

    }

    public static List<String> extractSectionList(String  JSON_RESPONSE) {
        ArrayList<String> sections = new ArrayList<>();
        if(JSON_RESPONSE!=null) {
            try {
                JSONObject root = new JSONObject(JSON_RESPONSE);
                JSONObject r=root.getJSONObject("response");
                JSONArray postArray = r.optJSONArray("results");
                for (int i = 0; i < postArray.length(); i++) {
                    JSONObject postObj = postArray.optJSONObject(i);

                    String sec = postObj.optString("webTitle");
                    sections.add(sec);
                }

            } catch (JSONException e) {
                Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
            }
        }
        // Return the list of earthquakes
        return sections;
    }
}
