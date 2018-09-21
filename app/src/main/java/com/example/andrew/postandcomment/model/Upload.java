package com.example.andrew.postandcomment.model;

/**
 * Created by Andrew on 5/4/2018.
 */

public class Upload {
    private String name;
    private String imageUrl;

    public Upload(){
        //empty constructor needed
    }

    public Upload(String Name, String ImageUrl){
        if(Name.trim().equals("")){
            Name = "No Name";
        }
        name = Name;
        imageUrl = ImageUrl;
    }

    public String getName(){
        return name;
    }

    public void setName(String Name){
        name = Name;
    }

    public String getImageUrl(){
        return imageUrl;
    }

    public void setImageUrl(String ImageUrl){
        imageUrl = ImageUrl;
    }


}
