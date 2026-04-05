package org.rec.bitforge.knlaw.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import org.rec.bitforge.knlaw.entities.Punishment;

import java.util.List;

@Dao
public interface PunishmentDao {
    @Insert
    void insert(Punishment punishment);
}
