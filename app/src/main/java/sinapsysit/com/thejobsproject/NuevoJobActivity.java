package sinapsysit.com.thejobsproject;

import android.app.AlertDialog;
import android.content.DialogInterface;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.*;


import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;

public class NuevoJobActivity extends AppCompatActivity {
    EditText txt_titulo,txt_descripcion,txt_contactos;
    private static final String URLPOSTS="http://dipandroid-ucb.herokuapp.com/work_posts.json";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_job);
        txt_titulo= (EditText) findViewById(R.id.txtTitulo);
        txt_descripcion= (EditText) findViewById(R.id.txtDescripcion);
        txt_contactos= (EditText) findViewById(R.id.txtContacto);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                System.out.println("PRESIONASTE BACK en NUEVOJOB");
                super.onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void saveJob(View view) throws JSONException, UnsupportedEncodingException {
        AsyncHttpClient cliente=new AsyncHttpClient();
        String data_titulo,data_desc,data_contacts;
        data_titulo=txt_titulo.getText().toString();
        data_desc=txt_descripcion.getText().toString();
        data_contacts=txt_contactos.getText().toString();




        JSONObject posjson=new JSONObject();
        posjson.put("title",data_titulo);
        posjson.put("description", data_desc);

        posjson.put("contacts", new JSONArray(Arrays.toString(data_contacts.split(","))));

        JSONObject mydata=new JSONObject();
        mydata.put("work_post", posjson);


        System.out.println(Arrays.toString(data_contacts.split(",")));
        System.out.println("EL JSON A ENVIAR");
        System.out.println(mydata.toString());


        StringEntity entity=new StringEntity(mydata.toString());
        entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));

     cliente.post(this,URLPOSTS,entity,"application/json",new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                try {
                    if(response.getInt("id")>0){
                        AlertDialog.Builder builder=new AlertDialog.Builder(NuevoJobActivity.this);
                        builder.setTitle("Exito !").setMessage("Se creo el nuevo post en el servidor con exito!");
                        builder.setPositiveButton("Volver al listado", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                onBackPressed();
                            }
                        });
                        AlertDialog dialog=builder.create();

                        dialog.show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("onFailure", "onFailure", throwable);
            }

        });
    }
}
