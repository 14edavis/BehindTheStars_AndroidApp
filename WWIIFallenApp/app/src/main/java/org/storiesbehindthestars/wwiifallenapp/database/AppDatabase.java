package org.storiesbehindthestars.wwiifallenapp.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import org.storiesbehindthestars.wwiifallenapp.models.ApiResource;

//Uses Room to create a database that the app can save things to
//Room also implemented in build.gradle
@Database(entities = {ApiResource.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ApiResourceDao getContactDao();

}
