package com.app.inventory.inventoryapp;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.app.inventory.inventoryapp.Contract.TableEntry;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int LOADER_ID = 0;
    private CursorAdapter cursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);

        FloatingActionButton add_new_product = findViewById(R.id.fab);
        add_new_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ProductEditorActivity.class);
                startActivity(intent);
            }
        });

        ListView inventoryListView = findViewById(R.id.inventory_listview);

        LinearLayout emptyView = findViewById(R.id.empty_table_placeholder);
        inventoryListView.setEmptyView(emptyView);

        cursorAdapter = new CursorAdapter(this, null);
        inventoryListView.setAdapter(cursorAdapter);

        inventoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, final long id) {
                Intent intent = new Intent(MainActivity.this, ProductDetailsActivity.class);
                Uri currentProductUri = ContentUris.withAppendedId(TableEntry.CONTENT_URI, id);
                intent.setData(currentProductUri);
                startActivity(intent);
            }
        });

        getLoaderManager().initLoader(LOADER_ID, null, this);
    }

    public void sellProductItem(int productID, int productQuantity, String productName) {
        productQuantity = productQuantity - 1;
        if (productQuantity >= 0) {
            ContentValues values = new ContentValues();
            values.put(TableEntry.PRODUCT_QUANTITY_COL, productQuantity);
            Uri updateUri = ContentUris.withAppendedId(TableEntry.CONTENT_URI, productID);
            getContentResolver().update(updateUri, values, null, null);
            Toast.makeText(this, getString(R.string.on_product_sold_toast_msg) + productName, Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(this, getString(R.string.out_of_stock_toast_msg), Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                TableEntry._ID,
                TableEntry.PRODUCT_NAME_COL,
                TableEntry.PRODUCT_PRICE_COL,
                TableEntry.PRODUCT_QUANTITY_COL,
                TableEntry.SUPPLIER_NAME_COL,
                TableEntry.SUPPLIER_PHONE_NUMBER_COL
        };

        return new CursorLoader(this,
                TableEntry.CONTENT_URI,
                projection,
                null,
                null,
                null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete_all_entries:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(getString(R.string.alert_dialog_msg));
                builder.setPositiveButton(getString(R.string.positive), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        getContentResolver().delete(TableEntry.CONTENT_URI, null, null);
                        Toast.makeText(MainActivity.this, getString(R.string.inventory_cleared), Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton(getString(R.string.negative), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        if (dialog != null) {
                            dialog.dismiss();
                        }
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        cursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        cursorAdapter.swapCursor(null);
    }

}
