package com.app.inventory.inventoryapp;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import static com.app.inventory.inventoryapp.Contract.TableEntry;

public class NewEntry extends AppCompatActivity {

    private EditText productNameEditText;
    private EditText productPriceEditText;
    private EditText productQtyEditText;
    private EditText supplierNameEditText;
    private EditText supplierPhoneNumberEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_entry);

        productNameEditText = findViewById(R.id.product_name_et);
        productPriceEditText = findViewById(R.id.product_price_et);
        productQtyEditText = findViewById(R.id.product_quantity_et);
        supplierNameEditText = findViewById(R.id.product_supplier_name_et);
        supplierPhoneNumberEditText = findViewById(R.id.product_supplier_phone_number_et);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add:
                boolean isFieldEmpty = add();
                if (!isFieldEmpty){
                    finish();
                    break;
                }
                return true;
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean add() {
        String productNameString = productNameEditText.getText().toString();
        String productPrice = productPriceEditText.getText().toString();
        String productQuantity = productQtyEditText.getText().toString();
        String supplierName = supplierNameEditText.getText().toString();
        String supplierPhoneNumber = supplierPhoneNumberEditText.getText().toString();

        if (TextUtils.isEmpty(productNameString) || TextUtils.isEmpty(productPrice) || TextUtils.isEmpty(productQuantity) || TextUtils.isEmpty(supplierName) || TextUtils.isEmpty(supplierPhoneNumber)){
            Toast.makeText(this, "You cant leave any field blank", Toast.LENGTH_SHORT).show();
            return true;
        }
        else {

            StorageDbHelper dbHelper = new StorageDbHelper(this);
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(TableEntry.PRODUCT_NAME_COL, productNameString);
            values.put(TableEntry.PRODUCT_PRICE_COL, Integer.parseInt(productPrice));
            values.put(TableEntry.PRODUCT_QUANTITY_COL, Integer.parseInt(productQuantity));
            values.put(TableEntry.SUPPLIER_NAME_COL, supplierName);
            values.put(TableEntry.SUPPLIER_PHONE_NUMBER_COL, supplierPhoneNumber);

            long rowInserted = db.insert(TableEntry.TABLE_NAME, null, values);

            if (rowInserted == -1) {
                Toast.makeText(this, "Some Error occurred. Please try again", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "New Row Added Successfully", Toast.LENGTH_SHORT).show();
            }
            return false;
        }

    }

}
