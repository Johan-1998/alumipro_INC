package com.alumipro.mobile.data.remote;

import android.content.Context;

import androidx.annotation.NonNull;

import com.alumipro.mobile.data.local.SessionStore;
import com.alumipro.mobile.model.UsuarioSesion;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor {
    private final Context context;

    public AuthInterceptor(Context context) {
        this.context = context.getApplicationContext();
    }

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request original = chain.request();
        UsuarioSesion session = new SessionStore(context).get();
        if (session == null || session.getToken() == null || session.getToken().trim().isEmpty()) {
            return chain.proceed(original);
        }
        Request request = original.newBuilder()
                .header("Authorization", "Bearer " + session.getToken())
                .build();
        return chain.proceed(request);
    }
}
