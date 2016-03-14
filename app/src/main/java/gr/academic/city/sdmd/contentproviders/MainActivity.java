package gr.academic.city.sdmd.contentproviders;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import gr.academic.city.sdmd.contentproviders.db.StudentManagementContract;

public class MainActivity extends AppCompatActivity {

    // Define a projection that specifies which columns from the database
    // you will actually use after this query.
    // To save on resources only return the column values that you actually need.
    private static final String[] PROJECTION = {
            StudentManagementContract.Student._ID,
            StudentManagementContract.Student.COLUMN_NAME_FIRST_NAME,
            StudentManagementContract.Student.COLUMN_NAME_LAST_NAME
    };

    // How you want the results sorted in the resulting Cursor
    private static final String SORT_ORDER = StudentManagementContract.Student.COLUMN_NAME_LAST_NAME + " ASC";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_insert).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstName = ((TextView) findViewById(R.id.txt_first_name)).getText().toString();
                String lastName = ((TextView) findViewById(R.id.txt_last_name)).getText().toString();
                String age = ((TextView) findViewById(R.id.txt_age)).getText().toString();

                insertStudent(firstName, lastName, age);
            }
        });

        findViewById(R.id.btn_query).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAllStudents();
            }
        });
    }

    private void insertStudent(String firstName, String lastName, String age) {
        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(StudentManagementContract.Student.COLUMN_NAME_FIRST_NAME, firstName);
        values.put(StudentManagementContract.Student.COLUMN_NAME_LAST_NAME, lastName);
        values.put(StudentManagementContract.Student.COLUMN_NAME_AGE, age);

        // Insert the new row, returning the Uri of the new row
        Uri newRowUri;
        newRowUri = getContentResolver().insert(
                StudentManagementContract.Student.CONTENT_URI, // the table to insert to
                values); // all the data to insert

        Toast.makeText(MainActivity.this, "New record inserted - Uri " + newRowUri, Toast.LENGTH_SHORT).show();
    }

    private void getAllStudents() {
        Cursor cursor = getContentResolver().query(
                StudentManagementContract.Student.CONTENT_URI,          // The Uri to query
                PROJECTION,                                             // The columns to return
                null,                                                   // The columns for the WHERE clause
                null,                                                   // The values for the WHERE clause
                SORT_ORDER                                              // The sort order
        );

        String result = "";

        int firstNameColumn = cursor.getColumnIndexOrThrow(StudentManagementContract.Student.COLUMN_NAME_FIRST_NAME);
        int lastNameColumn = cursor.getColumnIndexOrThrow(StudentManagementContract.Student.COLUMN_NAME_LAST_NAME);
        while (cursor.moveToNext()) {
            String firstName = cursor.getString(firstNameColumn);
            String lastName = cursor.getString(lastNameColumn);

            result += firstName + "\t" + lastName + "\n";
        }

        TextView resultsTextView = (TextView) findViewById(R.id.tv_results);
        resultsTextView.setText(result);

        cursor.close();
    }
}
