package org.rec.bitforge.knlaw.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "laws")
public class Law {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String actName;        // e.g. IPC
    public String section;        // e.g. 379
    public String title;          // Short title
    public String description;    // Detailed law
    public String simpleExplanation; // Easy explanation

}
