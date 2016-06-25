package ttcnpm.cse.hcmut.reminder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by david on 23/10/2015.
 */
public class MyAdapter extends ArrayAdapter<Function>{

    private Context context;
    private int layoutID;
    ArrayList<Function> arrOpt;

    public MyAdapter(Context context, int layoutID, ArrayList<Function> arrOpt) {
        super(context, layoutID, arrOpt);

        this.context = context;
        this.layoutID = layoutID;
        this.arrOpt = arrOpt;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.drawer_list_item, parent, false);
        ImageView ivOpt = (ImageView) rowView.findViewById(R.id.ivOpt);
        TextView tvName = (TextView) rowView.findViewById(R.id.tvName);

        if (arrOpt.get(position).opt == 0) {
            ivOpt.setImageResource(R.drawable.create);
        }
        else if (arrOpt.get(position).opt == 1) {
            ivOpt.setImageResource(R.drawable.about);
        }
        else {
            ivOpt.setImageResource(R.drawable.settings);
        }

        tvName.setText(arrOpt.get(position).nameOfOption);

        return rowView;
    }
}


