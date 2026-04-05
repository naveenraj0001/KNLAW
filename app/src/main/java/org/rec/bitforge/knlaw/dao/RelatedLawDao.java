package org.rec.bitforge.knlaw.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import org.rec.bitforge.knlaw.entities.RelatedLaw;

import java.util.List;

@Dao
public interface RelatedLawDao {
    @Insert
    void insert(RelatedLaw law);
}
