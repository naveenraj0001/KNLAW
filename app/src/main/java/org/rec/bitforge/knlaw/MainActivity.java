package org.rec.bitforge.knlaw;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;
import org.rec.bitforge.knlaw.entities.Keyword;
import org.rec.bitforge.knlaw.entities.Law;
import org.rec.bitforge.knlaw.entities.Punishment;
import org.rec.bitforge.knlaw.entities.RelatedLaw;
import org.rec.bitforge.knlaw.ui.AIActivity;
import org.rec.bitforge.knlaw.ui.CriminalActivity;
import org.rec.bitforge.knlaw.ui.FamilyActivity;
import org.rec.bitforge.knlaw.ui.RightsActivity;
import org.rec.bitforge.knlaw.ui.SearchActivity;

import androidx.room.Room;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private String loadJSONFromAsset() {
        try {
            InputStream is = getAssets().open("laws.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            return new String(buffer, "UTF-8");
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private void insertFromJson(DB db) {
        try {
            String[] files = getAssets().list(""); // list all files in assets root

            List<JSONArray> allLawArrays = new ArrayList<>();

            for (String fileName : files) {

                // ✅ Process only JSON files
                if (!fileName.endsWith(".json"))
                    continue;

                InputStream is = getAssets().open(fileName);
                int size = is.available();
                byte[] buffer = new byte[size];
                is.read(buffer);
                is.close();

                String json = new String(buffer, "UTF-8");

                JSONObject root = new JSONObject(json);
                JSONArray lawsArray = root.getJSONArray("laws");

                allLawArrays.add(lawsArray); // store for second pass

                // -------------------------
                // FIRST PASS: Insert laws, keywords, punishments
                // -------------------------
                for (int i = 0; i < lawsArray.length(); i++) {

                    JSONObject obj = lawsArray.getJSONObject(i);

                    // ✅ LAW
                    Law law = new Law();
                    law.actName = obj.optString("actName");
                    law.section = obj.optString("section");
                    law.title = obj.optString("title");
                    law.description = obj.optString("description");
                    law.simpleExplanation = obj.optString("simpleExplanation");

                    law.minPunishment = null;
                    law.maxPunishment = null;
                    law.policeAction = null;

                    long lawIdLong = db.lawDao().insert(law);
                    int lawId = (int) lawIdLong;

                    // ✅ KEYWORDS
                    JSONArray keywords = obj.optJSONArray("keywords");
                    if (keywords != null) {
                        for (int j = 0; j < keywords.length(); j++) {
                            Keyword k = new Keyword();
                            k.lawId = lawId;
                            k.keyword = keywords.getString(j).toLowerCase().trim();

                            db.keywordDao().insert(k);
                        }
                    }

                    // ✅ PUNISHMENTS
                    JSONArray punishments = obj.optJSONArray("punishments");
                    if (punishments != null) {
                        for (int j = 0; j < punishments.length(); j++) {

                            JSONObject pObj = punishments.getJSONObject(j);

                            Punishment p = new Punishment();
                            p.lawId = lawId;

                            p.minimumDuration = pObj.isNull("minimumDuration") ? null : pObj.getLong("minimumDuration");
                            p.maximumDuration = pObj.isNull("maximumDuration") ? null : pObj.getLong("maximumDuration");

                            p.minimumFine = pObj.isNull("minimumFine") ? null : pObj.getDouble("minimumFine");
                            p.maximumFine = pObj.isNull("maximumFine") ? null : pObj.getDouble("maximumFine");

                            p.punishmentType = pObj.optString("punishmentType");
                            p.description = pObj.optString("description");

                            db.punishmentDao().insert(p);
                        }
                    }
                }
            }

            // -------------------------
            // SECOND PASS: Related Laws (across ALL files)
            // -------------------------
            insertRelatedLaws(db, allLawArrays);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void insertRelatedLaws(DB db, List<JSONArray> allLawArrays) {
        try {

            for (JSONArray lawsArray : allLawArrays) {

                for (int i = 0; i < lawsArray.length(); i++) {

                    JSONObject obj = lawsArray.getJSONObject(i);

                    String section = obj.optString("section");

                    Law currentLaw = db.lawDao().getLawBySection(section);
                    if (currentLaw == null)
                        continue;

                    JSONArray related = obj.optJSONArray("relatedSections");

                    if (related != null) {
                        for (int j = 0; j < related.length(); j++) {

                            String relatedSection = related.getString(j);

                            Law relatedLaw = db.lawDao().getLawBySection(relatedSection);

                            if (relatedLaw != null) {
                                RelatedLaw rl = new RelatedLaw();
                                rl.lawId = currentLaw.id;
                                rl.relatedLawId = relatedLaw.id;
                                rl.relationType = "REFERENCE";

                                db.relatedLawDao().insert(rl);
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DB db = Room.databaseBuilder(
                getApplicationContext(),
                DB.class,
                "law-db").allowMainThreadQueries().build();

        // FOR DEBUG ONLY
        db.clearAllTables();

        if (db.lawDao().getCount() == 0) {
            insertFromJson(db);
        }

        setContentView(R.layout.activity_main);

        Button btnRights = findViewById(R.id.btnRights);
        Button btnCriminal = findViewById(R.id.btnCriminal);
        Button btnFamily = findViewById(R.id.btnFamily);
        Button btnSearch = findViewById(R.id.btnSearch);
        Button btnAI = findViewById(R.id.btnAI);

        btnRights.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, RightsActivity.class)));

        btnCriminal.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, CriminalActivity.class)));

        btnFamily.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, FamilyActivity.class)));

        btnSearch.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, SearchActivity.class)));

        btnAI.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, AIActivity.class)));
    }
}
