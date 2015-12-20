package th.co.siamkubota.kubota.utils.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.skyfishjy.library.RippleBackground;

import th.co.siamkubota.kubota.R;
import th.co.siamkubota.kubota.utils.function.Converter;

/**
 * Created by sipangka on 28/11/2558.
 */
public class CustomSpinnerDialog extends Dialog implements AdapterView.OnItemClickListener, View.OnClickListener
{
    private OnItemSelectedListener onItemSelectedListener;

    public DialogInterface.OnClickListener mListener;
    public Context mContext;
    //private int selected;

    public interface OnItemSelectedListener
    {
        public void onItemSelected(String itemValue);
    }

    public CustomSpinnerDialog(Context context, Adapter spinnerAdapter, DialogInterface.OnClickListener listener, int style)
    {
        super(context, R.style.FullHeightDialog);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);


        this.setContentView(R.layout.custom_spinner_dialog);
        mListener = listener;
        mContext = context;
        //this.selected = selected;

        //setDialogTitle();




        ListView listView = (ListView) this.findViewById(R.id.listview);
        listView.setAdapter((ListAdapter) spinnerAdapter);
        listView.setOnItemClickListener(this);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);


    }


    public void setOnItemSelectedListener(OnItemSelectedListener listener)
    {
        this.onItemSelectedListener = listener;
    }

    @Override
    public void onClick(View v)
    {
        if(mListener != null)
            mListener.onClick(this, DialogInterface.BUTTON_POSITIVE);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        if(mListener != null)
            mListener.onClick(this, position);
        String text = (String) parent.getItemAtPosition(position);
        onItemSelectedListener.onItemSelected(text);
    }

    public void setDialogTitle(String title)
    {
        TextView titleText = (TextView) this.findViewById(R.id.titleText);
        titleText.setText(title);
    }
}