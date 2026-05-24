package com.alumipro.mobile.ui.main.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alumipro.mobile.R;
import com.alumipro.mobile.data.local.CacheStore;
import com.alumipro.mobile.data.local.ConfigStore;
import com.alumipro.mobile.data.local.NotificacionStore;
import com.alumipro.mobile.data.remote.ApiClientFactory;
import com.alumipro.mobile.model.NotificacionMovil;
import com.alumipro.mobile.realtime.RealtimeNotificationManager;
import com.alumipro.mobile.ui.adapters.NotificacionAdapter;
import com.alumipro.mobile.ui.auth.ApiConfigActivity;
import com.alumipro.mobile.ui.main.MainActivity;
import com.google.android.material.button.MaterialButton;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AyudaFragment extends Fragment {
    private TextView tvApi;
    private TextView tvSqlite;
    private TextView tvRealtime;
    private NotificacionAdapter adapter;
    private MediaPlayer mediaPlayer;
    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            loadNotifications();
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ayuda, container, false);
        tvApi = view.findViewById(R.id.tvApiAyuda);
        tvSqlite = view.findViewById(R.id.tvSqliteInfo);
        tvRealtime = view.findViewById(R.id.tvRealtimeEstado);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerNotificaciones);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new NotificacionAdapter();
        recyclerView.setAdapter(adapter);

        MaterialButton btnConfig = view.findViewById(R.id.btnAbrirConfigApi);
        MaterialButton btnPlay = view.findViewById(R.id.btnPlayAudio);
        MaterialButton btnStop = view.findViewById(R.id.btnStopAudio);
        VideoView videoView = view.findViewById(R.id.videoTutorial);

        btnConfig.setOnClickListener(v -> startActivity(new Intent(requireContext(), ApiConfigActivity.class)));
        btnPlay.setOnClickListener(v -> playAudio());
        btnStop.setOnClickListener(v -> stopAudio());

        MediaController mediaController = new MediaController(requireContext());
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);
        Uri videoUri = Uri.parse("android.resource://" + requireContext().getPackageName() + "/" + R.raw.alumipro_tutorial);
        videoView.setVideoURI(videoUri);

        loadInfo();
        syncNotifications();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).updateTitle("Ayuda");
        }
        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(receiver, new IntentFilter(RealtimeNotificationManager.ACTION_NOTIFICACION_RECIBIDA));
        loadInfo();
        loadNotifications();
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(receiver);
    }

    @Override
    public void onDestroyView() {
        stopAudio();
        super.onDestroyView();
    }

    private void loadInfo() {
        CacheStore cacheStore = new CacheStore(requireContext());
        tvApi.setText("URL actual: " + new ConfigStore(requireContext()).getApiBaseUrl());
        tvSqlite.setText("Clientes: " + cacheStore.countClientes() + " • Productos: " + cacheStore.countProductos() + " • Ventas: " + cacheStore.countVentas());
        tvRealtime.setText("Canal SSE activo para notificaciones operativas persistidas en MySQL y cacheadas en SQLite.");
        loadNotifications();
    }

    private void loadNotifications() {
        List<NotificacionMovil> items = new NotificacionStore(requireContext()).getAll();
        adapter.setItems(items);
    }

    private void syncNotifications() {
        ApiClientFactory.create(requireContext()).listarNotificaciones().enqueue(new Callback<List<NotificacionMovil>>() {
            @Override
            public void onResponse(Call<List<NotificacionMovil>> call, Response<List<NotificacionMovil>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    new NotificacionStore(requireContext()).replace(response.body());
                    loadNotifications();
                }
            }

            @Override
            public void onFailure(Call<List<NotificacionMovil>> call, Throwable t) {
                Toast.makeText(requireContext(), "No se pudieron sincronizar las notificaciones.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void playAudio() {
        stopAudio();
        mediaPlayer = MediaPlayer.create(requireContext(), R.raw.alumipro_alert);
        if (mediaPlayer != null) {
            mediaPlayer.start();
        }
    }

    private void stopAudio() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
