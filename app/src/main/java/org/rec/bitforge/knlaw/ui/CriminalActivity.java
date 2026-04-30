package org.rec.bitforge.knlaw.ui;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import org.rec.bitforge.knlaw.DB;
import org.rec.bitforge.knlaw.R;
import org.rec.bitforge.knlaw.adapters.LawAdapter;
import org.rec.bitforge.knlaw.entities.Law;
import org.rec.bitforge.knlaw.entities.Punishment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CriminalActivity extends AppCompatActivity {

    DB db;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_criminal);

        setTitle("Criminal Laws");

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = Room.databaseBuilder(
                getApplicationContext(),
                DB.class,
                "law-db"
        ).allowMainThreadQueries().build();

        // 🔥 Fetch ALL criminal laws
        List<Law> laws = db.lawDao().getAllCriminalLaws();

        // 🔥 Build punishment map (same logic)
        Map<Integer, List<Punishment>> punishmentMap = new HashMap<>();

        for (Law law : laws) {
            List<Punishment> list =
                    db.punishmentDao().getAllPunishmentByLawId(law.id);

            if (list != null && !list.isEmpty()) {
                punishmentMap.put(law.id, list);
            }
        }

        // 🔥 Set adapter
        LawAdapter adapter = new LawAdapter(laws, punishmentMap);
        recyclerView.setAdapter(adapter);
    }
}