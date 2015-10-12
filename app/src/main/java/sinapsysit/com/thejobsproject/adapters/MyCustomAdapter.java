package sinapsysit.com.thejobsproject.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import sinapsysit.com.thejobsproject.R;
import sinapsysit.com.thejobsproject.pojos.JobPost;

/**
 * Created by jujomago on 12/10/2015.
 */
public class MyCustomAdapter extends ArrayAdapter<JobPost> {

    private Context contexto;
    private int layoutId;



    public MyCustomAdapter(Context context, int resource, List<JobPost> objects) {
        super(context, resource, objects);
        context=context;
        layoutId=resource;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View item_row=convertView;
        JobItemHolder jobItemHolder=null;

        if(item_row==null){
            LayoutInflater inflater=(LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            item_row=inflater.inflate(layoutId,parent,false);

            jobItemHolder=new JobItemHolder();

            jobItemHolder.texto1= (TextView) item_row.findViewById(R.id.textito1);
            jobItemHolder.texto2= (TextView) item_row.findViewById(R.id.textito2);

            item_row.setTag(jobItemHolder);

        }else{
            jobItemHolder= (JobItemHolder) item_row.getTag();

        }
        JobPost thejob=getItem(position);

        jobItemHolder.texto1.setText(thejob.getTitle());
        jobItemHolder.texto2.setText(thejob.getPostDate());

        return item_row;
    }

    static class JobItemHolder{
        TextView texto1;
        TextView texto2;
    }
}
