package com.lexie.todolist.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.lexie.todolist.Model.ToDoModel;

import java.util.ArrayList;
import java.util.List;

public class DataBaseHelper extends SQLiteOpenHelper {
    private SQLiteDatabase database;
    private static final String DATABASE_NAME = "TODO_DATABASE";
    private static final String TABLE_NAME = "TO_DO_TABLE";
    private static final String COL_1 = "ID";
    private static final String COL_2 = "TASK";
    private static final String COL_3 = "STATUS";

    public DataBaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT,TASK TEXT,STATUS INTEGER)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);

    }

    public void insertTask(ToDoModel model){
        database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_2,model.getTask());
        values.put(COL_3,0);
        database.insert(TABLE_NAME,null,values);
    }
    public void updateTask(int id,String task){
        database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_2,task);
        database.update(TABLE_NAME,values,"ID+?",new String[]{String.valueOf(id)});


    }
    public void updateStatus(int id , String status){
        database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_3,status);
        database.update(TABLE_NAME,values,"ID+?",new String[]{String.valueOf(id)});


    }
    public void deleteTask(int id){
        database = this.getWritableDatabase();
        database.delete(TABLE_NAME,"ID+?" ,new String[]{String.valueOf(id)});
    }
    public List<ToDoModel> getAllTasks(){
        database = this.getWritableDatabase();
        Cursor cursor = null;
        List<ToDoModel> modelList = new ArrayList<>();
        database.beginTransaction();
        try {
            cursor = database.query(TABLE_NAME,null,null,null,null,null,null);
            if (cursor != null){
                if (cursor.moveToFirst()){
                    do {
                        ToDoModel task = new ToDoModel();
                        task.setId(cursor.getInt(cursor.getColumnIndex(COL_1)));
                        task.setTask(cursor.getString(cursor.getColumnIndex(COL_2)));
                        task.setStatus(cursor.getInt(cursor.getColumnIndex(COL_3)));
                        modelList.add(task);
                    }while (cursor.moveToNext());
                }
            }
        }finally {
            database.endTransaction();
            cursor.close();
        }
        return modelList;
    }
}
