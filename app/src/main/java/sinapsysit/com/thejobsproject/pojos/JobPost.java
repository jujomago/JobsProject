package sinapsysit.com.thejobsproject.pojos;

import android.content.ContentResolver;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.Serializable;
import java.util.ArrayList;


import sinapsysit.com.thejobsproject.data.JobsDBContants.ContactDbData;

/**
 * Created by jujomago on 11/10/2015.
 */
public class JobPost implements Serializable {
    private int id;
    private String title;
    private String postDate;
    private String description;
    private String[] contacts;

    public JobPost(int id, String title, String postDate, String description) {
        this.id = id;
        this.title = title;
        this.postDate = postDate;
        this.description = description;
    }

    public JobPost() {

    }

    public String[] getContacts() {
        return contacts;
    }

    public void setContacts(ContentResolver cr,int current_job_id) {

        String[] columnas_contacts={ContactDbData._ID, ContactDbData.COLUMN_JOB_ID, ContactDbData.COLUMN_NUMBER};
        String[] whereArgs={current_job_id+""};


        Cursor cursor_contacts=cr.query(ContactDbData.CONTENT_URI, columnas_contacts, ContactDbData.COLUMN_JOB_ID+"=?", whereArgs,  ContactDbData._ID + " ASC");
        contacts=new String[cursor_contacts.getCount()];

        for (int i = 0; i < cursor_contacts.getCount(); i++) {
            cursor_contacts.moveToNext();
            contacts[i]=cursor_contacts.getString(2);
        }
       cursor_contacts.close();
    }

    @Override
    public String toString() {
       return "JobPost{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", postDate='" + postDate + '\'' +
                ", description='" + description + '\'' +
                '}';

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPostDate() {
        return postDate;
    }

    public void setPostDate(String postDate) {
        this.postDate = postDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
