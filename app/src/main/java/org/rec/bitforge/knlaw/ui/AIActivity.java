package org.rec.bitforge.knlaw.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.rec.bitforge.knlaw.R;
import org.rec.bitforge.knlaw.ai.GeminiAI;

public class AIActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_aiactivity);

        setTitle("AI Legal Assistant");

        EditText input = findViewById(R.id.inputAI);
        Button btn = findViewById(R.id.btnAskAI);
        TextView result = findViewById(R.id.txtAIResult);


        btn.setOnClickListener(v -> {

            String question = input.getText().toString().trim();

            if (question.isEmpty()) {
                result.setText("Please Enter the Question!");
                return;
            }

            result.setText("🤖 Thinking...\n\n");

            // 🔥 CALL GEMINI AI (STREAM MODE)
            GeminiAI.sendMessageStream(
                    question,
                    new GeminiAI.StreamCallback() {

                        @Override
                        public void onChunk(String text) {
                            result.append(text); // 🔥 live typing effect
                        }

                        @Override
                        public void onComplete() {
                            // done (optional)
                        }

                        @Override
                        public void onError(String error) {
                            result.setText("❌ Error: " + error);
                        }
                    }
            );
        });

        // 🔥 Edge-to-edge fix
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}