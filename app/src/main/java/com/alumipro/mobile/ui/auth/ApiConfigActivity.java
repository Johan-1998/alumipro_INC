package com.alumipro.mobile.ui.auth;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.alumipro.mobile.R;
import com.alumipro.mobile.data.local.ConfigStore;
import com.alumipro.mobile.utils.ValidationUtils;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class ApiConfigActivity extends AppCompatActivity {
    private TextInputEditText etApiBaseUrl;
    private ConfigStore configStore;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_api_config);
        configStore = new ConfigStore(this);
        etApiBaseUrl = findViewById(R.id.etApiBaseUrl);
        MaterialButton btnGuardar = findViewById(R.id.btnGuardarApi);
        MaterialButton btnRestablecer = findViewById(R.id.btnRestablecerApi);
        MaterialToolbar toolbar = findViewById(R.id.toolbarConfig);
        toolbar.setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material);
        toolbar.setNavigationOnClickListener(v -> finish());
        etApiBaseUrl.setText(configStore.getApiBaseUrl());

        btnGuardar.setOnClickListener(v -> save());
        btnRestablecer.setOnClickListener(v -> {
            configStore.reset();
            etApiBaseUrl.setText(configStore.getApiBaseUrl());
            Toast.makeText(this, "Se restableció la URL por defecto.", Toast.LENGTH_SHORT).show();
        });
    }

    private void save() {
        String baseUrl = etApiBaseUrl.getText() == null ? "" : etApiBaseUrl.getText().toString();
        baseUrl = ValidationUtils.normalizeBaseUrl(baseUrl);
        if (baseUrl.isEmpty()) {
            etApiBaseUrl.setError("La URL es obligatoria.");
            return;
        }
        configStore.saveApiBaseUrl(baseUrl);
        Toast.makeText(this, "URL guardada correctamente.", Toast.LENGTH_SHORT).show();
        finish();
    }
}
