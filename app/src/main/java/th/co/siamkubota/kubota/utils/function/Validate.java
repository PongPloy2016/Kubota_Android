package th.co.siamkubota.kubota.utils.function;

import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;

/**
 * Created by atthapok on 29/09/2558.
 */
public class Validate {

    public static View inputEditTextEmpty(View view){
        if(view instanceof EditText){
            EditText editText = (EditText) view;

            if(editText.length() <= 0 || editText.getText().toString().isEmpty()){
                return editText;
            }
        }else if (view instanceof ViewGroup) {

            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {

                View innerView = ((ViewGroup) view).getChildAt(i);
                View resultView = inputEditTextEmpty(innerView);
                if(resultView != null){
                    return resultView;
                }
            }
        }

        return null;

    }

    public static View inputRadioGroupUncheck(View view){
        if(view instanceof RadioGroup){
            RadioGroup radioGroup = (RadioGroup) view;

            int id = radioGroup.getCheckedRadioButtonId();
            if (id == -1){
                return radioGroup;
            }

        }else if (view instanceof ViewGroup) {

            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {

                View innerView = ((ViewGroup) view).getChildAt(i);
                View resultView = inputRadioGroupUncheck(innerView);
                if(resultView != null){
                    return resultView;
                }
            }
        }

        return null;

    }

    public static View inputSpinnerNoneSelected(View view){
        if(view instanceof Spinner){
            Spinner spinner = (Spinner) view;

            if(spinner.getSelectedItem() == null){
                return spinner;
            }

        }else if (view instanceof ViewGroup) {

            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {

                View innerView = ((ViewGroup) view).getChildAt(i);
                View resultView = inputSpinnerNoneSelected(innerView);
                if(resultView != null){
                    return resultView;
                }
            }
        }

        return null;

    }

    public static View inputValidate(View view){

        if(view.getVisibility() == View.VISIBLE && (view instanceof EditText || view instanceof RadioGroup || view instanceof Spinner || view instanceof CheckBox)) {

            if(view instanceof EditText ) {
                EditText editText = (EditText) view;

                if (editText.length() <= 0 || editText.getText().toString().isEmpty()) {
                    return editText;
                }
            }else if(view instanceof RadioGroup){
                RadioGroup radioGroup = (RadioGroup) view;

                int id = radioGroup.getCheckedRadioButtonId();
                if (id == -1){
                    return radioGroup;
                }

            }else if(view instanceof Spinner){
                Spinner spinner = (Spinner) view;

                if(spinner.getSelectedItem() == null){
                    return spinner;
                }
            }else if(view instanceof CheckBox){
                CheckBox checkBox = (CheckBox) view;
                if(!checkBox.isChecked()){
                    return checkBox;
                }
            }

        }else if (view instanceof ViewGroup) {

            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {

                View innerView = ((ViewGroup) view).getChildAt(i);
                View resultView = inputValidate(innerView);
                if(resultView != null){
                    return resultView;
                }
            }
        }

        return null;

    }

    public static View inputValidate(View view, Object required){

        if(view.getVisibility() == View.VISIBLE && (view instanceof EditText || view instanceof RadioGroup || view instanceof Spinner || view instanceof CheckBox)) {

            if(view instanceof EditText ) {
                EditText editText = (EditText) view;

                if ((editText.getTag() != null && editText.getTag().toString().equals(required.toString()))
                        && (editText.length() <= 0 || editText.getText().toString().isEmpty())  ) {
                    return editText;
                }
            }else if(view instanceof RadioGroup){
                RadioGroup radioGroup = (RadioGroup) view;

                int id = radioGroup.getCheckedRadioButtonId();
                if (id == -1 &&  radioGroup.getTag() != null && radioGroup.getTag().toString().equals(required.toString())){
                    return radioGroup;
                }

            }else if(view instanceof Spinner){
                Spinner spinner = (Spinner) view;

                if(spinner.getSelectedItem() == null &&  spinner.getTag() != null && spinner.getTag().toString().equals(required.toString()) ){
                    return spinner;
                }
            }else if(view instanceof CheckBox){
                CheckBox checkBox = (CheckBox) view;
                if(!checkBox.isChecked() &&  checkBox.getTag() != null && checkBox.getTag().toString().equals(required.toString())){
                    return checkBox;
                }
            }

        }else if (view instanceof ViewGroup) {

            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {

                View innerView = ((ViewGroup) view).getChildAt(i);
                View resultView = inputValidate(innerView, required);
                if(resultView != null){
                    return resultView;
                }
            }
        }

        return null;

    }
}
