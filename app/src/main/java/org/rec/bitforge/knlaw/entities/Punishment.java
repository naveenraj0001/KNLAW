package org.rec.bitforge.knlaw.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "punishments", foreignKeys = @ForeignKey(entity = Law.class, parentColumns = "id", childColumns = "lawId", onDelete = ForeignKey.CASCADE), indices = {
        @Index("lawId"),
        @Index(value = {
                "lawId",
                "minimumDuration",
                "maximumDuration",
                "minimumFine",
                "maximumFine",
                "punishmentType"
        }, unique = true)
})
public class Punishment {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public int lawId;

    public Long minimumDuration;
    public Long maximumDuration;

    public Double minimumFine;
    public Double maximumFine;

    public String punishmentType;

    public String description;
}
