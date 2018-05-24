package com.guojian.weekcook.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.guojian.weekcook.R;
import com.guojian.weekcook.statusbar.StatusBarCompat;

/**
 * @author jkloshhm 2017-01-23 搜索页
 */
public class SearchActivity extends Activity {
    private EditText editText;
    private String hotSreachName[] = {"土豆", "红烧肉", "韭菜", "鱼", "汤", "排骨", "早餐", "批萨"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        StatusBarCompat.setStatusBarColor(this, ResourcesCompat.getColor(getResources(),R.color.white,null), true);
        LinearLayout mBack = (LinearLayout) findViewById(R.id.ll_search_back);
        if (mBack != null){
            mBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
        editText = (EditText) findViewById(R.id.edit_query_view_main);
        LinearLayout mSearch = (LinearLayout) findViewById(R.id.ll_search_activity);
        mSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchCookName(editText.getText().toString());
            }
        });

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH
                        || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    searchCookName(editText.getText().toString());
                }
                return false;
            }
        });


        GridView hotSearchGridView = (GridView) findViewById(R.id.gv_hot_search);
        ArrayAdapter<String> arrayAdapter =
                new ArrayAdapter<String>(this, R.layout.hot_search_gv_adapter_item, hotSreachName);
        hotSearchGridView.setAdapter(arrayAdapter);
        hotSearchGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = hotSreachName[position];
                searchCookName(name);
            }
        });

        ListView historySearchListView = (ListView) findViewById(R.id.lv_history_search);

    }

    private void searchCookName(String name) {
        //do something;
        if (TextUtils.isEmpty(name.trim())) {
            Toast.makeText(SearchActivity.this, "请输入正确的菜名", Toast.LENGTH_SHORT).show();
        } else {
            //setEditTextInhibitInputSpeChat(mSearchName);
            Intent mIntent = new Intent(SearchActivity.this, CookListActivity.class);
            mIntent.putExtra("CookType", "getDataBySearchName");
            mIntent.putExtra("name", name.replace(" ", ""));
            startActivity(mIntent);
        }
    }
}
