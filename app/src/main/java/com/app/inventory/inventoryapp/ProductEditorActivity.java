package com.app.inventory.inventoryapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import static com.app.inventory.inventoryapp.Contract.TableEntry;

public class ProductEditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int LOADER_ID = 0;
    private EditText productNameEditText;
    private EditText productPriceEditText;
    private EditText productQtyEditText;
    private EditText productSupplierNameEditText;
    private EditText productSupplierPhoneNumberEditText;
    private Uri currentUri;

    private boolean mProductHasChanged = false;
    private final View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mProductHasChanged = true;
            return false;
        }
    };

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_entry);

        Intent intent = getIntent();
        currentUri = intent.getData();

        if (currentUri == null) {
            setTitle(getString(R.string.add_title));
            invalidateOptionsMenu();
        } else {
            setTitle(getString(R.string.edit_product_title));
            getLoaderManager().initLoader(LOADER_ID, null, this);
        }

        productNameEditText = findViewById(R.id.product_name_et);
        productPriceEditText = findViewById(R.id.product_price_et);
        productQtyEditText = findViewById(R.id.product_quantity_et);
        productSupplierNameEditText = findViewById(R.id.product_supplier_name_et);
        productSupplierPhoneNumberEditText = findViewById(R.id.product_supplier_phone_number_et);

        productNameEditText.setOnTouchListener(mTouchListener);
        productPriceEditText.setOnTouchListener(mTouchListener);
        productQtyEditText.setOnTouchListener(mTouchListener);
        productSupplierNameEditText.setOnTouchListener(mTouchListener);
        productSupplierPhoneNumberEditText.setOnTouchListener(mTouchListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_menu, menu);
        return true;
    }

    private void addProductToInventory() {
        String productName = productNameEditText.getText().toString().trim();
        String productPrice = productPriceEditText.getText().toString().trim();
        String productQuantity = productQtyEditText.getText().toString().trim();
        String productSupplierName = productSupplierNameEditText.getText().toString().trim();
        String productSupplierPhoneNumber = productSupplierPhoneNumberEditText.getText().toString().trim();

        if (currentUri == null) {
            if (TextUtils.isEmpty(productName) || TextUtils.isEmpty(productPrice) || TextUtils.isEmpty(productQuantity) || TextUtils.isEmpty(productSupplierName) || TextUtils.isEmpty(productSupplierPhoneNumber)) {
                Toast.makeText(this, getString(R.string.cant_leave_any_field_blank_toast_msg), Toast.LENGTH_SHORT).show();

                checkBlank(productName, getString(R.string.product_name), productNameEditText);
                checkBlank(productPrice, getString(R.string.product_price), productPriceEditText);
                checkBlank(productQuantity, getString(R.string.product_quantity), productQtyEditText);
                checkBlank(productSupplierName, getString(R.string.product_supplier_name), productSupplierNameEditText);
                checkBlank(productSupplierPhoneNumber, getString(R.string.product_supplier_phone_number), productSupplierPhoneNumberEditText);

                return;
            }

            ContentValues values = new ContentValues();

            values.put(TableEntry.PRODUCT_NAME_COL, productName);
            values.put(TableEntry.PRODUCT_PRICE_COL, Integer.parseInt(productPrice));
            values.put(TableEntry.PRODUCT_QUANTITY_COL, Integer.parseInt(productQuantity));
            values.put(TableEntry.SUPPLIER_NAME_COL, productSupplierName);
            values.put(TableEntry.SUPPLIER_PHONE_NUMBER_COL, productSupplierPhoneNumber);

            Uri newUri = getContentResolver().insert(TableEntry.CONTENT_URI, values);

            if (newUri == null) {
                Toast.makeText(this, getString(R.string.error_msg),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.save_product_success_msg),
                        Toast.LENGTH_SHORT).show();

                finish();
            }
        } else {

            if (TextUtils.isEmpty(productName) || TextUtils.isEmpty(productPrice) || TextUtils.isEmpty(productQuantity) || TextUtils.isEmpty(productSupplierName) || TextUtils.isEmpty(productSupplierPhoneNumber)) {
                Toast.makeText(this, getString(R.string.cant_leave_any_field_blank_toast_msg), Toast.LENGTH_SHORT).show();

                checkBlank(productName, getString(R.string.product_name), productNameEditText);
                checkBlank(productPrice, getString(R.string.product_price), productPriceEditText);
                checkBlank(productQuantity, getString(R.string.product_quantity), productQtyEditText);
                checkBlank(productSupplierName, getString(R.string.product_supplier_name), productSupplierNameEditText);
                checkBlank(productSupplierPhoneNumber, getString(R.string.product_supplier_phone_number), productSupplierPhoneNumberEditText);

                return;
            }

            ContentValues values = new ContentValues();

            values.put(TableEntry.PRODUCT_NAME_COL, productName);
            values.put(TableEntry.PRODUCT_PRICE_COL, Integer.parseInt(productPrice));
            values.put(TableEntry.PRODUCT_QUANTITY_COL, Integer.parseInt(productQuantity));
            values.put(TableEntry.SUPPLIER_NAME_COL, productSupplierName);
            values.put(TableEntry.SUPPLIER_PHONE_NUMBER_COL, productSupplierPhoneNumber);

            int rowsAffected = getContentResolver().update(currentUri, values, null, null);
            if (rowsAffected == 0) {
                Toast.makeText(this, getString(R.string.error_msg),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.save_product_success_msg),
                        Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private void checkBlank(String field, String toastMsg, EditText editText) {
        if (TextUtils.isEmpty(field)) {
            String errorMsg = toastMsg + " " + getString(R.string.blank_error_msg);
            editText.setError(errorMsg);
        }
    }

    @Override
    public void onBackPressed() {
        if (!mProductHasChanged) {
            super.onBackPressed();
            return;
        }
        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                };

        discardChangesDialog(discardButtonClickListener);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add:
                addProductToInventory();
                return true;
            case android.R.id.home:
                if (!mProductHasChanged) {
                    NavUtils.navigateUpFromSameTask(ProductEditorActivity.this);
                    return true;
                }
                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                NavUtils.navigateUpFromSameTask(ProductEditorActivity.this);
                            }
                        };
                discardChangesDialog(discardButtonClickListener);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void discardChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.discard_changes_alert);
        builder.setPositiveButton(R.string.positive, discardButtonClickListener);
        builder.setNegativeButton(R.string.negative, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
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
            int nameColumnIndex = data.getColumnIndex(TableEntry.PRODUCT_NAME_COL);
            int priceColumnIndex = data.getColumnIndex(TableEntry.PRODUCT_PRICE_COL);
            int quantityColumnIndex = data.getColumnIndex(TableEntry.PRODUCT_QUANTITY_COL);
            int supplierNameColumnIndex = data.getColumnIndex(TableEntry.SUPPLIER_NAME_COL);
            int supplierPhoneColumnIndex = data.getColumnIndex(TableEntry.SUPPLIER_PHONE_NUMBER_COL);

            String productName = data.getString(nameColumnIndex);
            int productPrice = data.getInt(priceColumnIndex);
            int productQuantity = data.getInt(quantityColumnIndex);
            String productSupplierName = data.getString(supplierNameColumnIndex);
            String productSupplierPhone = data.getString(supplierPhoneColumnIndex);

            productNameEditText.setText(productName);
            productPriceEditText.setText(String.valueOf(productPrice));
            productQtyEditText.setText(String.valueOf(productQuantity));
            productSupplierNameEditText.setText(productSupplierName);
            productSupplierPhoneNumberEditText.setText(productSupplierPhone);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        productNameEditText.setText("");
        productPriceEditText.setText("");
        productQtyEditText.setText("");
        productSupplierPhoneNumberEditText.setText("");
        productSupplierNameEditText.setText("");
    }
}
