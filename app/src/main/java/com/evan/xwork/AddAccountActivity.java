package com.evan.xwork;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by evan on 2015/7/11.
 */
public class AddAccountActivity extends Activity {

    EditText url_edit;
    EditText username_edit;
    EditText password_edit;

    Button save_btn, delete_btn;

    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_add);

        url_edit = (EditText)findViewById(R.id.edit_url);
        username_edit = (EditText)findViewById(R.id.edit_username);
        password_edit = (EditText)findViewById(R.id.edit_password);

        String[] account = getIntent().getStringArrayExtra("account");
        if(account != null){
            id = account[0];
            url_edit.setText(account[1]);
            username_edit.setText(account[2]);
            password_edit.setText(account[3]);
        }

        save_btn = (Button) findViewById(R.id.save);
        delete_btn = (Button) findViewById(R.id.delete);
        if(TextUtils.isEmpty(id)){
            delete_btn.setVisibility(View.GONE);
        }

        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(url_edit.length() == 0 || username_edit.length() == 0|| password_edit.length() == 0){
                    Toast.makeText(AddAccountActivity.this, "数据输入不全", Toast.LENGTH_SHORT).show();
                    return;
                }
                String url = url_edit.getText().toString();
                String username = username_edit.getText().toString();
                String password = password_edit.getText().toString();

                ContentValues values = new ContentValues();
                values.put("url", url);
                values.put("username", username);
                values.put("password", password);

                ContentResolver cr = getContentResolver();

                if(!TextUtils.isEmpty(id)){
                    cr.update(AccountContentProvider.CONTENT_URI, values, "_id=?",new String[]{id});
                    Toast.makeText(AddAccountActivity.this, "更新成功", Toast.LENGTH_SHORT).show();
                }else {
                    cr.insert(AccountContentProvider.CONTENT_URI, values);
                    Toast.makeText(AddAccountActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
                }
                finish();
            }
        });

        delete_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                ContentResolver cr = getContentResolver();
                cr.delete(AccountContentProvider.CONTENT_URI, "_id=?",new String[]{id});
                Toast.makeText(AddAccountActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
