package com.app.inventory.inventoryapp;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import static com.app.inventory.inventoryapp.Contract.TableEntry;

class CursorAdapter extends android.widget.CursorAdapter {

    public CursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        return LayoutInflater.from(context).inflate(R.layout.inventory_list_item, parent, false);
    }

    @Override
    public void bindView(final View view, final Context context, final Cursor cursor) {

        TextView productNameTextView = view.findViewById(R.id.product_name_list_item_tv);
        TextView productPriceTextView = view.findViewById(R.id.product_price_list_item_tv);
        TextView productQuantityTextView = view.findViewById(R.id.product_quantity_list_item_tv);
        Button productSaleButton = view.findViewById(R.id.sale_btn);

        final int columnIdIndex = cursor.getColumnIndex(TableEntry._ID);
        int productNameColumnIndex = cursor.getColumnIndex(TableEntry.PRODUCT_NAME_COL);
        int productPriceColumnIndex = cursor.getColumnIndex(TableEntry.PRODUCT_PRICE_COL);
        int productQuantityColumnIndex = cursor.getColumnIndex(TableEntry.PRODUCT_QUANTITY_COL);

        final String productID = cursor.getString(columnIdIndex);
        final String productName = cursor.getString(productNameColumnIndex);
        String productPrice = cursor.getString(productPriceColumnIndex);
        final String productQuantity = cursor.getString(productQuantityColumnIndex);

        productSaleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MainActivity activity = (MainActivity) context;
                activity.sellProductItem(Integer.valueOf(productID), Integer.valueOf(productQuantity), productName);
            }
        });

        productNameTextView.setText(productName);
        productPriceTextView.setText(String.format("%s%s%s", context.getString(R.string.product_price), context.getString(R.string.colon), productPrice));
        productQuantityTextView.setText(String.format("%s%s%s", context.getString(R.string.quantity), context.getString(R.string.colon), productQuantity));

    }


}
