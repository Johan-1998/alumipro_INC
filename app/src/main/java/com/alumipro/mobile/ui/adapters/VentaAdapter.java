package com.alumipro.mobile.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alumipro.mobile.R;
import com.alumipro.mobile.model.VentaResumen;
import com.alumipro.mobile.utils.FormatUtils;

import java.util.ArrayList;
import java.util.List;

public class VentaAdapter extends RecyclerView.Adapter<VentaAdapter.ViewHolder> {
    public interface Listener {
        void onOpen(VentaResumen venta);
    }

    private final Listener listener;
    private List<VentaResumen> items = new ArrayList<>();

    public VentaAdapter(Listener listener) {
        this.listener = listener;
    }

    public void setItems(List<VentaResumen> items) {
        this.items = items == null ? new ArrayList<>() : new ArrayList<>(items);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_venta, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        VentaResumen item = items.get(position);
        holder.tvCliente.setText("Venta #" + item.getId() + " • " + FormatUtils.safe(item.getClienteNombre()));
        holder.tvMeta.setText("Fecha: " + FormatUtils.compactDate(item.getFecha()) + " • Vendedor: " + FormatUtils.safe(item.getVendedorNombre()));
        holder.tvTotal.setText(FormatUtils.currency(item.getTotal()));
        holder.itemView.setOnClickListener(v -> listener.onOpen(item));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView tvCliente;
        final TextView tvMeta;
        final TextView tvTotal;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCliente = itemView.findViewById(R.id.tvVentaCliente);
            tvMeta = itemView.findViewById(R.id.tvVentaMeta);
            tvTotal = itemView.findViewById(R.id.tvVentaTotal);
        }
    }
}
