package com.alumipro.mobile.data.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.alumipro.mobile.model.NotificacionMovil;

import java.util.ArrayList;
import java.util.List;

public class NotificacionStore {
    private final AlumiproDbHelper dbHelper;

    public NotificacionStore(Context context) {
        this.dbHelper = new AlumiproDbHelper(context.getApplicationContext());
    }

    public void replace(List<NotificacionMovil> items) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            db.delete("notificaciones_cache", null, null);
            for (NotificacionMovil item : items) {
                insert(db, item);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    public void add(NotificacionMovil item) {
        insert(dbHelper.getWritableDatabase(), item);
    }

    private void insert(SQLiteDatabase db, NotificacionMovil item) {
        ContentValues values = new ContentValues();
        values.put("id", item.getId());
        values.put("titulo", item.getTitulo());
        values.put("mensaje", item.getMensaje());
        values.put("tipo", item.getTipo());
        values.put("created_at", item.getCreatedAt());
        db.insertWithOnConflict("notificaciones_cache", null, values, SQLiteDatabase.CONFLICT_REPLACE);
    }

    public List<NotificacionMovil> getAll() {
        List<NotificacionMovil> items = new ArrayList<>();
        Cursor cursor = dbHelper.getReadableDatabase().rawQuery("SELECT id, titulo, mensaje, tipo, created_at FROM notificaciones_cache ORDER BY id DESC LIMIT 20", null);
        try {
            while (cursor.moveToNext()) {
                NotificacionMovil item = new NotificacionMovil();
                item.setId(cursor.getLong(0));
                item.setTitulo(cursor.getString(1));
                item.setMensaje(cursor.getString(2));
                item.setTipo(cursor.getString(3));
                item.setCreatedAt(cursor.getString(4));
                items.add(item);
            }
        } finally {
            cursor.close();
        }
        return items;
    }
}
