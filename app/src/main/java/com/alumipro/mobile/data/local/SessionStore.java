package com.alumipro.mobile.data.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.alumipro.mobile.model.LoginResponse;
import com.alumipro.mobile.model.UsuarioSesion;

public class SessionStore {
    private final AlumiproDbHelper dbHelper;

    public SessionStore(Context context) {
        this.dbHelper = new AlumiproDbHelper(context.getApplicationContext());
    }

    public void save(LoginResponse response) {
        if (response == null || response.getUser() == null) {
            return;
        }
        ContentValues values = new ContentValues();
        values.put("id", 1);
        values.put("token", response.getToken());
        values.put("user_id", response.getUser().getId());
        values.put("nombre", response.getUser().getNombre());
        values.put("correo", response.getUser().getCorreo());
        values.put("rol", response.getUser().getRol());
        values.put("updated_at", String.valueOf(System.currentTimeMillis()));
        dbHelper.getWritableDatabase().insertWithOnConflict("session", null, values, SQLiteDatabase.CONFLICT_REPLACE);
    }

    public UsuarioSesion get() {
        Cursor cursor = dbHelper.getReadableDatabase().rawQuery("SELECT token, user_id, nombre, correo, rol FROM session WHERE id = 1", null);
        try {
            if (!cursor.moveToFirst()) {
                return null;
            }
            return new UsuarioSesion(
                    cursor.getInt(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(0)
            );
        } finally {
            cursor.close();
        }
    }

    public boolean hasSession() {
        return get() != null;
    }

    public void clear() {
        dbHelper.getWritableDatabase().delete("session", null, null);
    }
}
