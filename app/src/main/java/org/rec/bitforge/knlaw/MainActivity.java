package org.rec.bitforge.knlaw;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import org.rec.bitforge.knlaw.entities.Keyword;
import org.rec.bitforge.knlaw.entities.Law;
import org.rec.bitforge.knlaw.ui.AIActivity;
import org.rec.bitforge.knlaw.ui.CriminalActivity;
import org.rec.bitforge.knlaw.ui.FamilyActivity;
import org.rec.bitforge.knlaw.ui.RightsActivity;
import org.rec.bitforge.knlaw.ui.SearchActivity;

import androidx.room.Room;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DB db = Room.databaseBuilder(
                getApplicationContext(),
                DB.class, "law-db"
        ).allowMainThreadQueries().build();

        // demo laws
        if (db.lawDao().getCount() == 0) {
            Law law1 = new Law();
            law1.actName = "IPC";
            law1.section = "379";
            law1.title = "Theft";
            law1.description = "Whoever dishonestly takes movable property out of the possession of any person without that person's consent commits theft.";
            law1.simpleExplanation = "Taking someone’s property without permission.";
            law1.minPunishment = "Fine or jail";
            law1.maxPunishment = "Up to 3 years imprisonment";
            law1.policeAction = "Police can register FIR and start investigation";

            db.lawDao().insert(law1);

            Keyword k1 = new Keyword();
            k1.lawId = 1;
            k1.keyword = "theft";
            db.keywordDao().insert(k1);

            Law law2 = new Law();
            law2.actName = "IPC";
            law2.section = "302";
            law2.title = "Murder";
            law2.description = "Whoever commits murder shall be punished with death or imprisonment for life.";
            law2.simpleExplanation = "Intentionally killing another person.";
            law2.minPunishment = "Life imprisonment";
            law2.maxPunishment = "Death penalty";
            law2.policeAction = "Police will arrest immediately and start investigation";

            db.lawDao().insert(law2);

            Keyword k2 = new Keyword();
            k2.lawId = 2;
            k2.keyword = "murder";
            db.keywordDao().insert(k2);

            Law law3 = new Law();
            law3.actName = "IPC";
            law3.section = "420";
            law3.title = "Cheating";
            law3.description = "Whoever cheats and dishonestly induces a person to deliver property shall be punished.";
            law3.simpleExplanation = "Tricking someone to gain money or benefit.";
            law3.minPunishment = "Fine";
            law3.maxPunishment = "Up to 7 years imprisonment";
            law3.policeAction = "Police may file case and investigate fraud";

            db.lawDao().insert(law3);

            Keyword k3 = new Keyword();
            k3.lawId = 3;
            k3.keyword = "cheating";
            db.keywordDao().insert(k3);
        }
        // demo law over remove the import also when demo law removed

        setContentView(R.layout.activity_main);

        Button btnRights = findViewById(R.id.btnRights);
        Button btnCriminal = findViewById(R.id.btnCriminal);
        Button btnFamily = findViewById(R.id.btnFamily);
        Button btnSearch = findViewById(R.id.btnSearch);
        Button btnAI = findViewById(R.id.btnAI);

        btnRights.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, RightsActivity.class));
        });

        btnCriminal.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, CriminalActivity.class));
        });

        btnFamily.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, FamilyActivity.class));
        });

        btnSearch.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, SearchActivity.class));
        });

        btnAI.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, AIActivity.class));
        });
    }
}