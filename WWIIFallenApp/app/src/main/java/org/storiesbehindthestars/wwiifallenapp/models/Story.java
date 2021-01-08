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

    @ForeignKey(entity = ApiResource.class, parentColumns = "apiResourceColumn", childColumns = "storyColumn") //related to API call to get a specific story
    public String apiResource;

}