package com.evan.xwork;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.evan.xtool.ui.TDialog;

public class AccountActivity extends Activity {

    ListView account_lv;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        account_lv = (ListView) findViewById(R.id.account_lv);
        button = (Button) findViewById(R.id.btn1);

        refresh_listView();

        account_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor c = (Cursor) parent.getItemAtPosition(position);

                String[] account = new String[4];

                account[0] = String.valueOf(c.getInt(c.getColumnIndex("_id")));
                account[1] = c.getString(c.getColumnIndex("url"));
                account[2] = c.getString(c.getColumnIndex("username"));
                account[3] = c.getString(c.getColumnIndex("password"));

                Intent intent = new Intent(AccountActivity.this, AddAccountActivity.class);
                intent.putExtra("account", account);
                startActivity(intent);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TDialog dialog = new TDialog(AccountActivity.this);
                dialog.show();
            }
        });
        button.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
        button.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                return false;
            }
        });

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    private void refresh_listView() {
        ContentResolver cr = getContentResolver();
        Cursor cursor = cr.query(AccountContentProvider.CONTENT_URI, null, null, null, null);

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.account_item, cursor, new String[]{"url", "username", "password"}, new int[]{R.id.account_url, R.id.account_username, R.id.account_password}, 1);
        account_lv.setAdapter(adapter);
    }

    View.OnClickListener queryListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ContentResolver cr = getContentResolver();
            Cursor cursor = cr.query(AccountContentProvider.CONTENT_URI, null, null, null, null);
            StringBuffer resultBuffer = new StringBuffer();
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndex("_id"));
                String url = cursor.getString(cursor.getColumnIndex("url"));
                String username = cursor.getString(cursor.getColumnIndex("username"));
                String password = cursor.getString(cursor.getColumnIndex("password"));
                resultBuffer.append(id).append(url).append(username).append(password).append("\r\n");
            }
            cursor.close();
            Toast.makeText(AccountActivity.this, "查询成功", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_account, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemID = item.getItemId();
        switch (itemID) {
            case R.id.action_add:
                Intent intent = new Intent(this, AddAccountActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh_listView();
    }
}