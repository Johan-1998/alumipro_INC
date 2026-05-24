package com.alumipro.mobile.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alumipro.mobile.R;
import com.alumipro.mobile.model.NotificacionMovil;
import com.alumipro.mobile.utils.FormatUtils;

import java.util.ArrayList;
import java.util.List;

public class NotificacionAdapter extends RecyclerView.Adapter<NotificacionAdapter.ViewHolder> {
    private List<NotificacionMovil> items = new ArrayList<>();

    public void setItems(List<NotificacionMovil> items) {
        this.items = items == null ? new ArrayList<>() : new ArrayList<>(items);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notificacion, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NotificacionMovil item = items.get(position);
        holder.tvTitulo.setText(FormatUtils.safe(item.getTitulo()));
        holder.tvMensaje.setText(FormatUtils.safe(item.getMensaje()));
        holder.tvFecha.setText(FormatUtils.compactDateTime(item.getCreatedAt()));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView tvTitulo;
        final TextView tvMensaje;
        final TextView tvFecha;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitulo = itemView.findViewById(R.id.tvNotifTitulo);
            tvMensaje = itemView.findViewById(R.id.tvNotifMensaje);
            tvFecha = itemView.findViewById(R.id.tvNotifFecha);
        }
    }
}
