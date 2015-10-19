package sinapsysit.com.thejobsproject;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

import cz.msebera.android.httpclient.Header;
import sinapsysit.com.thejobsproject.adapters.MyCustomAdapter;
import sinapsysit.com.thejobsproject.data.JobsDBContants;
import sinapsysit.com.thejobsproject.data.JobsDBContants.ContactDbData;
import sinapsysit.com.thejobsproject.data.JobsDBContants.JobDbData;
import sinapsysit.com.thejobsproject.data.JobsDbHelper;
import sinapsysit.com.thejobsproject.pojos.JobPost;

public class ListadoActivity extends AppCompatActivity {

    private static final String URLPOSTS="http://dipandroid-ucb.herokuapp.com/work_posts.json";
    private ArrayList<JobPost> posts_array;
    private MyCustomAdapter customAdapter;

    ListView lista_jobs;

    JobsDbHelper jobs_db_helper;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        posts_array=new ArrayList<>();

        jobs_db_helper=new JobsDbHelper(this,"jobsDB",null);
        createComponents();
        saveJSONToDatabse();
    }

    private void createComponents() {
        lista_jobs= (ListView) findViewById(R.id.listajobs);

        customAdapter =new MyCustomAdapter(getBaseContext(),R.layout.list_item,posts_array);
        lista_jobs.setAdapter(customAdapter);

        lista_jobs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle b = new Bundle();
                b.putSerializable("job_selected", posts_array.get(position));

                Intent myintent = new Intent(getApplicationContext(), DetalleActivity.class);
                myintent.putExtras(b);

                startActivity(myintent);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menuapp, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){

            case R.id.sincronizar:
                sincronizarData();
                break;
            case R.id.postear:
                Intent myintent=new Intent(this,NuevoJobActivity.class);
                startActivity(myintent);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }

    private void sincronizarData() {
        saveJSONToDatabse();
    }

    private void saveJSONToDatabse() {
        db = jobs_db_helper.getWritableDatabase();
        AsyncHttpClient cliente=new AsyncHttpClient();
        cliente.get(URLPOSTS, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {

                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject jsonobject = response.getJSONObject(i);

                        ContentValues contentValues = new ContentValues();
                        contentValues.put(JobDbData._ID, jsonobject.getInt("id"));
                        contentValues.put(JobDbData.COLUMN_TITLE, jsonobject.getString("title"));
                        contentValues.put(JobDbData.COLUMN_DESCRIPTION, jsonobject.getString("description"));
                        contentValues.put(JobDbData.COLUMN_POSTED_DATE, jsonobject.getString("posted_date"));


                        long newid=db.insert(JobDbData.TABLE_NAME, null, contentValues);

                        if(newid>0){

                            JSONArray jsonArray=jsonobject.getJSONArray("contacts");
                            for (int k = 0; k < jsonArray.length(); k++) {
                                String numerocontact=jsonArray.getString(k);
                                ContentValues contentValues2 = new ContentValues();
                                contentValues2.put(ContactDbData.COLUMN_JOB_ID,jsonobject.getInt("id"));
                                contentValues2.put(ContactDbData.COLUMN_NUMBER, numerocontact);
                                long newidc=db.insert(ContactDbData.TABLE_NAME, null, contentValues2);
//                                System.out.println("Insertado:"+newidc);
                            }
                        }else{
                            System.out.println("Ocurrio un error al Insertar a la BD");
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                db.close();
                fillListViewFromDB();

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                Log.d("onFailure", "onFailure", throwable);
            }
        });
    }

    private void fillListViewFromDB() {
        db = jobs_db_helper.getReadableDatabase();

        String [] columnas={JobDbData._ID,JobDbData.COLUMN_TITLE,JobDbData.COLUMN_DESCRIPTION,JobDbData.COLUMN_POSTED_DATE};

        Cursor cursor=db.query(JobDbData.TABLE_NAME, columnas, null, null, null, null, JobDbData._ID + " ASC");

        if(cursor.getCount()>0){
            customAdapter.clear();
        }

        while (cursor.moveToNext()){
            JobPost jobPostTemp=new JobPost();
            jobPostTemp.setId(cursor.getInt(0));
            jobPostTemp.setTitle(cursor.getString(1));
            jobPostTemp.setDescription(cursor.getString(2));
            jobPostTemp.setPostDate(cursor.getString(3));


            String[] columnas_contacts={ContactDbData._ID,ContactDbData.COLUMN_JOB_ID,ContactDbData.COLUMN_NUMBER};
            Cursor cursor_contacts=db.query(ContactDbData.TABLE_NAME, columnas_contacts, ContactDbData.COLUMN_JOB_ID+"=?", new String[]{String.valueOf(jobPostTemp.getId())}, null, null, ContactDbData._ID + " ASC");
            ArrayList<String> numbers_for_job=new ArrayList<>();

            while (cursor_contacts.moveToNext()){
                numbers_for_job.add(cursor_contacts.getString(2));
            }

            jobPostTemp.setContacts(numbers_for_job.toArray(new String[numbers_for_job.size()]));

            posts_array.add(jobPostTemp);
//            customAdapter.add(jobPostTemp);
        }
        db.close();
    }
}
