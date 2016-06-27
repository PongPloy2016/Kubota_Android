package th.co.siamkubota.kubota.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import io.swagger.client.model.Task;
import th.co.siamkubota.kubota.R;
import th.co.siamkubota.kubota.fragment.UnfinishTaskFragment;
import th.co.siamkubota.kubota.model.OfflineTask;

/**
 * Created by sipangka on 15/11/2558.
 */
public class UnfinishTaskAdapter extends BaseAdapter {

    Context context;

    ArrayList<OfflineTask> data;
    private static LayoutInflater inflater = null;

    private View.OnClickListener onClickListener;

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public UnfinishTaskAdapter(Context context, ArrayList<OfflineTask> data) {
        // TODO Auto-generated constructor stub
        this.context = context;
        this.data = data;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public ArrayList<OfflineTask> getData() {
        return data;
    }

    public void setData(ArrayList<OfflineTask> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        //return data[position];
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        final ViewHolder holder;

        View view = convertView;
        if (view == null) {
            view = View.inflate(context, R.layout.item_unfinish_task, null);
            holder = new ViewHolder();

            holder.statusImage = (ImageView) view.findViewById(R.id.statusImage);
            holder.titleText = (TextView) view.findViewById(R.id.titleText);
            holder.viewButton = (ImageButton) view.findViewById(R.id.viewButton);
            holder.deleteButton = (ImageButton) view.findViewById(R.id.deleteButton);

            view.setTag(holder);
        } else {
            // Get the ViewHolder back to get fast access to the TextView
            // and the ImageView.
            holder = (ViewHolder) view.getTag();
        }

        OfflineTask task = data.get(position);

        if(task.getTask().getComplete()){
            //holder.rootLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.rectangle_round_corner_gray));
            holder.statusImage.setImageResource(R.drawable.bullet_active);
        }else{
            holder.statusImage.setImageResource(R.drawable.bullet_inactive);
        }

        String title = task.getTask().getTaskInfo().getTaskCode();

        if( title != null && !title.isEmpty()  ){
            holder.titleText.setText(title);
        }else{
            holder.titleText.setText(context.getText(R.string.unfinish_task_number) + " " + (position + 1));
        }



        holder.deleteButton.setTag(R.id.key, UnfinishTaskFragment.KEY_DELETE);
        holder.deleteButton.setTag(R.id.value, data.get(position));
        holder.deleteButton.setOnClickListener(onClickListener);

        holder.viewButton.setTag(R.id.key, UnfinishTaskFragment.KEY_VIEW);
        holder.viewButton.setTag(R.id.value, data.get(position));
        holder.viewButton.setOnClickListener(onClickListener);


        view.setClickable(false);
        view.setFocusable(false);

        return view;
    }



    @Override
    public boolean isEnabled(int position)
    {
        return true;
    }

    static class ViewHolder {
        TextView titleText;
        ImageView statusImage;
        ImageButton viewButton;
        ImageButton deleteButton;
    }

}
