package com.alumipro.mobile.data.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class AlumiproDbHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "alumipro_mobile.db";
    public static final int DB_VERSION = 1;

    public AlumiproDbHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE app_config (id INTEGER PRIMARY KEY CHECK (id = 1), api_base_url TEXT NOT NULL, updated_at TEXT)");
        db.execSQL("CREATE TABLE session (id INTEGER PRIMARY KEY CHECK (id = 1), token TEXT NOT NULL, user_id INTEGER, nombre TEXT, correo TEXT, rol TEXT, updated_at TEXT)");
        db.execSQL("CREATE TABLE clientes_cache (id INTEGER PRIMARY KEY, nombre TEXT NOT NULL, telefono TEXT, direccion TEXT, email TEXT, updated_at TEXT)");
        db.execSQL("CREATE TABLE productos_cache (id INTEGER PRIMARY KEY, nombre TEXT NOT NULL, precio REAL NOT NULL, stock INTEGER NOT NULL, descripcion TEXT, updated_at TEXT)");
        db.execSQL("CREATE TABLE ventas_cache (id INTEGER PRIMARY KEY, fecha TEXT, cliente_nombre TEXT, vendedor_nombre TEXT, total REAL NOT NULL, updated_at TEXT)");
        db.execSQL("CREATE TABLE notificaciones_cache (id INTEGER PRIMARY KEY, titulo TEXT NOT NULL, mensaje TEXT NOT NULL, tipo TEXT, created_at TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS app_config");
        db.execSQL("DROP TABLE IF EXISTS session");
        db.execSQL("DROP TABLE IF EXISTS clientes_cache");
        db.execSQL("DROP TABLE IF EXISTS productos_cache");
        db.execSQL("DROP TABLE IF EXISTS ventas_cache");
        db.execSQL("DROP TABLE IF EXISTS notificaciones_cache");
        onCreate(db);
    }
}
