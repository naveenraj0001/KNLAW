package org.rec.bitforge.knlaw;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.rec.bitforge.knlaw.entities.Law;

import java.util.List;

public class LawAdapter extends RecyclerView.Adapter<LawAdapter.ViewHolder> {

    List<Law> list;

    public LawAdapter(List<Law> list) {
        this.list = list;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView title, section, explanation, desc, punishment, police;

        public ViewHolder(View view) {
            super(view);

            title = view.findViewById(R.id.txtTitle);
            section = view.findViewById(R.id.txtSection);
            explanation = view.findViewById(R.id.txtExplanation);

            // 🔥 NEW FIELDS
            desc = view.findViewById(R.id.txtDesc);
            punishment = view.findViewById(R.id.txtPunishment);
            police = view.findViewById(R.id.txtPolice);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_law, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Law law = list.get(position);
        holder.section.setText("⚖️ Law: " + law.actName + " | Section: " + law.section);

        holder.desc.setText("📖 Description:\n" + law.description);
        holder.explanation.setText("🧠 Simple Explanation:\n" + law.simpleExplanation);

        holder.punishment.setText(
                "🚨 Min Punishment: " + law.minPunishment +
                        "\n💀 Max Punishment: " + law.maxPunishment
        );

        holder.police.setText("👮 Police Action:\n" + law.policeAction);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}