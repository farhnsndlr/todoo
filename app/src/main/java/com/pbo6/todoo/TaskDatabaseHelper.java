package com.pbo6.todoo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class TaskDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "tasks_db";
    private static final int DATABASE_VERSION = 1;
    public static final String TABLE_TASKS = "tasks";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_COMPLETED = "completed";

    public TaskDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_TASKS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_COMPLETED + " INTEGER)";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
        onCreate(db);
    }

    // Save task to the database
    public void saveTask(Task task) {
        try (SQLiteDatabase db = this.getWritableDatabase()) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_NAME, task.getTaskName());
            values.put(COLUMN_COMPLETED, task.isCompleted() ? 1 : 0);
            db.insertOrThrow(TABLE_TASKS, null, values);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Retrieve all tasks from the database
    public List<Task> getAllTasks() {
        List<Task> taskList = new ArrayList<>();
        try (SQLiteDatabase db = this.getReadableDatabase();
             Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_TASKS, null)) {

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
                    String taskName = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
                    boolean isCompleted = cursor.getInt(cursor.getColumnIndex(COLUMN_COMPLETED)) == 1;
                    Task task = new Task(id, taskName, isCompleted);
                    taskList.add(task);
                } while (cursor.moveToNext());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return taskList;
    }

    // Update task completion status
    public void updateTask(Task task) {
        try (SQLiteDatabase db = this.getWritableDatabase()) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_COMPLETED, task.isCompleted() ? 1 : 0);
            db.update(TABLE_TASKS, values, COLUMN_ID + " = ?", new String[]{String.valueOf(task.getId())});
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Delete task from the database
    public void deleteTask(Task task) {
        try (SQLiteDatabase db = this.getWritableDatabase()) {
            int rowsDeleted = db.delete(TABLE_TASKS, COLUMN_ID + " = ?", new String[]{String.valueOf(task.getId())});
            if (rowsDeleted == 0) {
                System.out.println("Task not found for deletion");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
