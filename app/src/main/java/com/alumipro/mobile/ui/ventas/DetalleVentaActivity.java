package com.alumipro.mobile.ui.ventas;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.appbar.MaterialToolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.alumipro.mobile.R;
import com.alumipro.mobile.data.remote.ApiClientFactory;
import com.alumipro.mobile.model.VentaDetalle;
import com.alumipro.mobile.ui.adapters.VentaDetalleAdapter;
import com.alumipro.mobile.utils.ApiErrorUtils;
import com.alumipro.mobile.utils.FormatUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetalleVentaActivity extends AppCompatActivity {
    private final VentaDetalleAdapter adapter = new VentaDetalleAdapter();
    private TextView tvCliente;
    private TextView tvMeta;
    private TextView tvTotal;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_venta);
        MaterialToolbar toolbar = findViewById(R.id.toolbarDetalleVenta);
        toolbar.setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material);
        toolbar.setNavigationOnClickListener(v -> finish());
        tvCliente = findViewById(R.id.tvDetalleCliente);
        tvMeta = findViewById(R.id.tvDetalleMeta);
        tvTotal = findViewById(R.id.tvDetalleTotal);
        RecyclerView recyclerView = findViewById(R.id.recyclerDetalleVenta);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        int ventaId = getIntent().getIntExtra("venta_id", 0);
        if (ventaId <= 0) {
            Toast.makeText(this, "Venta inválida.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        cargarDetalle(ventaId);
    }

    private void cargarDetalle(int ventaId) {
        ApiClientFactory.create(this).obtenerVenta(ventaId).enqueue(new Callback<VentaDetalle>() {
            @Override
            public void onResponse(Call<VentaDetalle> call, Response<VentaDetalle> response) {
                if (!response.isSuccessful() || response.body() == null) {
                    Toast.makeText(DetalleVentaActivity.this, ApiErrorUtils.readError(response), Toast.LENGTH_LONG).show();
                    return;
                }
                VentaDetalle detalle = response.body();
                tvCliente.setText("Cliente: " + FormatUtils.safe(detalle.getClienteNombre()));
                tvMeta.setText("Fecha: " + FormatUtils.compactDate(detalle.getFecha()) + " • Vendedor: " + FormatUtils.safe(detalle.getVendedorNombre()));
                tvTotal.setText("Total: " + FormatUtils.currency(detalle.getTotal()));
                adapter.setItems(detalle.getItems());
            }

            @Override
            public void onFailure(Call<VentaDetalle> call, Throwable t) {
                Toast.makeText(DetalleVentaActivity.this, ApiErrorUtils.fromThrowable(t), Toast.LENGTH_LONG).show();
            }
        });
    }
}
