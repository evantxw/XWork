package com.evan.scanner;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * 扫描结果
 */
public class ResultActivity extends Activity {

    private TextView mResultText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        String result = getIntent().getStringExtra("result");
        mResultText = (TextView) findViewById(R.id.result_text);
        mResultText.setText(result);
    }
}
