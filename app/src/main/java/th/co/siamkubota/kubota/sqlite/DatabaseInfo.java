package th.co.siamkubota.kubota.sqlite;

/**
 * Created by sipangka on 7/10/2558.
 */
public class DatabaseInfo {

    public static final String DATABASE_NAME = "Kubota";
    public static final int DATABASE_VERSION = 1;

    public static final String KEY_TABLE_TASK = "task";

    public static final String KEY_COL_ID = "_id";
    public static final String KEY_COL_TASK_ID = "task_id";
    public static final String KEY_COL_TASK_DETAIL = "task_detail";
    public static final String KEY_COL_CREATE_DATE = "create_date";


    // Table Store
    public static final String createCommandTableTask = "CREATE TABLE " + DatabaseInfo.KEY_TABLE_TASK +
            "("+DatabaseInfo.KEY_COL_ID +" INTEGER AUTO_INCREMENT PRIMARY KEY," +
            DatabaseInfo.KEY_COL_TASK_ID + " TEXT NOT NULL UNIQUE ON CONFLICT REPLACE," +
            DatabaseInfo.KEY_COL_TASK_DETAIL + " TEXT NOT NULL," +
            DatabaseInfo.KEY_COL_CREATE_DATE + " TEXT NOT NULL);" ;
}
