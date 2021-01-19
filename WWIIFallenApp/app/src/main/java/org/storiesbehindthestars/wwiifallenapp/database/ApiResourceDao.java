package org.storiesbehindthestars.wwiifallenapp.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import org.storiesbehindthestars.wwiifallenapp.models.ApiResource;

import java.util.List;

//Database accessing operations for the Story model
//Lets you create, update, find, delete, and get all
@Dao
public interface ApiResourceDao {
    @Query("SELECT * FROM ApiResource")
    List<ApiResource> getAll();

    @Query("SELECT * FROM ApiResource WHERE id = :id LIMIT 1")
    ApiResource findById(int id);

    @Insert
    long create(ApiResource apiResource);

    @Update
    void update(ApiResource apiResource);

    @Delete
    void delete(ApiResource apiResource);

}
