package com.guyard.meetme.DataManagers.DataBase;

import android.provider.BaseColumns;

/**
 * Created by Guy on 09-Jan-16.
 */
public class UserTable {

    public static abstract class UserInfo implements BaseColumns {
        public static final String TABLE_NAME = "User_Table";
        public static final String NAME = "Name";
        public static final String IS_ME = "Is_Me";
        public static final String SECONDARY_ID = "Secondary_Id";
    }
}
