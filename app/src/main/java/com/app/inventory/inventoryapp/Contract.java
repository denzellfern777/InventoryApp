package com.app.inventory.inventoryapp;

import android.provider.BaseColumns;

class Contract {

    public final static class TableEntry implements BaseColumns {

        public final static String TABLE_NAME = "product";

        public final static String _ID = BaseColumns._ID;
        public final static String PRODUCT_NAME_COL = "prod_name";
        public final static String PRODUCT_PRICE_COL = "price";
        public final static String PRODUCT_QUANTITY_COL = "qty";
        public final static String SUPPLIER_NAME_COL = "supp_name";
        public final static String SUPPLIER_PHONE_NUMBER_COL = "supp_ph_no";

    }

}
