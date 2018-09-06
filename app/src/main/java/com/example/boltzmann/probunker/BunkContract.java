package com.example.boltzmann.probunker;

import android.provider.BaseColumns;

public class BunkContract {
    private BunkContract(){}
    public static final class TableEntry implements BaseColumns {
        public static final String TABLE_NAME = "MYTABLE";
        public  static  final String COLUMN_MANE = "NAME";
        public static final String COLUMN_TOTAL = "TOTAL";
        public static final String COLUMN_BUNK = "BUNK";
        public static final String COLUMN_COLOR = "COLOR";
    }
}
