package com.app.inventory.inventoryapp;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

class Contract {

    public static final String CONTENT_AUTHORITY = "com.app.inventory.inventoryapp";
    public static final String PATH_INVENTORY = "product";
    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    private Contract() {
    }

    public final static class TableEntry implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_INVENTORY);

        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_INVENTORY;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_INVENTORY;

        public final static String TABLE_NAME = "product";

        public final static String _ID = BaseColumns._ID;
        public final static String PRODUCT_NAME_COL = "prod_name";
        public final static String PRODUCT_PRICE_COL = "price";
        public final static String PRODUCT_QUANTITY_COL = "qty";
        public final static String SUPPLIER_NAME_COL = "supp_name";
        public final static String SUPPLIER_PHONE_NUMBER_COL = "supp_ph_no";

    }

}
