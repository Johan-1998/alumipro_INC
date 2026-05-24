package com.alumipro.mobile.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alumipro.mobile.R;
import com.alumipro.mobile.model.Producto;
import com.alumipro.mobile.utils.FormatUtils;

import java.util.ArrayList;
import java.util.List;

public class ProductoAdapter extends RecyclerView.Adapter<ProductoAdapter.ViewHolder> {
    public interface Listener {
        void onEdit(Producto producto);
        void onDelete(Producto producto);
    }

    private final Listener listener;
    private final boolean canManage;
    private List<Producto> items = new ArrayList<>();

    public ProductoAdapter(Listener listener, boolean canManage) {
        this.listener = listener;
        this.canManage = canManage;
    }

    public void setItems(List<Producto> items) {
        this.items = items == null ? new ArrayList<>() : new ArrayList<>(items);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_producto, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Producto item = items.get(position);
        holder.tvNombre.setText(FormatUtils.safe(item.getNombre()));
        holder.tvDescripcion.setText(FormatUtils.safe(item.getDescripcion()));
        holder.tvPrecio.setText(FormatUtils.currency(item.getPrecio()));
        holder.tvStock.setText("Stock: " + item.getStock());
        holder.btnEdit.setVisibility(canManage ? View.VISIBLE : View.GONE);
        holder.btnDelete.setVisibility(canManage ? View.VISIBLE : View.GONE);
        holder.btnEdit.setOnClickListener(v -> listener.onEdit(item));
        holder.btnDelete.setOnClickListener(v -> listener.onDelete(item));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView tvNombre;
        final TextView tvDescripcion;
        final TextView tvPrecio;
        final TextView tvStock;
        final ImageButton btnEdit;
        final ImageButton btnDelete;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvProductoNombre);
            tvDescripcion = itemView.findViewById(R.id.tvProductoDescripcion);
            tvPrecio = itemView.findViewById(R.id.tvProductoPrecio);
            tvStock = itemView.findViewById(R.id.tvProductoStock);
            btnEdit = itemView.findViewById(R.id.btnEditProducto);
            btnDelete = itemView.findViewById(R.id.btnDeleteProducto);
        }
    }
}
