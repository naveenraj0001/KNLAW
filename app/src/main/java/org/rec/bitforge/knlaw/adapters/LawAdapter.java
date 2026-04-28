package org.rec.bitforge.knlaw.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.rec.bitforge.knlaw.R;
import org.rec.bitforge.knlaw.entities.Law;
import org.rec.bitforge.knlaw.entities.Punishment;

import java.util.List;
import java.util.Map;

public class LawAdapter extends RecyclerView.Adapter<LawAdapter.ViewHolder> {

    List<Law> list;
    Map<Integer, List<Punishment>> punishmentMap; // 🔥 UPDATED

    public LawAdapter(List<Law> list, Map<Integer, List<Punishment>> punishmentMap) {
        this.list = list;
        this.punishmentMap = punishmentMap;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView title, section, explanation, desc, punishment, police;

        public ViewHolder(View view) {
            super(view);

            title = view.findViewById(R.id.txtTitle);
            section = view.findViewById(R.id.txtSection);
            explanation = view.findViewById(R.id.txtExplanation);

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

        holder.desc.setText("📖 Description:\n" + safe(law.description));
        holder.explanation.setText("🧠 Simple Explanation:\n" + safe(law.simpleExplanation));

        // 🔥 MULTIPLE PUNISHMENTS SUPPORT
        List<Punishment> punishments =
                punishmentMap != null ? punishmentMap.get(law.id) : null;

        if (punishments != null && !punishments.isEmpty()) {

            StringBuilder builder = new StringBuilder();

            for (Punishment p : punishments) {

                builder.append("⚖️ Punishment:\n")
                        .append(safe(p.description)).append("\n\n");

                String min = "-";
                String max = "-";

                if (p.minimumDuration != null) {
                    min = p.minimumDuration + " days";
                }

                if (p.maximumDuration != null) {
                    max = p.maximumDuration + " days";
                }

                if (p.minimumFine != null) {
                    min += " + ₹" + p.minimumFine;
                }

                if (p.maximumFine != null) {
                    max += " + ₹" + p.maximumFine;
                }

                builder.append("🚨 Min: ").append(min).append("\n");
                builder.append("💀 Max: ").append(max).append("\n\n");
            }

            holder.punishment.setText(builder.toString());

        } else {
            holder.punishment.setText("🚨 Punishment: Not available");
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private String safe(String value) {
        return value == null ? "-" : value;
    }
}
