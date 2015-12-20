package th.co.siamkubota.kubota.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.skyfishjy.library.RippleBackground;

import th.co.siamkubota.kubota.R;


/**
 * Created by sipangka on 15/6/2558.
 */
public class CustomSpinnerAdapter extends BaseAdapter {

    Context context;
    //String[] data;
    //ArrayList<Region> data;
    private static LayoutInflater inflater = null;

    private String[] itemList ;
    private int selected = -1;

    public void setItemList(String[] list){
        itemList = list;
        notifyDataSetChanged();
    }

    public int getSelected() {
        return selected;
    }

    public void setSelected(int selected) {
        this.selected = selected;
    }

    public CustomSpinnerAdapter(Context c) {
        this.context = c;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

//    void add(String path){
//        itemList.add(path);
//    }
//    void add(String path){
//        itemList[itemList.length] = path;
//    }

    void add(String path){
        itemList[itemList.length] = path;
    }

    public CustomSpinnerAdapter(Context context, String[] data) {
        // TODO Auto-generated constructor stub
        this.context = context;
        this.itemList = data;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        //return itemListle.size();
        return itemList.length;
    }

    @Override
    public String getItem(int position) {
        // TODO Auto-generated method stub
        //return data[position];
        //return itemList.get(position);
        return itemList[position];
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        View view = convertView;
        if (view == null)
            view = inflater.inflate( R.layout.item_spinner, null);
        //View view = View.inflate(context, R.layout.fav_row, null);

        /*final RippleBackground rippleBackground=(RippleBackground)view.findViewById(R.id.content);
        LinearLayout rootLayout=(LinearLayout)view.findViewById(R.id.rootLayout);
        rootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rippleBackground.startRippleAnimation();
            }
        });
*/
        TextView text = (TextView) view.findViewById(R.id.textView);
        //LinearLayout rootLayout = (LinearLayout) view.findViewById(R.id.rootLayout);
        //ImageView imageExpander = (ImageView) view.findViewById(R.id.imageExpander);
        //text.setText(itemList.get(position));
        text.setText(itemList[position]);
        //rootLayout.setBackgroundColor(context.getResources().getColor(R.color.white));
        //imageExpander.setVisibility(View.GONE);

        ImageView selectedImage = (ImageView) view.findViewById(R.id.selectedImage);

        if(selected == position){
            text.setTextColor(ContextCompat.getColor(context, R.color.dark_gray_selected));
            selectedImage.setVisibility(View.VISIBLE);
        }else{
            text.setTextColor(ContextCompat.getColor(context, R.color.dark_gray));
            selectedImage.setVisibility(View.GONE);
        }

        view.setClickable(false);
        view.setFocusable(false);
        //view.setBackgroundColor(Color.TRANSPARENT);

        return view;
    }


    @Override
    public boolean isEnabled(int position)
    {
        return true;
    }



}
