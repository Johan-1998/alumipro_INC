package com.alumipro.mobile.ui.main.fragments;

import androidx.appcompat.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.alumipro.mobile.R;
import com.alumipro.mobile.data.local.CacheStore;
import com.alumipro.mobile.data.local.SessionStore;
import com.alumipro.mobile.data.remote.ApiClientFactory;
import com.alumipro.mobile.data.remote.ApiService;
import com.alumipro.mobile.model.Producto;
import com.alumipro.mobile.model.UsuarioSesion;
import com.alumipro.mobile.ui.adapters.ProductoAdapter;
import com.alumipro.mobile.ui.main.MainActivity;
import com.alumipro.mobile.utils.ApiErrorUtils;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductosFragment extends Fragment implements ProductoAdapter.Listener {
    private final List<Producto> allItems = new ArrayList<>();
    private ProductoAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView tvInfo;
    private boolean canManage;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_productos, container, false);
        UsuarioSesion session = new SessionStore(requireContext()).get();
        canManage = session != null && session.isAdmin();
        adapter = new ProductoAdapter(this, canManage);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerProductos);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);
        tvInfo = view.findViewById(R.id.tvProductosInfo);
        swipeRefreshLayout = view.findViewById(R.id.swipeProductos);
        swipeRefreshLayout.setOnRefreshListener(this::syncFromApi);
        FloatingActionButton fab = view.findViewById(R.id.fabProducto);
        fab.setVisibility(canManage ? View.VISIBLE : View.GONE);
        fab.setOnClickListener(v -> openForm(null));
        SearchView searchView = view.findViewById(R.id.searchProductos);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override public boolean onQueryTextSubmit(String query) { filter(query); return true; }
            @Override public boolean onQueryTextChange(String newText) { filter(newText); return true; }
        });
        loadLocal();
        syncFromApi();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).updateTitle("Productos");
        }
        loadLocal();
    }

    private void loadLocal() {
        allItems.clear();
        allItems.addAll(new CacheStore(requireContext()).getProductos());
        adapter.setItems(allItems);
        tvInfo.setText(canManage ? "Gestión completa para rol ADMIN." : "Consulta habilitada para el rol actual.");
    }

    private void syncFromApi() {
        swipeRefreshLayout.setRefreshing(true);
        ApiClientFactory.create(requireContext()).listarProductos().enqueue(new Callback<List<Producto>>() {
            @Override
            public void onResponse(Call<List<Producto>> call, Response<List<Producto>> response) {
                swipeRefreshLayout.setRefreshing(false);
                if (!response.isSuccessful() || response.body() == null) {
                    Toast.makeText(requireContext(), ApiErrorUtils.readError(response), Toast.LENGTH_LONG).show();
                    return;
                }
                new CacheStore(requireContext()).replaceProductos(response.body());
                loadLocal();
            }

            @Override
            public void onFailure(Call<List<Producto>> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(requireContext(), ApiErrorUtils.fromThrowable(t), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void filter(String query) {
        String value = query == null ? "" : query.trim().toLowerCase(Locale.ROOT);
        if (value.isEmpty()) {
            adapter.setItems(allItems);
            return;
        }
        List<Producto> filtered = new ArrayList<>();
        for (Producto item : allItems) {
            if ((item.getNombre() != null && item.getNombre().toLowerCase(Locale.ROOT).contains(value)) ||
                    (item.getDescripcion() != null && item.getDescripcion().toLowerCase(Locale.ROOT).contains(value))) {
                filtered.add(item);
            }
        }
        adapter.setItems(filtered);
    }

    private void openForm(@Nullable Producto editable) {
        if (!canManage) {
            return;
        }
        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_producto_form, null, false);
        TextInputEditText etNombre = dialogView.findViewById(R.id.etProductoNombre);
        TextInputEditText etPrecio = dialogView.findViewById(R.id.etProductoPrecio);
        TextInputEditText etStock = dialogView.findViewById(R.id.etProductoStock);
        TextInputEditText etDescripcion = dialogView.findViewById(R.id.etProductoDescripcion);

        if (editable != null) {
            etNombre.setText(editable.getNombre());
            etPrecio.setText(String.valueOf(editable.getPrecio()));
            etStock.setText(String.valueOf(editable.getStock()));
            etDescripcion.setText(editable.getDescripcion());
        }

        AlertDialog dialog = new MaterialAlertDialogBuilder(requireContext())
                .setTitle(editable == null ? getString(R.string.crear_producto) : "Editar producto")
                .setView(dialogView)
                .setNegativeButton(R.string.cancelar, (d, which) -> d.dismiss())
                .setPositiveButton(R.string.guardar, null)
                .create();
        dialog.setOnShowListener(d -> dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            String nombre = valueOf(etNombre);
            String precioText = valueOf(etPrecio);
            String stockText = valueOf(etStock);
            String descripcion = valueOf(etDescripcion);
            if (nombre.isEmpty()) {
                etNombre.setError("El nombre es obligatorio.");
                return;
            }
            double precio;
            int stock;
            try {
                precio = Double.parseDouble(precioText);
                stock = Integer.parseInt(stockText);
            } catch (Exception ex) {
                Toast.makeText(requireContext(), "Precio y stock deben ser numéricos.", Toast.LENGTH_LONG).show();
                return;
            }
            Producto payload = new Producto();
            payload.setNombre(nombre);
            payload.setPrecio(precio);
            payload.setStock(stock);
            payload.setDescripcion(descripcion);
            ApiService api = ApiClientFactory.create(requireContext());
            Callback<Producto> callback = new Callback<Producto>() {
                @Override public void onResponse(Call<Producto> call, Response<Producto> response) {
                    if (!response.isSuccessful()) {
                        Toast.makeText(requireContext(), ApiErrorUtils.readError(response), Toast.LENGTH_LONG).show();
                        return;
                    }
                    dialog.dismiss();
                    syncFromApi();
                }
                @Override public void onFailure(Call<Producto> call, Throwable t) {
                    Toast.makeText(requireContext(), ApiErrorUtils.fromThrowable(t), Toast.LENGTH_LONG).show();
                }
            };
            if (editable == null) {
                api.crearProducto(payload).enqueue(callback);
            } else {
                api.actualizarProducto(editable.getId(), payload).enqueue(callback);
            }
        }));
        dialog.show();
    }

    @Override
    public void onEdit(Producto producto) {
        openForm(producto);
    }

    @Override
    public void onDelete(Producto producto) {
        if (!canManage) {
            return;
        }
        new MaterialAlertDialogBuilder(requireContext())
                .setTitle("Eliminar producto")
                .setMessage("¿Deseas eliminar " + producto.getNombre() + "?")
                .setNegativeButton(R.string.cancelar, null)
                .setPositiveButton(R.string.eliminar, (dialog, which) -> ApiClientFactory.create(requireContext())
                        .eliminarProducto(producto.getId())
                        .enqueue(new Callback<Void>() {
                            @Override public void onResponse(Call<Void> call, Response<Void> response) {
                                if (!response.isSuccessful()) {
                                    Toast.makeText(requireContext(), ApiErrorUtils.readError(response), Toast.LENGTH_LONG).show();
                                    return;
                                }
                                syncFromApi();
                            }
                            @Override public void onFailure(Call<Void> call, Throwable t) {
                                Toast.makeText(requireContext(), ApiErrorUtils.fromThrowable(t), Toast.LENGTH_LONG).show();
                            }
                        }))
                .show();
    }

    private String valueOf(TextInputEditText editText) {
        return editText.getText() == null ? "" : editText.getText().toString().trim();
    }
}
