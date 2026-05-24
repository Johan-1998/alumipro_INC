package com.alumipro.mobile.ui.main;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.alumipro.mobile.R;
import com.alumipro.mobile.data.local.SessionStore;
import com.alumipro.mobile.realtime.RealtimeNotificationManager;
import com.alumipro.mobile.ui.auth.ApiConfigActivity;
import com.alumipro.mobile.ui.auth.LoginActivity;
import com.alumipro.mobile.ui.main.fragments.AyudaFragment;
import com.alumipro.mobile.ui.main.fragments.ClientesFragment;
import com.alumipro.mobile.ui.main.fragments.DashboardFragment;
import com.alumipro.mobile.ui.main.fragments.ProductosFragment;
import com.alumipro.mobile.ui.main.fragments.VentasFragment;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private MaterialToolbar toolbar;
    private BottomNavigationView bottomNavigation;
    private SessionStore sessionStore;
    private RealtimeNotificationManager realtimeManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sessionStore = new SessionStore(this);
        if (!sessionStore.hasSession()) {
            openLogin();
            finish();
            return;
        }

        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbarMain);
        bottomNavigation = findViewById(R.id.bottomNavigation);
        realtimeManager = RealtimeNotificationManager.getInstance(getApplicationContext());

        setupToolbar();
        setupBottomNavigation();

        if (savedInstanceState == null) {
            bottomNavigation.setSelectedItemId(R.id.nav_dashboard);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (realtimeManager != null) {
            realtimeManager.start();
        }
    }

    @Override
    protected void onStop() {
        if (realtimeManager != null) {
            realtimeManager.stop();
        }
        super.onStop();
    }

    private void setupToolbar() {
        if (toolbar == null) {
            return;
        }
        toolbar.setTitle("Inicio");
        toolbar.setOnMenuItemClickListener(item -> handleToolbarAction(item.getItemId()));
    }

    private boolean handleToolbarAction(int itemId) {
        if (itemId == R.id.action_config_api) {
            startActivity(new Intent(this, ApiConfigActivity.class));
            return true;
        }
        if (itemId == R.id.action_logout) {
            cerrarSesion();
            return true;
        }
        return false;
    }

    private void setupBottomNavigation() {
        if (bottomNavigation == null) {
            return;
        }
        bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_dashboard) {
                openFragment(new DashboardFragment());
                updateTitle("Inicio");
                return true;
            }
            if (itemId == R.id.nav_clientes) {
                openFragment(new ClientesFragment());
                updateTitle("Clientes");
                return true;
            }
            if (itemId == R.id.nav_productos) {
                openFragment(new ProductosFragment());
                updateTitle("Productos");
                return true;
            }
            if (itemId == R.id.nav_ventas) {
                openFragment(new VentasFragment());
                updateTitle("Ventas");
                return true;
            }
            if (itemId == R.id.nav_ayuda) {
                openFragment(new AyudaFragment());
                updateTitle("Ayuda");
                return true;
            }
            return false;
        });
    }

    private void openFragment(@NonNull Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit();
    }

    public void updateTitle(@NonNull String title) {
        if (toolbar != null) {
            toolbar.setTitle(title);
        }
        setTitle(title);
    }

    private void cerrarSesion() {
        if (realtimeManager != null) {
            realtimeManager.stop();
        }
        sessionStore.clear();
        openLogin();
        finish();
    }

    private void openLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
