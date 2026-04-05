package org.rec.bitforge.knlaw.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import org.rec.bitforge.knlaw.entities.Keyword;

import java.util.List;

@Dao
public interface KeywordDao {

    @Insert
    void insert(Keyword law);

    @Query("SELECT * FROM laws")
    List<Keyword> getAllKeywords();
}
