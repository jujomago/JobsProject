package sinapsysit.com.thejobsproject.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import sinapsysit.com.thejobsproject.data.JobsDBContants.*;

/**
 * Created by jujomago on 12/10/2015.
 */
public class JobsDbHelper extends SQLiteOpenHelper {
    private static final int DB_VERSION=1;
    private static final String DB_NAME="jobs.db";

    public JobsDbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory, DB_VERSION);
        System.out.println("************************** CONSTRUCTOR DBHELPER ***********************");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        System.out.println("************************** METODO ON CREATE ***********************");
        createTables(db);
    }

    private void createTables(SQLiteDatabase db) {
        System.out.println("////// CREANDO TABLAS /////////////");

        final String SQL_CREATE_JOB = "CREATE TABLE " + JobDbData.TABLE_NAME + "(" +
                JobDbData._ID + " INTEGER PRIMARY KEY ON CONFLICT REPLACE," +
                JobDbData.COLUMN_TITLE + " TEXT NOT NULL," +
                JobDbData.COLUMN_DESCRIPTION + " TEXT NOT NULL," +
                JobDbData.COLUMN_POSTED_DATE + " TEXT NOT NULL)";
        final String SQL_CREATE_CONTACT = "CREATE TABLE " + ContactDbData.TABLE_NAME + "(" +
                ContactDbData._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                ContactDbData.COLUMN_JOB_ID + " INTEGER NOT NULL," +
                ContactDbData.COLUMN_NUMBER + " TEXT NOT NULL," +
                "FOREIGN KEY (" + ContactDbData.COLUMN_JOB_ID + ") REFERENCES " +
                JobDbData.TABLE_NAME + " (" + JobDbData._ID + ")," +
                "UNIQUE(" + ContactDbData.COLUMN_JOB_ID + ","
                + ContactDbData.COLUMN_NUMBER + ") ON CONFLICT REPLACE);";

        db.execSQL(SQL_CREATE_JOB);
        db.execSQL(SQL_CREATE_CONTACT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ContactDbData.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + JobDbData.TABLE_NAME);
        createTables(db);
    }
}
