package com.example.seyaha;

import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import java.util.Map;

public class Tour {

    public String[] ImageURLs;
    public Map<String, String> categories;
    public String[] placeNames;
    public double rating;
    public int comments;
    public String title, desc;


    public Tour(String[] imageURLs, Map<String, String> categories, String[] placeNames) {
        ImageURLs = imageURLs;
        this.categories = categories;
        this.placeNames = placeNames;
    }

    public Tour(String[] imageURLs, double rating, int comments, String title, String desc) {
        ImageURLs = imageURLs;
        this.rating = rating;
        this.comments = comments;
        this.title = title;
        this.desc = desc;
    }

    public String makeDesc(String[] categories)
    {
        String result="";
        for(int i=0;i<categories.length;i++)
        {
            if(i==0) {
                result += i;
            }
            else if(i==categories.length-1)
            {
                result+="and "+i+".";
            }
            else
            {
                result+=", "+i;
            }
        }
        return result;
    }

    public static void addClickEffect(View view)
    {
        Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
        animation1.setDuration(500);
        view.startAnimation(animation1);
    }

}