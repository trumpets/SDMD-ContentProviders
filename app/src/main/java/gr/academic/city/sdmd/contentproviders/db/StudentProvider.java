package gr.academic.city.sdmd.contentproviders.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by trumpets on 3/14/16.
 */
public class StudentProvider extends ContentProvider {

    // Creates a UriMatcher object.
    private static final UriMatcher uriMatcher;

    private static final int STUDENTS = 1;
    private static final int STUDENT_ITEM = 2;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(StudentManagementContract.AUTHORITY, StudentManagementContract.Student.TABLE_NAME, STUDENTS);
        uriMatcher.addURI(StudentManagementContract.AUTHORITY, StudentManagementContract.Student.TABLE_NAME + "/#", STUDENT_ITEM);
    }

    private StudentManagementDbHelper dbHelper;

    @Override
    public boolean onCreate() {
        dbHelper = new StudentManagementDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        switch (uriMatcher.match(uri)) {
            case STUDENT_ITEM:
                selection = StudentManagementContract.Student._ID + "=" + uri.getLastPathSegment();
                break;
            case STUDENTS:
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        // Get the database and run the query
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(StudentManagementContract.Student.TABLE_NAME, projection, selection, selectionArgs,
                null, null, sortOrder);

        // Tell the cursor what uri to watch, so it knows when its source data changes
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        if (uriMatcher.match(uri) != STUDENTS) {
            throw new IllegalArgumentException("Unknown URI " + uri);
        }

        // Get the database and insert
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long rowId = db.insert(StudentManagementContract.Student.TABLE_NAME, null, values);
        if (rowId > 0) {
            Uri studentUri = ContentUris.withAppendedId(StudentManagementContract.Student.CONTENT_URI, rowId);

            // Signal all cursor which monitor this URI that there is new data and
            // they should re-query
            getContext().getContentResolver().notifyChange(studentUri, null);
            return studentUri;
        }

        throw new SQLException("Failed to insert row into " + uri);

    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        switch (uriMatcher.match(uri)) {
            case STUDENT_ITEM:
                selection = StudentManagementContract.Student._ID + "=" + uri.getLastPathSegment();
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        // Get the database and update
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int count = db.update(StudentManagementContract.Student.TABLE_NAME, values, selection, selectionArgs);

        getContext().getContentResolver().notifyChange(uri, null);

        return count;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        switch (uriMatcher.match(uri)) {
            case STUDENT_ITEM:
                selection = StudentManagementContract.Student._ID + "=" + uri.getLastPathSegment();
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        // Get the database and delete
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int count = db.delete(StudentManagementContract.Student.TABLE_NAME, selection, selectionArgs);

        getContext().getContentResolver().notifyChange(uri, null);

        return count;

    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        String subType;
        switch (uriMatcher.match(uri)) {
            case STUDENTS:
                subType = "vnd.android.cursor.dir/";
                break;
            case STUDENT_ITEM:
                subType = "vnd.android.cursor.item/";
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        return subType += "vnd." + StudentManagementContract.AUTHORITY + "." + StudentManagementContract.Student.TABLE_NAME;

    }
}
