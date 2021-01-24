package org.storiesbehindthestars.wwiifallenapp.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.io.Serializable;

//Not meant for saving in Room necessarily, just used same syntax for consistency

@Entity
public class Story implements Serializable {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "storyText")
    public String storyText;

    @ColumnInfo(name = "profilePicURL")
    public String profilePicURL;

    @ColumnInfo(name = "backgroundPicURL")
    public String backgroundPicURL;

    @ForeignKey(entity = ApiResource.class, parentColumns = "apiResourceColumn", childColumns = "storyColumn") //related to API call to get a specific story
    public String apiResourceFK;


    public Story(ApiResource apiResource){
        this.apiResourceFK = apiResource.id;
        //TODO: get story and text from apiResource id, Use API
    }

    public Story (String name, String text, String url1, String url2){
        this.name = name;
        this.storyText = text;
        this.profilePicURL = url1;
        this.backgroundPicURL = url2;
    }

}