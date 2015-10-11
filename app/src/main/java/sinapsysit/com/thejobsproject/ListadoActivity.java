package sinapsysit.com.thejobsproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class ListadoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menuapp, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_settings:
                Toast.makeText(this, "Primera Opcion", Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_settings2:
                Toast.makeText(this,"Segunda Opcion",Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(this,"Tercera Opcion",Toast.LENGTH_SHORT).show();

        }

        return true;
    }


}
