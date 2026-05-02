package org.rec.bitforge.knlaw.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import org.rec.bitforge.knlaw.entities.Punishment;

import java.util.List;

@Dao
public interface PunishmentDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Punishment punishment);

    @Query("SELECT * FROM punishments WHERE lawId = :lawId LIMIT 1")
    Punishment getPunishmentByLawId(int lawId);

    @Query("SELECT * FROM punishments WHERE lawId = :lawId")
    List<Punishment> getAllPunishmentByLawId(int lawId);
}
