package th.co.siamkubota.kubota.utils.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import th.co.siamkubota.kubota.R;
import th.co.siamkubota.kubota.adapter.CustomSpinnerAdapter;
import th.co.siamkubota.kubota.adapter.SelectNoneSpinnerAdapter;


/**
 * Created by sipangka on 28/11/2558.
 */
public class CustomSpinner extends Spinner implements DialogInterface.OnClickListener,CustomSpinnerDialog.OnItemSelectedListener
{
    public Context mContext;
    public String[] mDataList;
    private String prompt;

    private CustomSpinnerAdapter adapter;
    private CustomSpinnerDialog dialog;
    private int selected = -1;

    public CustomSpinner(Context context, AttributeSet attrs)
    {
        super(context, attrs, R.style.SpinnerStyle);
        this.mContext = context;

        this.prompt = super.getPrompt().toString();

    }



    @Override
    public boolean performClick()
    {
        boolean handled = false;
        if (!handled)
        {
            handled = true;

            adapter = (CustomSpinnerAdapter)((SelectNoneSpinnerAdapter)getAdapter()).getAdapter();
            adapter.setSelected(selected);

            dialog = new CustomSpinnerDialog(mContext,  adapter, this, R.style.FullHeightDialog);
            //dialog.setDialogTitle(mContext.getResources().getString((R.string.my_dialog_text)));
            dialog.setDialogTitle(this.prompt);
            dialog.setOnItemSelectedListener(this);
            dialog.show();
        }
        return handled;
    }

    @Override
    public void onClick(DialogInterface dialog, int which)
    {
        setSelection(which + 1);
        dialog.dismiss();

        this.selected = which;

    }

    @Override
    public void onItemSelected(String itemValue) {

    }

    @Override
    public String getPrompt() {
        return prompt;
    }


    @Override
    public void setPrompt(CharSequence prompt) {
        super.setPrompt(prompt);

        this.prompt = prompt.toString();
        if(dialog != null){
            dialog.setDialogTitle(this.prompt);
        }
    }
}

