package com.alumipro.mobile.realtime;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.alumipro.mobile.data.local.ConfigStore;
import com.alumipro.mobile.data.local.NotificacionStore;
import com.alumipro.mobile.data.local.SessionStore;
import com.alumipro.mobile.model.NotificacionMovil;
import com.alumipro.mobile.model.UsuarioSesion;
import com.google.gson.Gson;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import okhttp3.sse.EventSources;

public class RealtimeNotificationManager {
    public static final String ACTION_NOTIFICACION_RECIBIDA = "com.alumipro.mobile.ACTION_NOTIFICACION_RECIBIDA";
    private static RealtimeNotificationManager instance;

    private final Context context;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private final Gson gson = new Gson();
    private EventSource eventSource;
    private boolean started;

    private RealtimeNotificationManager(Context context) {
        this.context = context.getApplicationContext();
    }

    public static synchronized RealtimeNotificationManager getInstance(Context context) {
        if (instance == null) {
            instance = new RealtimeNotificationManager(context);
        }
        return instance;
    }

    public void start() {
        if (started) {
            return;
        }
        UsuarioSesion session = new SessionStore(context).get();
        if (session == null || session.getToken() == null || session.getToken().trim().isEmpty()) {
            return;
        }
        started = true;
        connect(session.getToken());
    }

    public void stop() {
        started = false;
        if (eventSource != null) {
            eventSource.cancel();
            eventSource = null;
        }
        handler.removeCallbacksAndMessages(null);
    }

    private void connect(String token) {
        String baseUrl = new ConfigStore(context).getApiBaseUrl();
        if (!baseUrl.endsWith("/")) {
            baseUrl = baseUrl + "/";
        }
        String url = baseUrl + "api/movil/notificaciones/stream";

        OkHttpClient client = new OkHttpClient.Builder().build();
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Accept", "text/event-stream")
                .addHeader("Authorization", "Bearer " + token)
                .build();

        eventSource = EventSources.createFactory(client).newEventSource(request, new EventSourceListener() {
            @Override
            public void onEvent(EventSource eventSource, String id, String type, String data) {
                if (!"notificacion".equals(type) || data == null || data.trim().isEmpty()) {
                    return;
                }
                NotificacionMovil item = gson.fromJson(data, NotificacionMovil.class);
                if (item == null) {
                    return;
                }
                new NotificacionStore(context).add(item);
                LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent(ACTION_NOTIFICACION_RECIBIDA));
            }

            @Override
            public void onFailure(EventSource eventSource, Throwable t, Response response) {
                if (!started) {
                    return;
                }
                handler.postDelayed(() -> {
                    UsuarioSesion current = new SessionStore(context).get();
                    if (current != null && current.getToken() != null && !current.getToken().trim().isEmpty()) {
                        connect(current.getToken());
                    }
                }, 5000L);
            }
        });
    }
}
