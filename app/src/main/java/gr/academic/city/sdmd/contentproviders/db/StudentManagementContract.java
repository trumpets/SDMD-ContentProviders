package gr.academic.city.sdmd.contentproviders.db;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by trumpets on 3/14/16.
 */
public final class StudentManagementContract {

    public static final String AUTHORITY = "gr.academic.city.sdsm.app";

    // To prevent someone from accidentally instantiating the,
    // contract class, give it a private empty constructor.
    private StudentManagementContract() {
    }

    // Inner class that defines the table contents
    // BaseColumns allow us to inherit a primary key field called _ID
    // that most Android classes expect
    public static abstract class Student implements BaseColumns {

        public static final String TABLE_NAME = "student";
        public static final String COLUMN_NAME_FIRST_NAME = "first_name";
        public static final String COLUMN_NAME_LAST_NAME = "last_name";
        public static final String COLUMN_NAME_AGE = "age";

        public static final Uri CONTENT_URI = Uri.parse("content://" +
                AUTHORITY + "/" + TABLE_NAME);
    }
}
