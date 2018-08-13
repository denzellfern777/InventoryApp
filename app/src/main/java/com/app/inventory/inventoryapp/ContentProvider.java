package com.app.inventory.inventoryapp;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.app.inventory.inventoryapp.Contract.TableEntry;

public class ContentProvider extends android.content.ContentProvider {

    private static final int PRODUCTS = 100;

    private static final int PRODUCT_ID = 1;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(Contract.CONTENT_AUTHORITY, Contract.PATH_INVENTORY, PRODUCTS);

        sUriMatcher.addURI(Contract.CONTENT_AUTHORITY, Contract.PATH_INVENTORY + "/#", PRODUCT_ID);
    }

    private StorageDbHelper dbHelper;

    @Override
    public boolean onCreate() {
        dbHelper = new StorageDbHelper((getContext()));
        return true;
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase database = dbHelper.getReadableDatabase();

        Cursor cursor;

        int match = sUriMatcher.match(uri);
        switch (match) {
            case PRODUCTS:
                cursor = database.query(TableEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case PRODUCT_ID:
                selection = TableEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(TableEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("unknown URI");
        }
        //noinspection ConstantConditions
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PRODUCTS:
                return insertProduct(uri, contentValues);
            default:
                throw new IllegalArgumentException("An error occurred while inserting the product" + uri);
        }
    }

    @Override
    public String getType(@NonNull Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PRODUCTS:
                return TableEntry.CONTENT_LIST_TYPE;
            case PRODUCT_ID:
                return TableEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI");
        }
    }

    private Uri insertProduct(Uri uri, ContentValues values) {

        SQLiteDatabase database = dbHelper.getWritableDatabase();
        long id = database.insert(TableEntry.TABLE_NAME, null, values);
        if (id == -1) {
            return null;
        }
        //noinspection ConstantConditions
        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, id);
    }

    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        int rowsDeleted;

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PRODUCTS:
                rowsDeleted = database.delete(TableEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case PRODUCT_ID:
                selection = TableEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = database.delete(TableEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }
        if (rowsDeleted != 0) {
            //noinspection ConstantConditions,ConstantConditions,ConstantConditions
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues contentValues, String selection, String[]
            selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PRODUCTS:
                return updateProduct(uri, contentValues, selection, selectionArgs);
            case PRODUCT_ID:
                selection = TableEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateProduct(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    private int updateProduct(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        if (values.containsKey(TableEntry.PRODUCT_NAME_COL)) {
            String nameProduct = values.getAsString(TableEntry.PRODUCT_NAME_COL);
            if (nameProduct == null) {
                throw new IllegalArgumentException("Product name is required");
            }
        }
        if (values.containsKey(TableEntry.PRODUCT_PRICE_COL)) {
            Integer priceProduct = values.getAsInteger(TableEntry.PRODUCT_PRICE_COL);
            if (priceProduct != null && priceProduct < 0) {
                throw new
                        IllegalArgumentException("Product price is not valid");
            }
        }

        if (values.containsKey(TableEntry.PRODUCT_QUANTITY_COL)) {
            Integer quantityProduct = values.getAsInteger(TableEntry.PRODUCT_QUANTITY_COL);
            if (quantityProduct != null && quantityProduct < 0) {
                throw new
                        IllegalArgumentException("Product quantity is not valid");
            }
        }
        if (values.containsKey(TableEntry.SUPPLIER_NAME_COL)) {
            String supplierName = values.getAsString(TableEntry.SUPPLIER_NAME_COL);
            if (supplierName == null) {
                throw new IllegalArgumentException("Supplier Name is required");
            }
        }

        if (values.containsKey(TableEntry.SUPPLIER_PHONE_NUMBER_COL)) {
            String supplierPhone = values.getAsString(TableEntry.SUPPLIER_PHONE_NUMBER_COL);
            if (supplierPhone == null) {
                throw new
                        IllegalArgumentException("Supplier Phone number is required");
            }
        }

        if (values.size() == 0) {
            return 0;
        }

        SQLiteDatabase database = dbHelper.getWritableDatabase();

        int rowsUpdated = database.update(TableEntry.TABLE_NAME, values, selection, selectionArgs);

        if (rowsUpdated != 0) {
            //noinspection ConstantConditions
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsUpdated;
    }
}
