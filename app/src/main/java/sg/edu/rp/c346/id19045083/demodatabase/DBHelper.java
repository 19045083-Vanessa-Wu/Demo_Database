package sg.edu.rp.c346.id19045083.demodatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    // Start version with 1
    // increment by 1 whenever db schema changes.
    private static final int DATABASE_VER = 1;

    // Filename of the database
    private static final String DATABASE_NAME = "task.db";

    private static final String TABLE_TASK = "task";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_DATE = "date";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VER);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String createTableSql = "CREATE TABLE " + TABLE_TASK +  "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_DATE + " TEXT,"
                + COLUMN_DESCRIPTION + " TEXT )";
        db.execSQL(createTableSql);
        Log.i("info" ,"created tables");

    } // onCreate method

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASK);

        // Create table(s) again
        onCreate(db);
    } // onUpgrade method

    public void insertTask(String description, String date) {

        // Get an instance of the database for writing
        SQLiteDatabase db = this.getWritableDatabase();

        // Use ContentValues object to store the values for the db operation
        ContentValues values = new ContentValues();

        // Store the column name as key and the description as value
        values.put(COLUMN_DESCRIPTION, description);

        // Store the column name as key and the date as value
        values.put(COLUMN_DATE, date);

        // Insert the row into the TABLE_TASK
        db.insert(TABLE_TASK, null, values);

        // Close the database connection
        db.close();
    } // InsertTask method

    public ArrayList<String> getTaskContent() {

        //Create an Arraylist that holds String objects
        ArrayList<String> tasks = new ArrayList<>();

        // Select all the tasks' description
        String selectQuery = "SELECT " + COLUMN_DESCRIPTION + " FROM " + TABLE_TASK;

        // Get the instance of database to read
        SQLiteDatabase db = this.getReadableDatabase();

        // Run the SQL query and get back the Cursor object
        Cursor cursor = db.rawQuery(selectQuery, null);

        // moveToFirst() moves to first row, null f no records
        if (cursor.moveToFirst()) {
            // Loop while moveToNext() points to next row and returns true;
            // moveToNext() returns false when no more next row to move to
            do {
                // Add the task content to the Arraylist object
                // getString(0) retrieves first column data
                // getInt(0) if data is an integer value
                tasks.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        // Close connection
        cursor.close();
        db.close();

        return tasks;
    } // getTaskContent method


    public ArrayList<Task> getTasks() {
        ArrayList<Task> tasks = new ArrayList<Task>();
        String selectQuery = "SELECT " + COLUMN_ID + ", " + COLUMN_DESCRIPTION + ", " + COLUMN_DATE
                          + " FROM " + TABLE_TASK + " ORDER BY " + COLUMN_DESCRIPTION;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String description = cursor.getString(1);
                String date = cursor.getString(2);
                Task obj = new Task(id, description, date);
                tasks.add(obj);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return tasks;
    } // getTasks method

    public ArrayList<Task> getTasksDesc() {
        ArrayList<Task> tasks = new ArrayList<Task>();
        String selectQuery = "SELECT " + COLUMN_ID + ", " + COLUMN_DESCRIPTION + ", " + COLUMN_DATE
                + " FROM " + TABLE_TASK + " ORDER BY " + COLUMN_DESCRIPTION + " DESC";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String description = cursor.getString(1);
                String date = cursor.getString(2);
                Task obj = new Task(id, description, date);
                tasks.add(obj);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return tasks;
    } // getTasks method

} // DBHelper class