package th.co.siamkubota.kubota.logger;

import android.util.Log;

public  class Logger {
    private static final  boolean IS_ENABLE = true;

    public  static  void Log (String name, String text){
        if( IS_ENABLE ){
           // name.replaceAll(str, str1);i
            name.replace("", "*$\n");
            text.replace("", "$\n");
            Log.e(name,text);

        }
    }


    public static String DESCRIBABLE_KEY  (String data) {
        String DESCRIBABLE_KEY = "success";

        return  DESCRIBABLE_KEY ;
    }




}
