package com.alumipro.mobile.data.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.alumipro.mobile.model.Cliente;
import com.alumipro.mobile.model.Producto;
import com.alumipro.mobile.model.VentaResumen;

import java.util.ArrayList;
import java.util.List;

public class CacheStore {
    private final AlumiproDbHelper dbHelper;

    public CacheStore(Context context) {
        this.dbHelper = new AlumiproDbHelper(context.getApplicationContext());
    }

    public void replaceClientes(List<Cliente> clientes) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            db.delete("clientes_cache", null, null);
            for (Cliente item : clientes) {
                ContentValues values = new ContentValues();
                values.put("id", item.getId());
                values.put("nombre", item.getNombre());
                values.put("telefono", item.getTelefono());
                values.put("direccion", item.getDireccion());
                values.put("email", item.getEmail());
                values.put("updated_at", String.valueOf(System.currentTimeMillis()));
                db.insert("clientes_cache", null, values);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    public List<Cliente> getClientes() {
        List<Cliente> items = new ArrayList<>();
        Cursor cursor = dbHelper.getReadableDatabase().rawQuery("SELECT id, nombre, telefono, direccion, email FROM clientes_cache ORDER BY nombre ASC", null);
        try {
            while (cursor.moveToNext()) {
                Cliente item = new Cliente();
                item.setId(cursor.getInt(0));
                item.setNombre(cursor.getString(1));
                item.setTelefono(cursor.getString(2));
                item.setDireccion(cursor.getString(3));
                item.setEmail(cursor.getString(4));
                items.add(item);
            }
        } finally {
            cursor.close();
        }
        return items;
    }

    public void replaceProductos(List<Producto> productos) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            db.delete("productos_cache", null, null);
            for (Producto item : productos) {
                ContentValues values = new ContentValues();
                values.put("id", item.getId());
                values.put("nombre", item.getNombre());
                values.put("precio", item.getPrecio());
                values.put("stock", item.getStock());
                values.put("descripcion", item.getDescripcion());
                values.put("updated_at", String.valueOf(System.currentTimeMillis()));
                db.insert("productos_cache", null, values);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    public List<Producto> getProductos() {
        List<Producto> items = new ArrayList<>();
        Cursor cursor = dbHelper.getReadableDatabase().rawQuery("SELECT id, nombre, precio, stock, descripcion FROM productos_cache ORDER BY nombre ASC", null);
        try {
            while (cursor.moveToNext()) {
                Producto item = new Producto();
                item.setId(cursor.getInt(0));
                item.setNombre(cursor.getString(1));
                item.setPrecio(cursor.getDouble(2));
                item.setStock(cursor.getInt(3));
                item.setDescripcion(cursor.getString(4));
                items.add(item);
            }
        } finally {
            cursor.close();
        }
        return items;
    }

    public void replaceVentas(List<VentaResumen> ventas) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            db.delete("ventas_cache", null, null);
            for (VentaResumen item : ventas) {
                ContentValues values = new ContentValues();
                values.put("id", item.getId());
                values.put("fecha", item.getFecha());
                values.put("cliente_nombre", item.getClienteNombre());
                values.put("vendedor_nombre", item.getVendedorNombre());
                values.put("total", item.getTotal());
                values.put("updated_at", String.valueOf(System.currentTimeMillis()));
                db.insert("ventas_cache", null, values);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    public List<VentaResumen> getVentas() {
        List<VentaResumen> items = new ArrayList<>();
        Cursor cursor = dbHelper.getReadableDatabase().rawQuery("SELECT id, fecha, cliente_nombre, vendedor_nombre, total FROM ventas_cache ORDER BY id DESC", null);
        try {
            while (cursor.moveToNext()) {
                VentaResumen item = new VentaResumen();
                item.setId(cursor.getInt(0));
                item.setFecha(cursor.getString(1));
                item.setClienteNombre(cursor.getString(2));
                item.setVendedorNombre(cursor.getString(3));
                item.setTotal(cursor.getDouble(4));
                items.add(item);
            }
        } finally {
            cursor.close();
        }
        return items;
    }

    public int countClientes() {
        return countTable("clientes_cache");
    }

    public int countProductos() {
        return countTable("productos_cache");
    }

    public int countVentas() {
        return countTable("ventas_cache");
    }

    private int countTable(String table) {
        Cursor cursor = dbHelper.getReadableDatabase().rawQuery("SELECT COUNT(*) FROM " + table, null);
        try {
            if (cursor.moveToFirst()) {
                return cursor.getInt(0);
            }
            return 0;
        } finally {
            cursor.close();
        }
    }
}
