package org.rec.bitforge.knlaw.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import org.rec.bitforge.knlaw.entities.Law;

import java.util.List;

@Dao
public interface LawDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(Law law);

    @Query("SELECT * FROM laws WHERE section = :section LIMIT 1")
    Law getLawBySection(String section);

    @Query("SELECT * FROM laws")
    List<Law> getAllLaws();

    @Query("SELECT COUNT(*) FROM laws")
    int getCount();

    @Query("SELECT * FROM laws WHERE actName LIKE '%' || :search || '%'")
    List<Law> searchByAct(String search);

    @Query("SELECT * FROM laws l WHERE NOT EXISTS ( SELECT 1 FROM keywords k WHERE k.lawId = l.id AND LOWER(k.keyword) = 'civil')")
    List<Law> getAllCriminalLaws();

    @Query("SELECT * FROM laws l WHERE EXISTS ( SELECT 1 FROM keywords k WHERE k.lawId = l.id AND LOWER(k.keyword) = 'family')")
    List<Law> getFamilyLaws();

    @Query("SELECT * FROM laws l WHERE EXISTS ( SELECT 1 FROM keywords k WHERE k.lawId = l.id AND LOWER(k.keyword) = 'fundamental')")
    List<Law> getFundamentalRights();
}
