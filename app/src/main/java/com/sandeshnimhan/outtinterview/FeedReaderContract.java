package com.sandeshnimhan.outtinterview;

import android .provider.BaseColumns;

/**
 * Created by nimha on 6/1/2017.
 */

public final class FeedReaderContract {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private FeedReaderContract() {}

    /* Inner class that defines the table contents */
    public static class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "Posts";
        public static final String COLUMN_NAME_USERID = "userId";
        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_BODY = "body";

    }
}
