package th.co.siamkubota.kubota.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import th.co.siamkubota.kubota.R;

/**
 * Created by sipangka on 19/06/2559.
 */
public class UnfinishHeaderAdapter extends ArrayAdapter<String> {

    private Context mContext;
    private ArrayList<String> mData;
    private int mResource;

    public UnfinishHeaderAdapter(Context context, int resource) {
        super(context, resource);

        mContext = context;
        mResource = resource;
    }


    @Override
    public void add(String object) {
        super.add(object);

        if(mData == null){
            mData = new ArrayList<String>();
        }

        mData.add(object);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        ViewHolder holder;

        if (view == null) {
            view = View.inflate(mContext, mResource, null);

            holder = new ViewHolder();
            holder.rootLayout = (LinearLayout) view.findViewById(R.id.rootLayout);
            holder.textTitle = (TextView) view.findViewById(R.id.title);

            view.setTag(holder);
        }else{
            holder = (ViewHolder) view.getTag();
        }

        String group = getItem(position);
        holder.textTitle.setText(group);


        return  view;

    }

    static class ViewHolder {
        TextView textTitle;
        LinearLayout rootLayout;
    }


}