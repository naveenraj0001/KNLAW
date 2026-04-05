package org.rec.bitforge.knlaw.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import org.rec.bitforge.knlaw.DB;
import org.rec.bitforge.knlaw.R;
import org.rec.bitforge.knlaw.entities.Keyword;
import org.rec.bitforge.knlaw.entities.Law;

import java.util.List;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import org.rec.bitforge.knlaw.LawAdapter;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        setTitle("Search Laws");

        EditText input = findViewById(R.id.inputSearch);
        Button btn = findViewById(R.id.btnSearchNow);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        DB db = Room.databaseBuilder(
                getApplicationContext(),
                DB.class, "law-db"
        ).allowMainThreadQueries().build();
        btn.setOnClickListener(v -> {

            String text = input.getText().toString().toLowerCase();

            List<Law> list = db.keywordDao().searchAll(text);

            if (list.isEmpty()) {
                // optional: you can show a toast or keep empty list
                recyclerView.setAdapter(null);
            } else {
                LawAdapter adapter = new LawAdapter(list);
                recyclerView.setLayoutManager(new LinearLayoutManager(SearchActivity.this));
                recyclerView.setAdapter(adapter);
            }
        });
    }
}

