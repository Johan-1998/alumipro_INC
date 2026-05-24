package com.alumipro.mobile.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alumipro.mobile.R;
import com.alumipro.mobile.model.VentaDetalleItem;
import com.alumipro.mobile.utils.FormatUtils;

import java.util.ArrayList;
import java.util.List;

public class VentaDetalleAdapter extends RecyclerView.Adapter<VentaDetalleAdapter.ViewHolder> {
    private List<VentaDetalleItem> items = new ArrayList<>();

    public void setItems(List<VentaDetalleItem> items) {
        this.items = items == null ? new ArrayList<>() : new ArrayList<>(items);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_venta_detalle, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        VentaDetalleItem item = items.get(position);
        holder.tvProducto.setText(FormatUtils.safe(item.getProductoNombre()));
        holder.tvCantidadPrecio.setText("Cantidad: " + item.getCantidad() + " • Unitario: " + FormatUtils.currency(item.getPrecioUnitario()));
        holder.tvSubtotal.setText("Subtotal: " + FormatUtils.currency(item.getSubtotal()));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView tvProducto;
        final TextView tvCantidadPrecio;
        final TextView tvSubtotal;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProducto = itemView.findViewById(R.id.tvDetalleProducto);
            tvCantidadPrecio = itemView.findViewById(R.id.tvDetalleCantidadPrecio);
            tvSubtotal = itemView.findViewById(R.id.tvDetalleSubtotal);
        }
    }
}
