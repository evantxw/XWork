package com.evan.xwork;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

/**
 * Created by evan on 2015/7/11.
 */
public class AccountContentProvider extends ContentProvider {

    //Uri指定，此处的字符串必须和声明的authorities一致
    public static final String AUTHORITIES = "com.evan.provider";
    //数据库名称
    public static final String DB_NAME = "account";
    //表名
    public static final String TABLE_NAME = "account";
    //访问该ContentProvider的URI
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITIES + "/account");
    //该ContentProvider所返回的数据类型定义
    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.com.evan.provider.account";
    public static final String CONTENT_TYPE_ITEM = "vnd.android.cursor.item/vnd.com.evan.provider.account";

    private static final String SQL_CREATE_ACCOUNT = "CREATE TABLE " +
            " account " +                       // Table's name
            "(" +                           // The columns in the table
            " _id INTEGER PRIMARY KEY, " +
            " url TEXT," +
            " username TEXT, " +
            " password TEXT )";

    private static final int ITEMS = 1;
    private static final int ITEM = 2;

    private static final UriMatcher mUriMatcher;

    static {
        mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        mUriMatcher.addURI(AUTHORITIES, "account", ITEMS);
        mUriMatcher.addURI(AUTHORITIES, "account/*", ITEM);
    }


    private DatabaseHelper helper;

    private SQLiteDatabase db;

    @Override
    public boolean onCreate() {
        helper = new DatabaseHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        db = helper.getReadableDatabase();
        SQLiteQueryBuilder sqlBuilder = new SQLiteQueryBuilder();
        switch (mUriMatcher.match(uri)){
            case ITEMS:
                sqlBuilder.setTables(TABLE_NAME);
                break;
            case ITEM:
                sqlBuilder.setTables(TABLE_NAME);
                //追加条件,getPathSegments()得到用户请求的Uri地址截取的数组，get(1)得到去掉地址中/以后的第二个元素
                sqlBuilder.appendWhere("_ID="+uri.getPathSegments().get(1));
                break;
        }
        Cursor cursor = sqlBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        switch (mUriMatcher.match(uri)) {
            case ITEMS: //多行数据
                return CONTENT_TYPE;
            case ITEM: //单行数据
                return CONTENT_TYPE_ITEM;
            default:
                throw new IllegalArgumentException("UnKnown URI" + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        db = helper.getWritableDatabase();
        long rowid = db.insert(DB_NAME, null, values);
        if (rowid > 0) {//添加成功
            Uri insertUri = ContentUris.withAppendedId(CONTENT_URI, rowid);
            //通知监听器，数据已经改变
            getContext().getContentResolver().notifyChange(insertUri, null);
            return insertUri;
        }
        return uri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        db = helper.getWritableDatabase();
        //得到删除的行数
        int rows = db.delete(DB_NAME, selection, selectionArgs);
        return rows;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        db = helper.getWritableDatabase();
        //执行更新语句，得到更新的条
        int rows = db.update(DB_NAME, values, selection, selectionArgs);
        return rows;
    }

    protected static final class DatabaseHelper extends SQLiteOpenHelper {

        public DatabaseHelper(Context context) {
            super(context, DB_NAME, null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(SQL_CREATE_ACCOUNT);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}
