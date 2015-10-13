package sinapsysit.com.thejobsproject;

import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import sinapsysit.com.thejobsproject.pojos.JobPost;

public class DetalleActivity extends AppCompatActivity {

    TextView txtTitulo,txtDetalle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txtTitulo= (TextView) findViewById(R.id.txtTitulo);
        txtDetalle= (TextView) findViewById(R.id.txtDetalle);


        Bundle b=getIntent().getExtras();
        JobPost jp= (JobPost) b.getSerializable("job_selected");


        txtTitulo.setText(jp.getTitle());
        txtDetalle.setText(jp.getDescription());

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                System.out.println("PRESIONASTE BACK en detalle");
//                NavUtils.navigateUpFromSameTask(this);
                super.onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
