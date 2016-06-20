package th.co.siamkubota.kubota.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import io.swagger.client.model.LoginData;
import io.swagger.client.model.Task;
import th.co.siamkubota.kubota.utils.function.Converter;


/**
 * Created by sipangka on 25/12/2558.
 */
public class TaskDataSource {

    // Database fields
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;


    public TaskDataSource(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }


    public long InsertData(String Tables, ContentValues Val){
        //SQLiteDatabase db;
        database = dbHelper.getWritableDatabase(); // Write Data
        try {
            long rows = database.insert(Tables, null, Val);
            return rows; // return rows inserted.

        } catch (Exception e) {
            Log.i("Database Insert " + Tables, e.toString());
            return -1;
        } finally {
            database.close();
        }
    }

    // Update Data
    public long UpdateData(String Tables, ContentValues Val, String App_Where_Table, String[] App_Where_Val) {
        // TODO Auto-generated method stub
        //SQLiteDatabase db;
        database = dbHelper.getWritableDatabase(); // Write Data
        try {
            long rows = database.update(Tables, Val, App_Where_Table, App_Where_Val);
            return rows; // return rows updated.

        } catch (Exception e) {
            return -1;
        } finally {
            database.close();
        }
    }

    public long updateTaskCode(String taskCode, String newTaskCode){

        ContentValues values = new ContentValues();
        values.put(DatabaseInfo.KEY_COL_TASK_ID, newTaskCode);

        String whereClause = DatabaseInfo.KEY_COL_TASK_ID +" = ? ";

        long row = UpdateData(DatabaseInfo.KEY_TABLE_TASK, values, whereClause, new String[] { taskCode });

        return row;
    }

    public long updateTaskId(String taskCode, String newTaskCode){

        ContentValues values = new ContentValues();
        values.put(DatabaseInfo.KEY_COL_TASK_ID, newTaskCode);

        String whereClause = DatabaseInfo.KEY_COL_TASK_ID +" = ? AND " + DatabaseInfo.KEY_COL_COMPLETE + " = ? ";

        long row = UpdateData(DatabaseInfo.KEY_TABLE_TASK, values, whereClause, new String[] { taskCode , String.valueOf(false)});

        return row;
    }

    // Select Apps Data
    public HashMap<String, String> select_DataSql(String Tables, String Selects, List<String> column) {

        //SQLiteDatabase db;
        database = dbHelper.getReadableDatabase(); // Read Data
        try {
            Cursor cursor = database.rawQuery(Selects, null);

            final HashMap<String, String> itemList = new HashMap<String, String>();
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {

                        for (int i = 0; i < column.size(); i++) {
                            itemList.put(column.get(i), cursor.getString(i));
                        }


                    } while (cursor.moveToNext());
                    return itemList;
                }
            }
            cursor.close();

        } catch (Exception e) {
            //Log.i("Database Select " + Tables, e.toString());
        } finally {
            database.close();
        }
        return null;
    }


    // Select Apps Data
    public ArrayList<HashMap<String, String>> Select_Data(String Tables, String Selects, List<String> column){

        //SQLiteDatabase db;
        database = dbHelper.getReadableDatabase(); // Read Data
        try {

            Cursor cursor = database.rawQuery(Selects, null);

            final ArrayList<HashMap<String, String>> itemList = new ArrayList<HashMap<String, String>>();
            if(cursor != null)
            {
                if (cursor.moveToFirst()) {
                    do {
                        HashMap<String, String> map = new HashMap<String, String>();

                        for(int i =0 ; i< column.size(); i++){
                            map.put(column.get(i), cursor.getString(i));
                        }

                        itemList.add(map);

                    } while (cursor.moveToNext());
                    return itemList;
                }
            }
            cursor.close();

        } catch (Exception e) {
            //Log.i("Database Select "+Tables,e.toString());
        } finally {
            database.close();
        }
        return null;

    }

    public long Delete(String keyTableApp, String whereClause, String[] strings) {
        // TODO Auto-generated method stub
        //SQLiteDatabase db;
        database = dbHelper.getWritableDatabase(); // Write Data
        try
        {
            long rows =  database.delete(keyTableApp, whereClause, strings);
            return rows; // return rows delete.

        } catch (Exception e) {
            return -1;
        } finally {
            database.close();
        }
    }

    public long deleteAll(String keyTable) {
        // TODO Auto-generated method stub
        //SQLiteDatabase db;
        database = dbHelper.getWritableDatabase(); // Write Data
        try
        {
            long rows =  database.delete(keyTable, null,null);
            return rows; // return rows delete.

        } catch (Exception e) {
            return -1;
        } finally {
            database.close();
        }
    }

    public List<Task> getAllTasks() {
        List<Task> tasktList = new ArrayList<Task>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + DatabaseInfo.KEY_TABLE_TASK;
        //String whereClause = " WHERE " + DatabaseInfo.KEY_COL_COMPLETE +" = ? ";

        //selectQuery += whereClause;

        String[] args = new String[]{String.valueOf(true)};

        database = dbHelper.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, args);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

				/*Store store = new Store();
				store.setID(Integer.parseInt(cursor.getString(0)));
				store.setName(cursor.getString(1));
				store.setPhoneNumber(cursor.getString(2));
*/

                String jsonData = cursor.getString(2);
                Gson gson = new Gson();
                Task store = gson.fromJson(jsonData, Task.class);
                // Adding contact to list
                tasktList.add(store);
            } while (cursor.moveToNext());
        }

        // return store list
        return tasktList;
    }

    public HashMap<Task, LoginData> getIncompleteTask() {

        //List<HashMap<Task, LoginData>> mapList = new ArrayList<HashMap<Task, LoginData>>();
        HashMap<Task, LoginData> map = new HashMap<Task, LoginData>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + DatabaseInfo.KEY_TABLE_TASK ;
        String whereClause = " WHERE " + DatabaseInfo.KEY_COL_COMPLETE +" = ? ";
        selectQuery += whereClause;

        String[] args = new String[]{String.valueOf(false)};

        database = dbHelper.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, args);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {


                String jsonData = cursor.getString(2);
                Gson gson = new Gson();
                Task task = gson.fromJson(jsonData, Task.class);

                String jsonData2 = cursor.getString(4);
                LoginData loginData = gson.fromJson(jsonData2, LoginData.class);

                map.put(task, loginData);

                // Adding contact to list
            } while (cursor.moveToNext());
        }

        // return store list
        return map;
    }

    public HashMap<Task, LoginData> getCompleteTask() {

        //List<HashMap<Task, LoginData>> mapList = new ArrayList<HashMap<Task, LoginData>>();
        HashMap<Task, LoginData> map = new HashMap<Task, LoginData>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + DatabaseInfo.KEY_TABLE_TASK ;
        String whereClause = " WHERE " + DatabaseInfo.KEY_COL_COMPLETE +" = ? ";
        selectQuery += whereClause;

        String[] args = new String[]{String.valueOf(true)};

        database = dbHelper.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, args);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {


                String jsonData = cursor.getString(2);
                Gson gson = new Gson();
                Task task = gson.fromJson(jsonData, Task.class);

                String jsonData2 = cursor.getString(4);
                LoginData loginData = gson.fromJson(jsonData2, LoginData.class);

                map.put(task, loginData);

                // Adding contact to list
            } while (cursor.moveToNext());
        }

        // return store list
        return map;
    }

    public HashMap<String, String> getCheckTempTask() {

        HashMap<String, String> map = new HashMap<String, String>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + DatabaseInfo.KEY_TABLE_TASK ;
        String whereClause = " WHERE " + DatabaseInfo.KEY_COL_COMPLETE +" = ? ";

        selectQuery += whereClause;

        String[] args = new String[]{String.valueOf(false)};

        database = dbHelper.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, args);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {


                String taskId = cursor.getString(1);
                String create_date = cursor.getString(3);

                map.put(taskId, create_date);

                // Adding contact to list
                //mapList.add(map);
            } while (cursor.moveToNext());
        }

        // return store list
        return map;
    }


    public void addTask(Task task) {
        //SQLiteDatabase db = this.getWritableDatabase();

        Gson gson = new Gson();
        String jsonData = gson.toJson(task, Task.class);

        ContentValues values = new ContentValues();
        values.put(DatabaseInfo.KEY_COL_TASK_ID, task.getTaskId());
        values.put(DatabaseInfo.KEY_COL_TASK_DETAIL, jsonData);
        values.put(DatabaseInfo.KEY_COL_CREATE_DATE, Converter.DateToString(new Date(), "dd/MM/yyyy HH:mm:ss"));
        values.put(DatabaseInfo.KEY_COL_COMPLETE, task.getComplete().toString());

        // Inserting Row
//		db.insert(DatabaseInfo.KEY_TABLE_STORE, null, values);
//		db.close(); // Closing database connection

        InsertData(DatabaseInfo.KEY_TABLE_TASK, values);
    }

    public void addIncompleteTask(Task task, LoginData loginData) {
        //SQLiteDatabase db = this.getWritableDatabase();

        Gson gson = new Gson();
        String jsonData = gson.toJson(task, Task.class);
        String jsonDataLogin = gson.toJson(loginData, LoginData.class);
        String task_id = task.getTaskId();


        ContentValues values = new ContentValues();
        values.put(DatabaseInfo.KEY_COL_TASK_ID, task_id);
        values.put(DatabaseInfo.KEY_COL_TASK_DETAIL, jsonData);
        values.put(DatabaseInfo.KEY_COL_CREATE_DATE, Converter.DateToString(new Date(), "dd/MM/yyyy HH:mm:ss"));
        values.put(DatabaseInfo.KEY_COL_COMPLETE, task.getComplete().toString());
        values.put(DatabaseInfo.KEY_COL_LOGIN_DETAIL, jsonDataLogin);

        // Inserting Row
//		db.insert(DatabaseInfo.KEY_TABLE_STORE, null, values);
//		db.close(); // Closing database connection

        InsertData(DatabaseInfo.KEY_TABLE_TASK, values);
    }


    public void addAllTasks(List<Task> tasks) {

        for(Task t : tasks){
            addTask(t);
        }
    }

	public long deleteTask(String taskId){

        String whereClause = DatabaseInfo.KEY_COL_TASK_ID +" = ? ";
		long mData = Delete(DatabaseInfo.KEY_TABLE_TASK, whereClause, new String[]{taskId});

		return mData;
	}



}
