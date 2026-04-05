package org.rec.bitforge.knlaw.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "related_laws",
        foreignKeys = {
                @ForeignKey(
                        entity = Law.class,
                        parentColumns = "id",
                        childColumns = "lawId",
                        onDelete = ForeignKey.CASCADE
                ),
                @ForeignKey(
                        entity = Law.class,
                        parentColumns = "id",
                        childColumns = "relatedLawId",
                        onDelete = ForeignKey.CASCADE
                )
        },
        indices = {
                @Index("lawId"),
                @Index("relatedLawId"),
                @Index(value = {"lawId", "relatedLawId"}, unique = true)
        }
)
public class RelatedLaw {

    @PrimaryKey(autoGenerate = true)
    public int id;

    // 🔗 Main law
    public int lawId;

    // 🔗 Related law
    public int relatedLawId;

    // 📌 Type of relationship
    // Examples:
    // "SIMILAR", "AMENDMENT", "REFERENCE", "EXCEPTION"
    public String relationType;

    // 📝 Optional explanation
    public String description;
}