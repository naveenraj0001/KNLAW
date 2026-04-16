package org.rec.bitforge.knlaw.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.Insert;

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
    @ColumnInfo(name = "lawId")
    public int lawId;

    // 🔍 Search keyword
    @ColumnInfo(name = "keyword")
    public String keyword;
}
