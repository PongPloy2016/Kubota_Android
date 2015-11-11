package th.co.siamkubota.kubota.app;

/**
 * Created by sipangka on 17/12/2557.
 */
public class Config {

    // Mockup Swagger
    //public static final String host = "http://192.168.0.52:8080";
    // Test
    //public static final String host = "http://192.168.0.86:3000";
    // Production
    public static final String host = "http://49.231.24.111:3000";


    //test mockup data
    //public static final String base = "https://rss.ais.co.th/mobileappdep/GreenSpot/datamockup/";
    public static final String base =  host + "/api/";
    //public static final String base = "http://192.168.0.84:8080/api/";
    public static final String mediaService = base + "media/";


}
