package com.alumipro.mobile.ui.main.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.alumipro.mobile.R;
import com.alumipro.mobile.data.local.CacheStore;
import com.alumipro.mobile.data.remote.ApiClientFactory;
import com.alumipro.mobile.model.VentaResumen;
import com.alumipro.mobile.ui.adapters.VentaAdapter;
import com.alumipro.mobile.ui.main.MainActivity;
import com.alumipro.mobile.ui.ventas.DetalleVentaActivity;
import com.alumipro.mobile.ui.ventas.NuevaVentaActivity;
import com.alumipro.mobile.utils.ApiErrorUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VentasFragment extends Fragment implements VentaAdapter.Listener {
    private VentaAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView tvInfo;
    private final ActivityResultLauncher<Intent> nuevaVentaLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> syncFromApi()
    );

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ventas, container, false);
        adapter = new VentaAdapter(this);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerVentas);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);
        tvInfo = view.findViewById(R.id.tvVentasInfo);
        swipeRefreshLayout = view.findViewById(R.id.swipeVentas);
        swipeRefreshLayout.setOnRefreshListener(this::syncFromApi);
        view.findViewById(R.id.btnNuevaVenta).setOnClickListener(v -> nuevaVentaLauncher.launch(new Intent(requireContext(), NuevaVentaActivity.class)));
        loadLocal();
        syncFromApi();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).updateTitle("Ventas");
        }
        loadLocal();
    }

    private void loadLocal() {
        List<VentaResumen> ventas = new CacheStore(requireContext()).getVentas();
        adapter.setItems(ventas);
        tvInfo.setText("Ventas disponibles: " + ventas.size());
    }

    private void syncFromApi() {
        swipeRefreshLayout.setRefreshing(true);
        ApiClientFactory.create(requireContext()).listarVentas().enqueue(new Callback<List<VentaResumen>>() {
            @Override
            public void onResponse(Call<List<VentaResumen>> call, Response<List<VentaResumen>> response) {
                swipeRefreshLayout.setRefreshing(false);
                if (!response.isSuccessful() || response.body() == null) {
                    Toast.makeText(requireContext(), ApiErrorUtils.readError(response), Toast.LENGTH_LONG).show();
                    return;
                }
                new CacheStore(requireContext()).replaceVentas(response.body());
                loadLocal();
            }

            @Override
            public void onFailure(Call<List<VentaResumen>> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(requireContext(), ApiErrorUtils.fromThrowable(t), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onOpen(VentaResumen venta) {
        Intent intent = new Intent(requireContext(), DetalleVentaActivity.class);
        intent.putExtra("venta_id", venta.getId());
        startActivity(intent);
    }
}
