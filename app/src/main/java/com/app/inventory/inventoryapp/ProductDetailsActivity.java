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
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.app.inventory.inventoryapp.Contract.TableEntry;

public class ProductDetailsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int LOADER_ID = 0;
    private Uri currentUri;

    private TextView productNameTextView;
    private TextView productPriceTextView;
    private TextView productQuantityTextView;
    private TextView productSupplierNameTextView;
    private TextView productSupplierPhoneNumberTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        productNameTextView = findViewById(R.id.product_name_tv);
        productPriceTextView = findViewById(R.id.product_price_tv);
        productQuantityTextView = findViewById(R.id.product_quantity_tv);
        productSupplierNameTextView = findViewById(R.id.product_supplier_name_tv);
        productSupplierPhoneNumberTextView = findViewById(R.id.product_supplier_phone_number_tv);

        Intent intent = getIntent();
        currentUri = intent.getData();
        if (currentUri == null) {
            invalidateOptionsMenu();
        } else {
            getLoaderManager().initLoader(LOADER_ID, null, this);
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
                currentUri,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        if (data == null || data.getCount() < 1) {
            return;
        }
        if (data.moveToFirst()) {

            final int idColumnIndex = data.getColumnIndex(TableEntry._ID);
            int nameColumnIndex = data.getColumnIndex(TableEntry.PRODUCT_NAME_COL);
            int supplierNameColumnIndex = data.getColumnIndex(TableEntry.SUPPLIER_NAME_COL);
            int supplierPhoneColumnIndex = data.getColumnIndex(TableEntry.SUPPLIER_PHONE_NUMBER_COL);
            int priceColumnIndex = data.getColumnIndex(TableEntry.PRODUCT_PRICE_COL);
            int quantityColumnIndex = data.getColumnIndex(TableEntry.PRODUCT_QUANTITY_COL);

            final String productID = data.getString(idColumnIndex);
            String productName = data.getString(nameColumnIndex);
            String productSupplierName = data.getString(supplierNameColumnIndex);
            final String productSupplierPhone = data.getString(supplierPhoneColumnIndex);
            final int productPrice = data.getInt(priceColumnIndex);
            final int productQuantity = data.getInt(quantityColumnIndex);

            productNameTextView.setText(productName);
            productPriceTextView.setText(String.valueOf(productPrice));
            productQuantityTextView.setText(String.valueOf(productQuantity));
            productSupplierPhoneNumberTextView.setText((productSupplierPhone));
            productSupplierNameTextView.setText(productSupplierName);

            Button productDecreaseButton = findViewById(R.id.decrease_btn);
            productDecreaseButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    decreaseCount(productQuantity);
                }
            });

            Button productIncreaseButton = findViewById(R.id.increase_btn);
            productIncreaseButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    increaseCount(productQuantity);
                }
            });

            ImageButton callSupplierBtn = findViewById(R.id.call_supplier_btn);
            callSupplierBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", productSupplierPhone, null));
                    startActivity(intent);
                }
            });

            FloatingActionButton edit_current_product_details = findViewById(R.id.edit_current_product_details);
            edit_current_product_details.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(ProductDetailsActivity.this, ProductEditorActivity.class);
                    Uri currentProductUri = ContentUris.withAppendedId(TableEntry.CONTENT_URI, Long.parseLong(productID));
                    intent.putExtra("productId", Long.parseLong(productID));
                    intent.setData(currentProductUri);
                    startActivity(intent);
                }
            });

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.product_details_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_product:
                deleteProduct();
                return true;
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(ProductDetailsActivity.this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void decreaseCount(int productQuantity) {
        productQuantity = productQuantity - 1;
        if (productQuantity >= 0) {
            updateProduct(productQuantity);
        } else {
            Toast.makeText(this, R.string.out_of_stock_toast_msg, Toast.LENGTH_SHORT).show();
        }
    }

    private void increaseCount(int productQuantity) {
        productQuantity = productQuantity + 1;
        if (productQuantity >= 0) {
            updateProduct(productQuantity);
        }
    }

    private void deleteProduct() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ProductDetailsActivity.this);
        builder.setMessage(getString(R.string.delete_single_product_alert));
        builder.setPositiveButton(getString(R.string.positive), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (currentUri != null) {
                    int rowsDeleted = getContentResolver().delete(currentUri, null, null);
                    if (rowsDeleted == 0) {
                        Toast.makeText(ProductDetailsActivity.this, getString(R.string.error_deleting_product_msg),
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ProductDetailsActivity.this, getString(R.string.product_delete_success_msg),
                                Toast.LENGTH_SHORT).show();
                    }
                }
                finish();
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


    }

    private void updateProduct(int productQuantity) {

        if (currentUri == null) {
            return;
        }
        ContentValues values = new ContentValues();
        values.put(TableEntry.PRODUCT_QUANTITY_COL, productQuantity);

        if (currentUri == null) {
            Uri newUri = getContentResolver().insert(TableEntry.CONTENT_URI, values);
            if (newUri == null) {
                Toast.makeText(this, R.string.error_msg,
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            int noOfRowsUpdated = getContentResolver().update(currentUri, values, null, null);
            if (noOfRowsUpdated == 0) {
                Toast.makeText(this, getString(R.string.error_msg),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
