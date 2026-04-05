package org.rec.bitforge.knlaw.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "keywords",
        foreignKeys = @ForeignKey(
                entity = Law.class,
                parentColumns = "id",
                childColumns = "lawId",
                onDelete = ForeignKey.CASCADE
        ),
        indices = {
                @Index("lawId"),
                @Index("keyword"),
                @Index(value = {"lawId", "keyword"}, unique = true)
        }
)
public class Keyword {

    @PrimaryKey(autoGenerate = true)
    public int id;

    // 🔗 Foreign key to Law
    public int lawId;

    // 🔍 Search keyword (store in lowercase for consistency)
    public String keyword;
}