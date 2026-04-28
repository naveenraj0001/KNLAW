package org.rec.bitforge.knlaw.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.rec.bitforge.knlaw.DB;
import org.rec.bitforge.knlaw.R;
import org.rec.bitforge.knlaw.adapters.LawAdapter;
import org.rec.bitforge.knlaw.entities.Law;
import org.rec.bitforge.knlaw.entities.Punishment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchActivity extends AppCompatActivity {

    DB db;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        setTitle("Search Laws");

        EditText input = findViewById(R.id.inputSearch);
        Button btn = findViewById(R.id.btnSearchNow);
        recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = Room.databaseBuilder(
                getApplicationContext(),
                DB.class,
                "law-db"
        ).allowMainThreadQueries().build();

        btn.setOnClickListener(v -> {

            String text = input.getText().toString().trim().toLowerCase();

            if (text.isEmpty()) {
                Toast.makeText(this, "Enter something to search", Toast.LENGTH_SHORT).show();
                return;
            }

            List<Law> laws = db.keywordDao().searchAll(text);

            if (laws == null || laws.isEmpty()) {
                recyclerView.setAdapter(null);
                Toast.makeText(this, "No laws found", Toast.LENGTH_SHORT).show();
                return;
            }

            // 🔥 FIX: Map of LIST instead of single object
            Map<Integer, List<Punishment>> punishmentMap = new HashMap<>();

            for (Law law : laws) {

                // 🔥 You MUST have this DAO method:
                // List<Punishment> getPunishmentsByLawId(int lawId);
                List<Punishment> list = db.punishmentDao().getAllPunishmentByLawId(law.id);

                if (list != null && !list.isEmpty()) {
                    punishmentMap.put(law.id, list);
                }
            }

            LawAdapter adapter = new LawAdapter(laws, punishmentMap);
            recyclerView.setAdapter(adapter);
        });
    }
}
