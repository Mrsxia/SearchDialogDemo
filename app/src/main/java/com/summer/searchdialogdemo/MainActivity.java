package com.summer.searchdialogdemo;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by summer on 2024/10/18
 */

public class MainActivity extends AppCompatActivity {

    private TextView okTv;
    private List<ItemModel> itemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        okTv = findViewById(R.id.ok_tv);

        // 创建数据列表
        itemList = new ArrayList<>();
        itemList.add(new ItemModel("医生", false));
        itemList.add(new ItemModel("警察", false));
        itemList.add(new ItemModel("护士", false));
        itemList.add(new ItemModel("农民", false));
        itemList.add(new ItemModel("工人", false));
        itemList.add(new ItemModel("司机", false));

        okTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSearchSelectDialog();
            }
        });
    }

    private void openSearchSelectDialog() {
        SerachSelectDialog.Builder alert = new SerachSelectDialog.Builder(this);
        alert.setListData(itemList);
        alert.setTitle("岗位选择");
        alert.setSelectedListiner(new SerachSelectDialog.Builder.OnSelectedListiner() {
            @Override
            public void onSelected(String info) {
                okTv.setText(info);
            }
        });
        SerachSelectDialog mDialog = alert.show();
        //设置Dialog 尺寸
        mDialog.setDialogWindowAttr(0.9, 0.9, this);
    }
}