package org.storiesbehindthestars.wwiifallenapp.models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

//uses ROOM to save data. For future app development where we may want to
// track bookmarking or history
@Entity
public class ApiResource implements Serializable {
    @PrimaryKey(autoGenerate = false)  //related to API call to get a specific story
    @NonNull
    public String id;

}
