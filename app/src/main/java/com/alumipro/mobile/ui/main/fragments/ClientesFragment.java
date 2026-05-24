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
import com.alumipro.mobile.model.Cliente;
import com.alumipro.mobile.model.UsuarioSesion;
import com.alumipro.mobile.ui.adapters.ClienteAdapter;
import com.alumipro.mobile.ui.main.MainActivity;
import com.alumipro.mobile.utils.ApiErrorUtils;
import com.alumipro.mobile.utils.ValidationUtils;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClientesFragment extends Fragment implements ClienteAdapter.Listener {
    private final List<Cliente> allItems = new ArrayList<>();
    private ClienteAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView tvInfo;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_clientes, container, false);
        UsuarioSesion session = new SessionStore(requireContext()).get();
        adapter = new ClienteAdapter(this, session != null && session.isAdmin());
        RecyclerView recyclerView = view.findViewById(R.id.recyclerClientes);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);
        tvInfo = view.findViewById(R.id.tvClientesInfo);
        swipeRefreshLayout = view.findViewById(R.id.swipeClientes);
        swipeRefreshLayout.setOnRefreshListener(this::syncFromApi);
        FloatingActionButton fab = view.findViewById(R.id.fabCliente);
        fab.setOnClickListener(v -> openForm(null));
        SearchView searchView = view.findViewById(R.id.searchClientes);
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
            ((MainActivity) getActivity()).updateTitle("Clientes");
        }
        loadLocal();
    }

    private void loadLocal() {
        allItems.clear();
        allItems.addAll(new CacheStore(requireContext()).getClientes());
        adapter.setItems(allItems);
        tvInfo.setText("Clientes sincronizados: " + allItems.size());
    }

    private void syncFromApi() {
        swipeRefreshLayout.setRefreshing(true);
        ApiClientFactory.create(requireContext()).listarClientes().enqueue(new Callback<List<Cliente>>() {
            @Override
            public void onResponse(Call<List<Cliente>> call, Response<List<Cliente>> response) {
                swipeRefreshLayout.setRefreshing(false);
                if (!response.isSuccessful() || response.body() == null) {
                    Toast.makeText(requireContext(), ApiErrorUtils.readError(response), Toast.LENGTH_LONG).show();
                    return;
                }
                new CacheStore(requireContext()).replaceClientes(response.body());
                loadLocal();
            }

            @Override
            public void onFailure(Call<List<Cliente>> call, Throwable t) {
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
        List<Cliente> filtered = new ArrayList<>();
        for (Cliente item : allItems) {
            if ((item.getNombre() != null && item.getNombre().toLowerCase(Locale.ROOT).contains(value)) ||
                    (item.getEmail() != null && item.getEmail().toLowerCase(Locale.ROOT).contains(value))) {
                filtered.add(item);
            }
        }
        adapter.setItems(filtered);
    }

    private void openForm(@Nullable Cliente editable) {
        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_cliente_form, null, false);
        TextInputEditText etNombre = dialogView.findViewById(R.id.etClienteNombre);
        TextInputEditText etTelefono = dialogView.findViewById(R.id.etClienteTelefono);
        TextInputEditText etEmail = dialogView.findViewById(R.id.etClienteEmail);
        TextInputEditText etDireccion = dialogView.findViewById(R.id.etClienteDireccion);

        if (editable != null) {
            etNombre.setText(editable.getNombre());
            etTelefono.setText(editable.getTelefono());
            etEmail.setText(editable.getEmail());
            etDireccion.setText(editable.getDireccion());
        }

        AlertDialog dialog = new MaterialAlertDialogBuilder(requireContext())
                .setTitle(editable == null ? getString(R.string.crear_cliente) : "Editar cliente")
                .setView(dialogView)
                .setNegativeButton(R.string.cancelar, (d, which) -> d.dismiss())
                .setPositiveButton(R.string.guardar, null)
                .create();
        dialog.setOnShowListener(d -> dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            String nombre = valueOf(etNombre);
            String telefono = valueOf(etTelefono);
            String email = valueOf(etEmail);
            String direccion = valueOf(etDireccion);

            if (nombre.isEmpty()) {
                etNombre.setError("El nombre es obligatorio.");
                return;
            }
            if (!email.isEmpty() && !ValidationUtils.isEmail(email)) {
                etEmail.setError("Correo inválido.");
                return;
            }

            Cliente payload = new Cliente();
            payload.setNombre(nombre);
            payload.setTelefono(telefono);
            payload.setEmail(email);
            payload.setDireccion(direccion);

            ApiService api = ApiClientFactory.create(requireContext());
            Callback<Cliente> callback = new Callback<Cliente>() {
                @Override public void onResponse(Call<Cliente> call, Response<Cliente> response) {
                    if (!response.isSuccessful()) {
                        Toast.makeText(requireContext(), ApiErrorUtils.readError(response), Toast.LENGTH_LONG).show();
                        return;
                    }
                    dialog.dismiss();
                    syncFromApi();
                }
                @Override public void onFailure(Call<Cliente> call, Throwable t) {
                    Toast.makeText(requireContext(), ApiErrorUtils.fromThrowable(t), Toast.LENGTH_LONG).show();
                }
            };

            if (editable == null) {
                api.crearCliente(payload).enqueue(callback);
            } else {
                api.actualizarCliente(editable.getId(), payload).enqueue(callback);
            }
        }));
        dialog.show();
    }

    @Override
    public void onEdit(Cliente cliente) {
        openForm(cliente);
    }

    @Override
    public void onDelete(Cliente cliente) {
        new MaterialAlertDialogBuilder(requireContext())
                .setTitle("Eliminar cliente")
                .setMessage("¿Deseas eliminar a " + cliente.getNombre() + "?")
                .setNegativeButton(R.string.cancelar, null)
                .setPositiveButton(R.string.eliminar, (dialog, which) -> ApiClientFactory.create(requireContext())
                        .eliminarCliente(cliente.getId())
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
