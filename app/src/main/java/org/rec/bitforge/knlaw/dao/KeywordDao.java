package org.rec.bitforge.knlaw.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import org.rec.bitforge.knlaw.entities.Keyword;
import org.rec.bitforge.knlaw.entities.Law;

import java.util.List;

@Dao
public interface KeywordDao {

    @Insert
    void insert(Keyword law);

    @Query("SELECT DISTINCT laws.* FROM laws " +
            "LEFT JOIN keywords ON laws.id = keywords.lawId " +
            "WHERE keywords.keyword LIKE '%' || :search || '%' " +
            "OR laws.title LIKE '%' || :search || '%' " +
            "OR laws.description LIKE '%' || :search || '%' " +
            "OR laws.simpleExplanation LIKE '%' || :search || '%'")
    List<Law> searchAll(String search);
    @Query("SELECT laws.* FROM laws INNER JOIN keywords ON laws.id = keywords.lawId WHERE keywords.keyword LIKE '%' || :search || '%'")
    List<Law> searchFullLaw(String search);
}
