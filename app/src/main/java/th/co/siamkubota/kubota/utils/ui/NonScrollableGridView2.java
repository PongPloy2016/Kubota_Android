package th.co.siamkubota.kubota.utils.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * Created by atthapok on 13/09/2558.
 */
public class NonScrollableGridView2 extends GridView {
    public NonScrollableGridView2(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // Do not use the highest two bits of Integer.MAX_VALUE because they are
        // reserved for the MeasureSpec mode
        int heightSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, heightSpec);
        getLayoutParams().height = getMeasuredHeight();
    }

/*
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if(changed) {
            ProductUnitAdapter adapter = (ProductUnitAdapter)getAdapter();

            //int numColumns = getContext().getResources().getInteger(R.integer.list_num_columns);
            int numColumns = 3;
            GridViewItemLayout.initItemLayout(numColumns, adapter.getCount());

            if(numColumns > 1) {
                int columnWidth = getMeasuredWidth() / numColumns;
                adapter.measureItems(columnWidth);
            }
        }

    }*/

    /*
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        ProductUnitAdapter adapter = null;
        ListAdapter listadapter = getAdapter();
        if(listadapter instanceof  ProductUnitAdapter)
            adapter = (ProductUnitAdapter)listadapter;
        else if(listadapter instanceof  AlphaInAnimationAdapter)
        {
            AlphaInAnimationAdapter alphaAdapter = (AlphaInAnimationAdapter)listadapter;
            adapter = (MyCardGridArrayAdapter) alphaAdapter.getDecoratedBaseAdapter();
        }

        if(adapter != null)
        {
            int numColumns = this.getNumColumns();
            MyCardView.initItemLayout(numColumns, adapter.getCount());

            if(numColumns > 1) {
                int columnWidth = getMeasuredWidth() / numColumns;
                adapter.measureItems(columnWidth);
            }
        }

        super.onLayout(changed, l, t, r, b);
    }*/

}