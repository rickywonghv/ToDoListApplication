package xyz.damonwong.todolist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

/**
 * Created by Damon on 4/11/2016.
 */

public class DbHelper extends SQLiteOpenHelper {
    private static final String DB_NAME="ToDo_WongChunWai";
    DbHelper dbOpenHelper;
    public static final int VERSION = 1;
    SQLiteDatabase db;
    private static SQLiteDatabase database;

    public DbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public static SQLiteDatabase getDatabase(Context context) {
        if (database == null || !database.isOpen()) {
            database = new DbHelper(context, DB_NAME,
                    null, VERSION).getWritableDatabase();
        }

        return database;
    }

    /* Inner class that defines the table contents */
    public static abstract class ItemEntry implements BaseColumns {
        public static final String TABLE_NAME = "items";
        public static final String id= "id";
        public static final String cont= "cont";
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        SQLiteDatabase db=sqLiteDatabase;
        //Create Class Status
        db.execSQL("CREATE TABLE IF NOT EXISTS "+
                ItemEntry.TABLE_NAME+" ("+
                ItemEntry.id +" INTEGER PRIMARY KEY AUTOINCREMENT,"+
                ItemEntry.cont+" VARCHAR(100) NOT NULL)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean insertItem(String cont){
        db=this.getWritableDatabase();
        ContentValues itemVal=new ContentValues();
        itemVal.put(ItemEntry.cont,cont);
        long res=db.insert(ItemEntry.TABLE_NAME,null,itemVal);
        if(res==-1){
            return false;
        }else{
            return true;
        }
    }

    public boolean deleteItem(int id){
        db=this.getWritableDatabase();
        long res=db.delete(ItemEntry.TABLE_NAME,ItemEntry.id+"="+id,null);
        Log.d("res", String.valueOf(res));
        if(res==-1){
            return false;
        }else{
            return true;
        }
    }

    public Cursor getAllItems(){
        db=this.getWritableDatabase();
        String query="SELECT * FROM "+ItemEntry.TABLE_NAME;
        return db.rawQuery(query,null);
    }
}
