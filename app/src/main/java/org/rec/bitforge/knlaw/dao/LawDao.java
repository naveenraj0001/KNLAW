package org.rec.bitforge.knlaw.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import org.rec.bitforge.knlaw.entities.Law;

import java.util.List;

@Dao
public interface LawDao {

    @Insert
    void insert(Law law);

    @Query("SELECT * FROM laws")
    List<Law> getAllLaws();

    @Query("SELECT * FROM laws WHERE actName LIKE '%' || :search || '%'")
    List<Law> searchByAct(String search);
}
