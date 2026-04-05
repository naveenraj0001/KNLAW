package org.rec.bitforge.knlaw;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

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