package com.alumipro.mobile.data.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.alumipro.mobile.BuildConfig;
import com.alumipro.mobile.utils.ValidationUtils;

public class ConfigStore {
    private final AlumiproDbHelper dbHelper;

    public ConfigStore(Context context) {
        this.dbHelper = new AlumiproDbHelper(context.getApplicationContext());
    }

    public String getApiBaseUrl() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT api_base_url FROM app_config WHERE id = 1", null);
        try {
            if (cursor.moveToFirst()) {
                return ValidationUtils.normalizeBaseUrl(cursor.getString(0));
            }
        } finally {
            cursor.close();
        }
        return BuildConfig.DEFAULT_API_BASE_URL;
    }

    public void saveApiBaseUrl(String value) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", 1);
        contentValues.put("api_base_url", ValidationUtils.normalizeBaseUrl(value));
        contentValues.put("updated_at", String.valueOf(System.currentTimeMillis()));
        db.insertWithOnConflict("app_config", null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
    }

    public void reset() {
        saveApiBaseUrl(BuildConfig.DEFAULT_API_BASE_URL);
    }
}
