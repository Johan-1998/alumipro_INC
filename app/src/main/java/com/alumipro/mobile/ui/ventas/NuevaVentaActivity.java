package com.alumipro.mobile.ui.ventas;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import android.text.Editable;
import android.text.TextWatcher;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.alumipro.mobile.R;
import com.alumipro.mobile.data.local.CacheStore;
import com.alumipro.mobile.data.remote.ApiClientFactory;
import com.alumipro.mobile.data.remote.ApiService;
import com.alumipro.mobile.model.Cliente;
import com.alumipro.mobile.model.Producto;
import com.alumipro.mobile.model.VentaCreateRequest;
import com.alumipro.mobile.model.VentaCreateResponse;
import com.alumipro.mobile.utils.ApiErrorUtils;
import com.alumipro.mobile.utils.FormatUtils;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NuevaVentaActivity extends AppCompatActivity {
    private final List<Cliente> clientes = new ArrayList<>();
    private final List<Producto> productos = new ArrayList<>();
    private final List<ItemRowHolder> itemRows = new ArrayList<>();
    private Spinner spinnerCliente;
    private LinearLayout containerItems;
    private TextView tvResumen;
    private MaterialButton btnRegistrar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nueva_venta);

        MaterialToolbar toolbar = findViewById(R.id.toolbarVenta);
        toolbar.setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material);
        toolbar.setNavigationOnClickListener(v -> finish());
        spinnerCliente = findViewById(R.id.spinnerCliente);
        containerItems = findViewById(R.id.containerItemsVenta);
        tvResumen = findViewById(R.id.tvResumenVenta);
        btnRegistrar = findViewById(R.id.btnRegistrarVenta);
        findViewById(R.id.btnAgregarItem).setOnClickListener(v -> addItemRow());
        btnRegistrar.setOnClickListener(v -> registrarVenta());

        loadCachedData();
        refreshRemoteData();
    }

    private void loadCachedData() {
        CacheStore cacheStore = new CacheStore(this);
        clientes.clear();
        productos.clear();

        Cliente placeholderCliente = new Cliente();
        placeholderCliente.setId(0);
        placeholderCliente.setNombre("Seleccione un cliente");
        clientes.add(placeholderCliente);
        clientes.addAll(cacheStore.getClientes());

        Producto placeholderProducto = new Producto();
        placeholderProducto.setId(0);
        placeholderProducto.setNombre("Seleccione un producto");
        productos.add(placeholderProducto);
        productos.addAll(cacheStore.getProductos());

        spinnerCliente.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, clientes));

        if (itemRows.isEmpty()) {
            addItemRow();
        } else {
            rebindItemSpinners();
        }
        updateResumen();
    }

    private void refreshRemoteData() {
        ApiService api = ApiClientFactory.create(this);
        api.listarClientes().enqueue(new Callback<List<Cliente>>() {
            @Override public void onResponse(Call<List<Cliente>> call, Response<List<Cliente>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    new CacheStore(NuevaVentaActivity.this).replaceClientes(response.body());
                    loadCachedData();
                }
            }
            @Override public void onFailure(Call<List<Cliente>> call, Throwable t) { }
        });
        api.listarProductos().enqueue(new Callback<List<Producto>>() {
            @Override public void onResponse(Call<List<Producto>> call, Response<List<Producto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    new CacheStore(NuevaVentaActivity.this).replaceProductos(response.body());
                    loadCachedData();
                }
            }
            @Override public void onFailure(Call<List<Producto>> call, Throwable t) { }
        });
    }

    private void addItemRow() {
        View row = LayoutInflater.from(this).inflate(R.layout.row_venta_item_editor, containerItems, false);
        Spinner spinnerProducto = row.findViewById(R.id.spinnerProductoItem);
        EditText etCantidad = row.findViewById(R.id.etCantidadItem);
        TextView tvInfo = row.findViewById(R.id.tvItemInfo);
        ImageButton btnQuitar = row.findViewById(R.id.btnQuitarItem);

        spinnerProducto.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, productos));
        spinnerProducto.setOnItemSelectedListener(new SimpleItemSelectedListener() {
            @Override public void onItemSelected(int position) {
                updateItemInfo(spinnerProducto, tvInfo, etCantidad);
                updateResumen();
            }
        });
        etCantidad.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateItemInfo(spinnerProducto, tvInfo, etCantidad);
                updateResumen();
            }
            @Override public void afterTextChanged(Editable s) { }
        });
        etCantidad.setOnFocusChangeListener((v, hasFocus) -> updateResumen());
        btnQuitar.setOnClickListener(v -> {
            containerItems.removeView(row);
            itemRows.removeIf(holder -> holder.root == row);
            if (itemRows.isEmpty()) {
                addItemRow();
            }
            updateResumen();
        });

        ItemRowHolder holder = new ItemRowHolder(row, spinnerProducto, etCantidad, tvInfo);
        itemRows.add(holder);
        containerItems.addView(row);
        updateItemInfo(spinnerProducto, tvInfo, etCantidad);
        updateResumen();
    }

    private void rebindItemSpinners() {
        for (ItemRowHolder holder : itemRows) {
            holder.spinnerProducto.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, productos));
            updateItemInfo(holder.spinnerProducto, holder.tvInfo, holder.etCantidad);
        }
    }

    private void updateItemInfo(Spinner spinnerProducto, TextView tvInfo, EditText etCantidad) {
        Producto producto = (Producto) spinnerProducto.getSelectedItem();
        if (producto == null || producto.getId() <= 0) {
            tvInfo.setText("Selecciona un producto para ver precio y stock.");
            return;
        }
        int cantidad = safeInt(etCantidad.getText() == null ? "1" : etCantidad.getText().toString());
        double subtotal = producto.getPrecio() * Math.max(cantidad, 1);
        tvInfo.setText("Precio: " + FormatUtils.currency(producto.getPrecio()) + " • Stock: " + producto.getStock() + " • Subtotal estimado: " + FormatUtils.currency(subtotal));
    }

    private void updateResumen() {
        double total = 0;
        int validItems = 0;
        for (ItemRowHolder holder : itemRows) {
            Producto producto = (Producto) holder.spinnerProducto.getSelectedItem();
            if (producto != null && producto.getId() > 0) {
                int cantidad = safeInt(holder.etCantidad.getText() == null ? "0" : holder.etCantidad.getText().toString());
                if (cantidad > 0) {
                    validItems++;
                    total += producto.getPrecio() * cantidad;
                }
            }
        }
        tvResumen.setText("Items válidos: " + validItems + " • Total estimado: " + FormatUtils.currency(total));
    }

    private void registrarVenta() {
        Cliente cliente = (Cliente) spinnerCliente.getSelectedItem();
        if (cliente == null || cliente.getId() <= 0) {
            Toast.makeText(this, "Debes seleccionar un cliente.", Toast.LENGTH_LONG).show();
            return;
        }

        VentaCreateRequest request = new VentaCreateRequest();
        request.setClienteId(cliente.getId());
        List<VentaCreateRequest.ItemVentaRequest> items = new ArrayList<>();

        for (ItemRowHolder holder : itemRows) {
            Producto producto = (Producto) holder.spinnerProducto.getSelectedItem();
            int cantidad = safeInt(holder.etCantidad.getText() == null ? "0" : holder.etCantidad.getText().toString());
            if (producto == null || producto.getId() <= 0 || cantidad <= 0) {
                continue;
            }
            items.add(new VentaCreateRequest.ItemVentaRequest(producto.getId(), cantidad));
        }

        if (items.isEmpty()) {
            Toast.makeText(this, "Debes agregar al menos un producto válido.", Toast.LENGTH_LONG).show();
            return;
        }
        request.setItems(items);
        btnRegistrar.setEnabled(false);
        ApiClientFactory.create(this).crearVenta(request).enqueue(new Callback<VentaCreateResponse>() {
            @Override
            public void onResponse(Call<VentaCreateResponse> call, Response<VentaCreateResponse> response) {
                btnRegistrar.setEnabled(true);
                if (!response.isSuccessful() || response.body() == null) {
                    Toast.makeText(NuevaVentaActivity.this, ApiErrorUtils.readError(response), Toast.LENGTH_LONG).show();
                    return;
                }
                Toast.makeText(NuevaVentaActivity.this, "Venta registrada. Total: " + FormatUtils.currency(response.body().getTotal()), Toast.LENGTH_LONG).show();
                setResult(RESULT_OK, new Intent().putExtra("venta_id", response.body().getVentaId()));
                finish();
            }

            @Override
            public void onFailure(Call<VentaCreateResponse> call, Throwable t) {
                btnRegistrar.setEnabled(true);
                Toast.makeText(NuevaVentaActivity.this, ApiErrorUtils.fromThrowable(t), Toast.LENGTH_LONG).show();
            }
        });
    }

    private int safeInt(String value) {
        try {
            return Integer.parseInt(value.trim());
        } catch (Exception e) {
            return 0;
        }
    }

    private static class ItemRowHolder {
        final View root;
        final Spinner spinnerProducto;
        final EditText etCantidad;
        final TextView tvInfo;

        ItemRowHolder(View root, Spinner spinnerProducto, EditText etCantidad, TextView tvInfo) {
            this.root = root;
            this.spinnerProducto = spinnerProducto;
            this.etCantidad = etCantidad;
            this.tvInfo = tvInfo;
        }
    }
}
