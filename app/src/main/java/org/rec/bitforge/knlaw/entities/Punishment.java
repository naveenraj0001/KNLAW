package org.rec.bitforge.knlaw.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "punishments",
        foreignKeys = @ForeignKey(
                entity = Law.class,
                parentColumns = "id",
                childColumns = "lawId",
                onDelete = ForeignKey.CASCADE
        ),
        indices = {@Index("lawId")}
)
public class Punishment {

    @PrimaryKey(autoGenerate = true)
    public int id;

    // 🔗 Foreign key to Law table
    public int lawId;

    // ⏱ Duration (store in DAYS for consistency)
    public Long minimumDuration; // nullable
    public Long maximumDuration; // nullable

    // 💰 Fine (in rupees)
    public Double minimumFine;   // nullable
    public Double maximumFine;   // nullable

    // 📌 Type of punishment
    // e.g. "IMPRISONMENT", "FINE", "BOTH"
    public String punishmentType;

    // 📝 Extra notes (e.g. "or both", "repeat offenders")
    public String description;

}