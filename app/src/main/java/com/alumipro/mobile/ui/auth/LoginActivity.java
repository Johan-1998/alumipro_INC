package com.alumipro.mobile.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.alumipro.mobile.R;
import com.alumipro.mobile.data.local.ConfigStore;
import com.alumipro.mobile.data.local.SessionStore;
import com.alumipro.mobile.data.remote.ApiClientFactory;
import com.alumipro.mobile.data.remote.ApiService;
import com.alumipro.mobile.model.LoginRequest;
import com.alumipro.mobile.model.LoginResponse;
import com.alumipro.mobile.ui.main.MainActivity;
import com.alumipro.mobile.utils.ApiErrorUtils;
import com.alumipro.mobile.utils.ValidationUtils;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private TextInputEditText etCorreo;
    private TextInputEditText etPassword;
    private TextView tvApiActual;
    private MaterialButton btnIngresar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (new SessionStore(this).hasSession()) {
            openMain();
            return;
        }
        setContentView(R.layout.activity_login);
        etCorreo = findViewById(R.id.etCorreo);
        etPassword = findViewById(R.id.etPassword);
        tvApiActual = findViewById(R.id.tvApiActual);
        btnIngresar = findViewById(R.id.btnIngresar);
        MaterialButton btnConfigApi = findViewById(R.id.btnConfigApi);

        btnIngresar.setOnClickListener(v -> attemptLogin());
        btnConfigApi.setOnClickListener(v -> startActivity(new Intent(this, ApiConfigActivity.class)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        tvApiActual.setText("API actual: " + new ConfigStore(this).getApiBaseUrl());
    }

    private void attemptLogin() {
        String correo = valueOf(etCorreo);
        String password = valueOf(etPassword);

        if (correo.isEmpty()) {
            etCorreo.setError("El correo es obligatorio.");
            etCorreo.requestFocus();
            return;
        }
        if (!ValidationUtils.isEmail(correo)) {
            etCorreo.setError("Correo inválido.");
            etCorreo.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            etPassword.setError("La contraseña es obligatoria.");
            etPassword.requestFocus();
            return;
        }

        btnIngresar.setEnabled(false);
        ApiService apiService = ApiClientFactory.create(this);
        apiService.login(new LoginRequest(correo, password)).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                btnIngresar.setEnabled(true);
                if (!response.isSuccessful() || response.body() == null) {
                    Toast.makeText(LoginActivity.this, ApiErrorUtils.readError(response), Toast.LENGTH_LONG).show();
                    return;
                }
                new SessionStore(LoginActivity.this).save(response.body());
                Toast.makeText(LoginActivity.this, "Bienvenido, " + response.body().getUser().getNombre() + ".", Toast.LENGTH_SHORT).show();
                openMain();
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                btnIngresar.setEnabled(true);
                Toast.makeText(LoginActivity.this, ApiErrorUtils.fromThrowable(t), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void openMain() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private String valueOf(TextInputEditText editText) {
        return editText.getText() == null ? "" : editText.getText().toString().trim();
    }
}
