package com.example.app_tt2.data.local.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.app_tt2.data.local.entity.TranslationHistoryEntity;

import java.util.List;

@Dao
public interface TranslationHistoryDao {

    @Insert
    long insert(TranslationHistoryEntity entity);

    @Update
    void update(TranslationHistoryEntity entity);

    @Delete
    void delete(TranslationHistoryEntity entity);

    @Query("SELECT * FROM translation_history ORDER BY createdAt DESC")
    List<TranslationHistoryEntity> getAll();

    @Query("SELECT * FROM translation_history WHERE id = :id LIMIT 1")
    TranslationHistoryEntity getById(int id);

    @Query("DELETE FROM translation_history WHERE id = :id")
    void deleteById(int id);

    @Query("DELETE FROM translation_history")
    void clearAll();
}