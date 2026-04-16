package org.rec.bitforge.knlaw.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.content.SharedPreferences;

import org.rec.bitforge.knlaw.R;
import org.rec.bitforge.knlaw.ai.GeminiAI;

import android.speech.RecognizerIntent;
import android.content.Intent;
import java.util.ArrayList;
import java.util.Locale;
import android.speech.tts.TextToSpeech;

public class AIActivity extends AppCompatActivity {

    TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_aiactivity);

        setTitle("AI Legal Assistant");

        tts = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) {
                tts.setLanguage(Locale.US);
            }
        });

        EditText input = findViewById(R.id.inputAI);
        Button btn = findViewById(R.id.btnAskAI);
        TextView result = findViewById(R.id.txtAIResult);
        Button btnMic = findViewById(R.id.btnMic);
        Button btnLang = findViewById(R.id.btnLang);

        SharedPreferences prefs = getSharedPreferences("settings", MODE_PRIVATE);
        String lang = prefs.getString("voice_lang", null);

        // 🔥 FIRST TIME LANGUAGE
        if (lang == null) {
            showLanguageDialog(prefs);
        }

        // 🔥 CHANGE LANGUAGE BUTTON
        btnLang.setOnClickListener(v -> showLanguageDialog(prefs));

        // 🎤 MIC
        btnMic.setOnClickListener(v -> {

            result.setText("🎤 Listening...\n\n");

            String selectedLang = prefs.getString("voice_lang", "en-US");

            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, selectedLang);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, selectedLang);
            intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3);
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak now...");

            try {
                startActivityForResult(intent, 1);
            } catch (Exception e) {
                result.setText("Voice not supported 😭");
            }
        });

        // 🤖 AI BUTTON
        btn.setOnClickListener(v -> {

            String question = input.getText().toString().trim();

            if (question.isEmpty()) {
                result.setText("Please Enter the Question!");
                return;
            }

            result.setText("🤖 Thinking...\n\n");

            GeminiAI.sendMessageStream(
                    question,
                    new GeminiAI.StreamCallback() {

                        @Override
                        public void onChunk(String text) {
                            result.append(text);
                            tts.speak(text, TextToSpeech.QUEUE_ADD, null, null);
                        }

                        @Override
                        public void onComplete() {}

                        @Override
                        public void onError(String error) {
                            result.setText("❌ Error: " + error);
                        }
                    }
            );
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // 🔥 LANGUAGE DIALOG METHOD
    private void showLanguageDialog(SharedPreferences prefs) {
        new AlertDialog.Builder(this)
                .setTitle("Select Language")
                .setItems(new String[]{"English", "Tamil"}, (dialog, which) -> {

                    String selected = (which == 0) ? "en-US" : "ta-IN";
                    prefs.edit().putString("voice_lang", selected).apply();

                })
                .show();
    }

    // 🎤 VOICE RESULT
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {

            ArrayList<String> resultList =
                    data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

            if (resultList != null && !resultList.isEmpty()) {

                String spokenText = resultList.get(0);

                EditText input = findViewById(R.id.inputAI);
                TextView result = findViewById(R.id.txtAIResult);

                input.setText(spokenText);

                result.setText("📝 Press 'Ask AI' to send");
            }
        }
    }

    @Override
    protected void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }
}