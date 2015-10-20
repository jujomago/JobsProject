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
import android.widget.SimpleCursorAdapter;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.ArrayList;
import cz.msebera.android.httpclient.Header;
import sinapsysit.com.thejobsproject.data.JobsDBContants.ContactDbData;
import sinapsysit.com.thejobsproject.data.JobsDBContants.JobDbData;
import sinapsysit.com.thejobsproject.pojos.JobPost;

public class ListadoActivity extends AppCompatActivity {

    private static final String URLPOSTS="http://dipandroid-ucb.herokuapp.com/work_posts.json";
    private ArrayList<JobPost> posts_array;

    ListView lista_jobs;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado);

        posts_array=new ArrayList<>();

        createComponents();
        saveJSONToDatabse();
    }

    private void createComponents() {
        lista_jobs= (ListView) findViewById(R.id.listajobs);
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

                        getContentResolver().insert(JobDbData.CONTENT_URI, contentValues);

                            JSONArray jsonArray=jsonobject.getJSONArray("contacts");
                            for (int k = 0; k < jsonArray.length(); k++) {
                                String numerocontact=jsonArray.getString(k);
                                ContentValues contentValues2 = new ContentValues();
                                contentValues2.put(ContactDbData.COLUMN_JOB_ID,jsonobject.getInt("id"));
                                contentValues2.put(ContactDbData.COLUMN_NUMBER, numerocontact);

                                getContentResolver().insert(ContactDbData.CONTENT_URI,contentValues2);
;                            }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                fillListViewFromDB();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                Log.d("onFailure", "onFailure", throwable);
            }
        });
    }

    private void fillListViewFromDB() {


        String [] columnas={JobDbData._ID,JobDbData.COLUMN_TITLE,JobDbData.COLUMN_DESCRIPTION,JobDbData.COLUMN_POSTED_DATE};

        Cursor cursor=getContentResolver().query(JobDbData.CONTENT_URI, columnas, null, null, JobDbData._ID + " ASC");

        String [] from={JobDbData.COLUMN_TITLE,JobDbData.COLUMN_POSTED_DATE};
        int [] to={R.id.textito1,R.id.textito2};

        SimpleCursorAdapter mysimple_adapter = new SimpleCursorAdapter(this, R.layout.list_item, cursor, from, to, 0);
        lista_jobs.setAdapter(mysimple_adapter);

        while (cursor.moveToNext()){

            JobPost jobPostTemp=new JobPost();
            jobPostTemp.setId(cursor.getInt(0));
            jobPostTemp.setTitle(cursor.getString(1));
            jobPostTemp.setDescription(cursor.getString(2));
            jobPostTemp.setPostDate(cursor.getString(3));
            jobPostTemp.setContacts(getContentResolver(),jobPostTemp.getId());

            posts_array.add(jobPostTemp);
        }

    }
}
