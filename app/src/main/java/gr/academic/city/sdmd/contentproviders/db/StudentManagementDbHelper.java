package gr.academic.city.sdmd.contentproviders.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by trumpets on 3/14/16.
 */
public class StudentManagementDbHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "StudentManagement.db";

    private static final String TEXT_TYPE = " TEXT";
    private static final String INT_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";

    private static final String SQL_CREATE_STUDENTS =
            "CREATE TABLE " + StudentManagementContract.Student.TABLE_NAME + " (" +
                StudentManagementContract.Student._ID + INT_TYPE + " PRIMARY KEY AUTOINCREMENT," +
                StudentManagementContract.Student.COLUMN_NAME_FIRST_NAME + TEXT_TYPE + COMMA_SEP +
                StudentManagementContract.Student.COLUMN_NAME_LAST_NAME + TEXT_TYPE + COMMA_SEP +
                StudentManagementContract.Student.COLUMN_NAME_AGE + INT_TYPE +
            " )";

    private static final String SQL_DELETE_STUDENTS =
            "DROP TABLE IF EXISTS " + StudentManagementContract.Student.TABLE_NAME;

    public StudentManagementDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_STUDENTS);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade
        // policy is to simply discard the data and start over
        db.execSQL(SQL_DELETE_STUDENTS);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
