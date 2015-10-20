package sinapsysit.com.thejobsproject.data;

import android.content.*;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

import java.sql.SQLException;

import sinapsysit.com.thejobsproject.data.JobsDBContants.*;

/**
 * Created by jujomago on 20/10/2015.
 */
public class JobsContentProvider extends ContentProvider {

    /*Build a URI Matcher*/
    private static final UriMatcher uriMatcher = buildUriMatcher();

    private static final int JOBS = 100;
    private static final int JOBS_WITH_ID = 101;
    private static final int CONTACTS = 200;
    private static final int CONTACTS_WITH_JOB = 201;
    private JobsDbHelper dbHelper;

    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = JobsDBContants.CONTENT_AUTHORITY;

        matcher.addURI(authority, JobsDBContants.JOBS_PATH, JOBS);
        matcher.addURI(authority, JobsDBContants.JOBS_PATH + "/#", JOBS_WITH_ID);
        matcher.addURI(authority, JobsDBContants.CONTACTS_PATH, CONTACTS);
        matcher.addURI(authority, JobsDBContants.CONTACTS_PATH + "/#", CONTACTS_WITH_JOB);

        return matcher;

    }

    @Override
    public boolean onCreate() {
        dbHelper=new  JobsDbHelper(getContext(),"jobsDB",null);
        return false;
    }

     @Nullable
    @Override
    public String getType(Uri uri) {
        int match = uriMatcher.match(uri);
        switch (match){
            case JOBS:
                return JobDbData.CONTENT_TYPE;
            case JOBS_WITH_ID:
                return JobDbData.CONTENT_ITEM_TYPE;
            case CONTACTS:
                return ContactDbData.CONTENT_TYPE;
            case CONTACTS_WITH_JOB:
                return ContactDbData.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Not yet implemented");
        }
    }


    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        int match = uriMatcher.match(uri);
        SQLiteDatabase db=dbHelper.getReadableDatabase();
        switch(match){
            case JOBS:
                return db.query(JobDbData.TABLE_NAME,projection,selection,selectionArgs,null, null,sortOrder);
            case CONTACTS:
                return db.query(ContactDbData.TABLE_NAME,projection,selection,selectionArgs,null, null, sortOrder);
            default:
                throw new UnsupportedOperationException("Not yet implemented");
        }
    }


    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int match = uriMatcher.match(uri);
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        Uri returnUri=null;
        long retValue;

        switch (match) {
            case JOBS:
                retValue = db.insert(JobDbData.TABLE_NAME,null, values);
                if (retValue != -1) {
                    returnUri = JobDbData.CONTENT_URI.buildUpon().appendPath(Long.toString(retValue)).build();
                } else {
                    try {
                        throw new SQLException("Error inserting Job");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case CONTACTS:
                retValue = dbHelper.getWritableDatabase().insert(ContactDbData.TABLE_NAME,null, values);
                if (retValue != -1) {
                    returnUri = ContactDbData.CONTENT_URI;
                } else {
                    try {
                        throw new SQLException("Error inserting Job");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                break;
           default:
                throw new UnsupportedOperationException("Not yet implemented");
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
