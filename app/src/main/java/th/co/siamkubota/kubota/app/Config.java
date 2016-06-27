package th.co.siamkubota.kubota.app;

/**
 * Created by sipangka on 17/12/2557.
 */
public class Config {

    // Mockup Swagger
    //public static final String host = "http://192.168.0.52:8080";
    // Test
    public static final String host = "http://188.166.220.115";
    // Production
    //public static final String host = "http://kubota.dhamadah.com";


    //test mockup data
    public static final String base =  host + "/kubota/public/api/";
    //public static final String base = "http://192.168.0.84:8080/api/";
    public static final String mediaService = host + "/kubota/public";

    //http://kubota.dhamadah.com/public/api/login
    //http://kubota.dhamadah.com/public/api/submit-task-mobile

    /*Post data
    http://kubota.dhamadah.com/public/exampleform/submit-task-mobile
    ส่งชื่อ parameter ชื่อว่า form_data ซึ่งค่าของ form_data คือ json string ตาม URL ตัวอย่าง json string ด้านบน*/

    public static final boolean showDefault = false;

}
