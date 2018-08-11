package com.app.inventory.inventoryapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.app.inventory.inventoryapp.Contract.TableEntry;

public class MainActivity extends AppCompatActivity {

    private StorageDbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);

        FloatingActionButton plus = findViewById(R.id.fab);
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NewEntry.class);
                startActivity(intent);
            }
        });

        dbHelper = new StorageDbHelper(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        displayDatabaseInfo();

    }

    private void displayDatabaseInfo() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] columns = {
                TableEntry._ID,
                TableEntry.PRODUCT_NAME_COL,
                TableEntry.PRODUCT_PRICE_COL,
                TableEntry.PRODUCT_QUANTITY_COL,
                TableEntry.SUPPLIER_NAME_COL,
                TableEntry.SUPPLIER_PHONE_NUMBER_COL
        };
        Cursor cursor = db.query(
                TableEntry.TABLE_NAME,
                columns,
                null,
                null,
                null,
                null,
                null);

        TextView displayView = findViewById(R.id.table_contents_tv);

        //noinspection TryFinallyCanBeTryWithResources
        try {

            displayView.setText("");

            displayView.append(
                    TableEntry._ID + getString(R.string.separator) +
                            TableEntry.PRODUCT_NAME_COL + getString(R.string.separator) +
                            TableEntry.PRODUCT_PRICE_COL + getString(R.string.separator) +
                            TableEntry.PRODUCT_QUANTITY_COL + getString(R.string.separator) +
                            TableEntry.SUPPLIER_NAME_COL + getString(R.string.separator) +
                            TableEntry.SUPPLIER_PHONE_NUMBER_COL + "\n");

            int idColumnIndex = cursor.getColumnIndex(TableEntry._ID);
            int nameColumnIndex = cursor.getColumnIndex(TableEntry.PRODUCT_NAME_COL);
            int priceColumnIndex = cursor.getColumnIndex(TableEntry.PRODUCT_PRICE_COL);
            int quantityColumnIndex = cursor.getColumnIndex(TableEntry.PRODUCT_QUANTITY_COL);
            int supplierNameColumnIndex = cursor.getColumnIndex(TableEntry.SUPPLIER_NAME_COL);
            int supplierPhoneColumnIndex = cursor.getColumnIndex(TableEntry.SUPPLIER_PHONE_NUMBER_COL);
            while (cursor.moveToNext()) {
                int currentID = cursor.getInt(idColumnIndex);
                String currentName = cursor.getString(nameColumnIndex);
                long currentPrice = cursor.getLong(priceColumnIndex);
                int currentQuantity = cursor.getInt(quantityColumnIndex);
                String currentSupplierName = cursor.getString(supplierNameColumnIndex);
                String currentSupplierPhone = cursor.getString(supplierPhoneColumnIndex);

                displayView.append(("\n\n" + currentID + getString(R.string.separator) +
                        currentName + getString(R.string.separator) +
                        currentPrice + getString(R.string.separator) +
                        currentQuantity + getString(R.string.separator) +
                        currentSupplierName + getString(R.string.separator) +
                        currentSupplierPhone));
            }

        } finally {
            cursor.close();
        }
    }
}
