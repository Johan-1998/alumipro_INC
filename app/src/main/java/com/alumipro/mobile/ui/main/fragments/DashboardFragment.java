package com.alumipro.mobile.ui.main.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.alumipro.mobile.R;
import com.alumipro.mobile.data.local.CacheStore;
import com.alumipro.mobile.data.local.ConfigStore;
import com.alumipro.mobile.data.local.SessionStore;
import com.alumipro.mobile.data.remote.ApiClientFactory;
import com.alumipro.mobile.data.remote.ApiService;
import com.alumipro.mobile.model.Cliente;
import com.alumipro.mobile.model.Producto;
import com.alumipro.mobile.model.UsuarioSesion;
import com.alumipro.mobile.model.VentaResumen;
import com.alumipro.mobile.ui.main.MainActivity;
import com.alumipro.mobile.utils.ApiErrorUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardFragment extends Fragment {
    private TextView tvSaludo;
    private TextView tvRolCorreo;
    private TextView tvApiInfo;
    private TextView tvCountClientes;
    private TextView tvCountProductos;
    private TextView tvCountVentas;
    private TextView tvUltimaSync;
    private CacheStore cacheStore;
    private String lastSync = "Sin sincronización reciente";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        cacheStore = new CacheStore(requireContext());
        tvSaludo = view.findViewById(R.id.tvSaludo);
        tvRolCorreo = view.findViewById(R.id.tvRolCorreo);
        tvApiInfo = view.findViewById(R.id.tvApiInfo);
        tvCountClientes = view.findViewById(R.id.tvCountClientes);
        tvCountProductos = view.findViewById(R.id.tvCountProductos);
        tvCountVentas = view.findViewById(R.id.tvCountVentas);
        tvUltimaSync = view.findViewById(R.id.tvUltimaSync);
        view.findViewById(R.id.btnSyncDashboard).setOnClickListener(v -> syncAll());
        loadLocal();
        syncAll();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).updateTitle("Inicio");
        }
        loadLocal();
    }

    private void loadLocal() {
        UsuarioSesion session = new SessionStore(requireContext()).get();
        if (session != null) {
            tvSaludo.setText("Bienvenido, " + session.getNombre());
            tvRolCorreo.setText(session.getRol() + " • " + session.getCorreo());
        }
        tvApiInfo.setText("API: " + new ConfigStore(requireContext()).getApiBaseUrl());
        tvCountClientes.setText(String.valueOf(cacheStore.countClientes()));
        tvCountProductos.setText(String.valueOf(cacheStore.countProductos()));
        tvCountVentas.setText(String.valueOf(cacheStore.countVentas()));
        tvUltimaSync.setText(lastSync);
    }

    private void syncAll() {
        ApiService api = ApiClientFactory.create(requireContext());
        api.listarClientes().enqueue(new Callback<List<Cliente>>() {
            @Override public void onResponse(Call<List<Cliente>> call, Response<List<Cliente>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    cacheStore.replaceClientes(response.body());
                    markSync();
                }
            }
            @Override public void onFailure(Call<List<Cliente>> call, Throwable t) { }
        });
        api.listarProductos().enqueue(new Callback<List<Producto>>() {
            @Override public void onResponse(Call<List<Producto>> call, Response<List<Producto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    cacheStore.replaceProductos(response.body());
                    markSync();
                }
            }
            @Override public void onFailure(Call<List<Producto>> call, Throwable t) { }
        });
        api.listarVentas().enqueue(new Callback<List<VentaResumen>>() {
            @Override public void onResponse(Call<List<VentaResumen>> call, Response<List<VentaResumen>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    cacheStore.replaceVentas(response.body());
                    markSync();
                } else {
                    Toast.makeText(requireContext(), ApiErrorUtils.readError(response), Toast.LENGTH_SHORT).show();
                }
            }
            @Override public void onFailure(Call<List<VentaResumen>> call, Throwable t) {
                Toast.makeText(requireContext(), ApiErrorUtils.fromThrowable(t), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void markSync() {
        lastSync = "Última sincronización: " + android.text.format.DateFormat.format("dd/MM/yyyy HH:mm", System.currentTimeMillis());
        if (isAdded()) {
            loadLocal();
        }
    }
}
