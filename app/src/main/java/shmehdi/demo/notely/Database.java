package shmehdi.demo.notely;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rizz on 05-02-2018.
 */

public class Database {

    Context context;
    MyDBHelper myDBHelper;

    public Database(Context context) {
        this.context = context;
        this.myDBHelper = new MyDBHelper(context);
    }

    public long saveNote(String title,String note,String category){
        SQLiteDatabase database = myDBHelper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(MyDBHelper.TITLE,title);
        cv.put(MyDBHelper.NOTE,note);
        cv.put(MyDBHelper.IS_STAR,"NO");
        cv.put(MyDBHelper.IS_FAV,"NO");
        cv.put(MyDBHelper.CATEGORY,category);
        cv.put(MyDBHelper.DATE,MyFunction.getCurrentDate());
        cv.put(MyDBHelper.TIME,MyFunction.getCurrentTime());

        long i = database.insert(MyDBHelper.TABLE_NAME,null,cv);

        return i;
    }

    public List<NoteModel> fetchNotes(){
        List<NoteModel> noteModelList = new ArrayList<>();
        NoteModel noteModel;
        SQLiteDatabase database = myDBHelper.getReadableDatabase();
        String[] columns = {MyDBHelper.NOTE_ID,MyDBHelper.TITLE,MyDBHelper.NOTE,MyDBHelper.IS_FAV,MyDBHelper.IS_STAR,MyDBHelper.CATEGORY,MyDBHelper.DATE,MyDBHelper.TIME};
        Cursor cursor = database.query(MyDBHelper.TABLE_NAME,columns,null,null,null,null,null);

        while (cursor.moveToNext()){
           // Log.d("query", cursor.getString(cursor.getColumnIndex(MyDBHelper.NOTE)));
            noteModel = new NoteModel();
            noteModel.setNoteID(cursor.getString(cursor.getColumnIndex(MyDBHelper.NOTE_ID)));
            noteModel.setTitle(cursor.getString(cursor.getColumnIndex(MyDBHelper.TITLE)));
            noteModel.setNote(cursor.getString(cursor.getColumnIndex(MyDBHelper.NOTE)));
            noteModel.setFav(cursor.getString(cursor.getColumnIndex(MyDBHelper.IS_FAV)));
            noteModel.setStar(cursor.getString(cursor.getColumnIndex(MyDBHelper.IS_STAR)));
            noteModel.setCategory(cursor.getString(cursor.getColumnIndex(MyDBHelper.CATEGORY)));
            noteModel.setDate(cursor.getString(cursor.getColumnIndex(MyDBHelper.DATE)));
            noteModel.setTime(cursor.getString(cursor.getColumnIndex(MyDBHelper.TIME)));

            noteModelList.add(noteModel);
        }

        return noteModelList;

    }

    public void fetchFilterNotes(){
        SQLiteDatabase database = myDBHelper.getReadableDatabase();
        String[] columns = {MyDBHelper.NOTE_ID,MyDBHelper.TITLE,MyDBHelper.NOTE,MyDBHelper.IS_FAV,MyDBHelper.IS_STAR,MyDBHelper.CATEGORY,MyDBHelper.DATE,MyDBHelper.TIME};
        //database.query(MyDBHelper.TABLE_NAME,columns,MyDBHelper.)
    }

    public void deleteNote(String noteID){
        SQLiteDatabase database = myDBHelper.getWritableDatabase();
        int i = database.delete(MyDBHelper.TABLE_NAME,MyDBHelper.NOTE_ID + "=" + noteID,null);
        if(i>0)
            Log.d("delete", "Deleted");
        else
             Log.d("delete", "Deleteion failed");

    }

    public int updateNote(String noteID, String title, String note){
        ContentValues cv = new ContentValues();
        cv.put(MyDBHelper.TITLE,title);
        cv.put(MyDBHelper.NOTE,note);
        cv.put(MyDBHelper.DATE,MyFunction.getCurrentDate());
        cv.put(MyDBHelper.TIME,MyFunction.getCurrentTime());
        SQLiteDatabase database = myDBHelper.getWritableDatabase();
        return database.update(MyDBHelper.TABLE_NAME,cv,MyDBHelper.NOTE_ID + "=" + noteID,null);

    }

    public int setFav(String noteID, String tag){
        ContentValues cv = new ContentValues();
        cv.put(MyDBHelper.IS_FAV,tag);
        SQLiteDatabase database = myDBHelper.getWritableDatabase();
        return database.update(MyDBHelper.TABLE_NAME,cv,MyDBHelper.NOTE_ID +"="+noteID,null );
    }
    public int setStar(String noteID, String tag){
        ContentValues cv = new ContentValues();
        cv.put(MyDBHelper.IS_STAR,tag);
        SQLiteDatabase database = myDBHelper.getWritableDatabase();
        return database.update(MyDBHelper.TABLE_NAME,cv,MyDBHelper.NOTE_ID +"="+noteID,null );
    }

    static class MyDBHelper extends SQLiteOpenHelper{

        private final static String DATABASE_NAME = "notes";
        private final static int DATABASE_VERSION = 1;
        private final static String TABLE_NAME = "notes";
        //COULMNS
        public final static String NOTE_ID = "noteId";
        public final static String TITLE = "title";
        public final static String NOTE = "note";
        public final static String IS_STAR = "isStar";
        public final static String IS_FAV = "isFav";
        public final static String CATEGORY = "category";
        public final static String DATE = "date";
        public final static String TIME = "time";
        Context context;
        public MyDBHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            this.context = context;
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            if(!doesTableExist(sqLiteDatabase,TABLE_NAME)){
                createNotesTable(sqLiteDatabase);
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        }


        private void createNotesTable(SQLiteDatabase db){
            String query = "CREATE TABLE " + TABLE_NAME + "(" + NOTE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    TITLE + " VARCHAR(255), " + NOTE + " VARCHAR(255), " + IS_STAR + " VARCHAR(255), " +
                    IS_FAV + " VARCHAR(255), " + CATEGORY + " VARCHAR (255)," + DATE + " VARCHAR (255)," + TIME + " VARCHAR(255))";
            Log.e("Test", query);
            try{
                db.execSQL(query);
                Log.e("Test", "Table created");
            }catch (Exception e){
                Log.e("Test", "Failed to create table : " + e.toString());
            }
        }

        public boolean doesTableExist(SQLiteDatabase db, String tableName) {
            Cursor cursor = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '" + tableName + "'", null);

            if (cursor != null) {
                if (cursor.getCount() > 0) {
                    cursor.close();
                    return true;
                }
                cursor.close();
            }
            return false;
        }
    }
}
