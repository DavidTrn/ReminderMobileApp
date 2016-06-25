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
 * Created by david on 16/11/2015.
 */
public class NotiDateAdapter extends ArrayAdapter<Container>{
    private Context context;
    private int layoutID;
    ArrayList<Container> arrNoti;

    public NotiDateAdapter(Context context, int layoutID, ArrayList<Container> arrNoti) {
        super(context, layoutID, arrNoti);

        this.context = context;
        this.layoutID = layoutID;
        this.arrNoti = arrNoti;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.row, parent, false);
        TextView tvMsg = (TextView) rowView.findViewById(R.id.msg_tv);
        TextView tvTime = (TextView) rowView.findViewById(R.id.time_tv);

        tvMsg.setText(arrNoti.get(position).getTitle());
        tvTime.setText(arrNoti.get(position).getTime());

        return rowView;
    }
}
