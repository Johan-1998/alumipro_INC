package com.alumipro.mobile.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alumipro.mobile.R;
import com.alumipro.mobile.model.Cliente;
import com.alumipro.mobile.utils.FormatUtils;

import java.util.ArrayList;
import java.util.List;

public class ClienteAdapter extends RecyclerView.Adapter<ClienteAdapter.ViewHolder> {
    public interface Listener {
        void onEdit(Cliente cliente);
        void onDelete(Cliente cliente);
    }

    private final Listener listener;
    private final boolean canDelete;
    private List<Cliente> items = new ArrayList<>();

    public ClienteAdapter(Listener listener, boolean canDelete) {
        this.listener = listener;
        this.canDelete = canDelete;
    }

    public void setItems(List<Cliente> items) {
        this.items = items == null ? new ArrayList<>() : new ArrayList<>(items);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cliente, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Cliente item = items.get(position);
        holder.tvNombre.setText(FormatUtils.safe(item.getNombre()));
        holder.tvMeta.setText("Tel: " + FormatUtils.safe(item.getTelefono()) + " • Correo: " + FormatUtils.safe(item.getEmail()));
        holder.btnEdit.setOnClickListener(v -> listener.onEdit(item));
        holder.btnDelete.setVisibility(canDelete ? View.VISIBLE : View.GONE);
        holder.btnDelete.setOnClickListener(v -> listener.onDelete(item));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView tvNombre;
        final TextView tvMeta;
        final ImageButton btnEdit;
        final ImageButton btnDelete;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvClienteNombre);
            tvMeta = itemView.findViewById(R.id.tvClienteMeta);
            btnEdit = itemView.findViewById(R.id.btnEditCliente);
            btnDelete = itemView.findViewById(R.id.btnDeleteCliente);
        }
    }
}
