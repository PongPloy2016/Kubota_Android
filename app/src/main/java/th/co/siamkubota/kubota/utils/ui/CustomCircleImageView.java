package th.co.siamkubota.kubota.utils.ui;

import android.content.Context;
import android.util.AttributeSet;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by atthapok on 27/09/2558.
 */
public class CustomCircleImageView extends CircleImageView {

    public CustomCircleImageView(Context context) {
        super(context);
    }

    public CustomCircleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomCircleImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setScaleType(ScaleType scaleType) {
//        if(scaleType != SCALE_TYPE) {
//            throw new IllegalArgumentException(String.format("ScaleType %s not supported.", new Object[]{scaleType}));
//        }

       // super.setScaleType(scaleType);

        this.setScaleType(scaleType);
    }

    @Override
    public void setAdjustViewBounds(boolean adjustViewBounds) {
//        if(adjustViewBounds) {
//            throw new IllegalArgumentException("adjustViewBounds not supported.");
//        }

        //super.setAdjustViewBounds(adjustViewBounds);
        this.setAdjustViewBounds(adjustViewBounds);
    }
}

