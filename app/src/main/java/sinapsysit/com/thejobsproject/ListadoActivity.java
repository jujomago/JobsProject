package sinapsysit.com.thejobsproject;

import android.app.ActionBar;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import sinapsysit.com.thejobsproject.adapters.MyCustomAdapter;
import sinapsysit.com.thejobsproject.data.JobsDbHelper;
import sinapsysit.com.thejobsproject.pojos.JobPost;

public class ListadoActivity extends AppCompatActivity {

    static final String URLPOSTS="http://dipandroid-ucb.herokuapp.com/work_posts.json";
    ArrayList<JobPost> posts_array;
    ListView lista_jobs;
    MyCustomAdapter lista_adapter;
//    ArrayAdapter<JobPost> lista_adapter;
    ArrayAdapter<String> lista_adapter2;
//    ArrayList<String> posts_array2;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        posts_array=new ArrayList<>();
//        posts_array2=new ArrayList<>();
        createComponents();
        cargarLista();
        JobsDbHelper jobs_db_helper=new JobsDbHelper(this,"jobsDB",null);
        db = jobs_db_helper.getWritableDatabase();

    }

    private void createComponents() {
        lista_jobs= (ListView) findViewById(R.id.listajobs);

        lista_adapter=new MyCustomAdapter(getBaseContext(),R.layout.list_item,posts_array);

//      lista_adapter=new ArrayAdapter<>(getBaseContext(),android.R.layout.simple_list_item_2,android.R.id.text1,posts_array);
        lista_jobs.setAdapter(lista_adapter);

        lista_jobs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle b=new Bundle();
                b.putSerializable("job_selected", posts_array.get(position));

                Intent myintent=new Intent(getApplicationContext(),DetalleActivity.class);
                myintent.putExtras(b);

                startActivity(myintent);
            }
        });



    }

    private void cargarLista(){
        AsyncHttpClient cliente=new AsyncHttpClient();

        cliente.get(URLPOSTS,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {

                for (int i = 0; i < response.length(); i++) {
                    JobPost jobPost = new JobPost();
                    try {
                        JSONObject jsonobject = response.getJSONObject(i);
                        jobPost.setId(jsonobject.getInt("id"));
                        jobPost.setTitle(jsonobject.getString("title"));
                        jobPost.setPostDate(jsonobject.getString("posted_date"));
                        jobPost.setDescription(jsonobject.getString("description"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    posts_array.add(jobPost);
//                    posts_array2.add(jobPost.getTitle());
      /*              System.out.println("SIZE ONsUcESS:");
                    System.out.println(posts_array2.size());*/
                }
                lista_adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                Log.d("onFailure", "onFailure", throwable);
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
                Toast.makeText(this, "Primera Opcion", Toast.LENGTH_SHORT).show();
                break;
            case R.id.postear:
                Intent myintent=new Intent(this,DetalleActivity.class);
                startActivity(myintent);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }


}
