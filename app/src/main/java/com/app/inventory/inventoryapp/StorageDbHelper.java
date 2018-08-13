package com.app.inventory.inventoryapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.app.inventory.inventoryapp.Contract.TableEntry;

class StorageDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "storeinventory.db";
    private static final int DATABASE_VERSION = 1;

    public StorageDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String createTable = "CREATE TABLE " + TableEntry.TABLE_NAME + " ("
                + TableEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TableEntry.PRODUCT_NAME_COL + " TEXT NOT NULL, "
                + TableEntry.PRODUCT_PRICE_COL + " INTEGER NOT NULL, "
                + TableEntry.PRODUCT_QUANTITY_COL + " INTEGER NOT NULL, "
                + TableEntry.SUPPLIER_NAME_COL + " TEXT NOT NULL, "
                + TableEntry.SUPPLIER_PHONE_NUMBER_COL + " TEXT NOT NULL);";

        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TableEntry.TABLE_NAME);
    }
}
