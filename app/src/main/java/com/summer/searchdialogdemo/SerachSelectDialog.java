package com.summer.searchdialogdemo;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by summer on 2024/10/18
 */

public class SerachSelectDialog extends Dialog {

    private static SearchSelectAdapter sa;
    private static String result;

    private static List<String> resultList = new ArrayList<>();
    private static List<String> selectedItems;

    private static int searchPosition;


    public SerachSelectDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    /**
     * 设置 Dialog的大小
     *
     * @param x 宽比例
     * @param y 高比例
     */
    public void setDialogWindowAttr(double x, double y, Activity activity) {
        if (x < 0 || x > 1 || y < 0 || y > 1) {
            return;
        }
        Window window = this.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        WindowManager manager = activity.getWindowManager();
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        int width = outMetrics.widthPixels;
        int height = outMetrics.heightPixels;
        lp.gravity = Gravity.BOTTOM;
        lp.width = (int) (width * x);
        lp.height = (int) (height * y);
        this.getWindow().setAttributes(lp);
    }


    public static class Builder {
        private String title;
        private View contentView;
        private String positiveButtonText;
        private String negativeButtonText;

        private List<ItemModel> listData;

        private View.OnClickListener positiveButtonClickListener;
        private View.OnClickListener negativeButtonClickListener;
        private View.OnClickListener singleButtonClickListener;

        private View layout;
        private Context context;
        private SerachSelectDialog dialog;
        private OnSelectedListiner selectedListiner;

        SearchView searchView;
        LinearLayout closeBtn;
        LinearLayout okBtn;
        TextView titleView;
        private boolean state = false;
        private RecyclerView itemLv;
        private final TextView qxTv;


        public Builder(Context context) {
            //这里传入自定义的style，直接影响此Dialog的显示效果。style具体实现见style.xml
            this.context = context;
            dialog = new SerachSelectDialog(context, R.style.selectDialog);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            layout = inflater.inflate(R.layout.dialog_select_search, null);
            qxTv = layout.findViewById(R.id.qx_tv);
            itemLv = layout.findViewById(R.id.item_lv);
            searchView = layout.findViewById(R.id.searchView);
            closeBtn = layout.findViewById(R.id.diss_layout);
            okBtn = layout.findViewById(R.id.ok_layout);
            titleView = layout.findViewById(R.id.title_tv);
            dialog.addContentView(layout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setContentView(View v) {
            this.contentView = v;
            return this;
        }

        public void setListData(List<ItemModel> listData) {
            this.listData = listData;
        }

        public Builder setPositiveButton(String positiveButtonText, View.OnClickListener listener) {
            this.positiveButtonText = positiveButtonText;
            this.positiveButtonClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(String negativeButtonText, View.OnClickListener listener) {
            this.negativeButtonText = negativeButtonText;
            this.negativeButtonClickListener = listener;
            return this;
        }

        /**
         * 单按钮对话框和双按钮对话框的公共部分在这里设置
         */
        private SerachSelectDialog create() {

            GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 3);
            sa = new SearchSelectAdapter(listData);
            itemLv.setLayoutManager(gridLayoutManager);
            itemLv.setAdapter(sa);


            //搜索事件
            searchView.setSearchViewListener(new SearchView.onSearchViewListener() {

                @Override
                public boolean onQueryTextChange(String text) {
                    updateLayout(searchItem(text));
                    return false;
                }
            });
            //全选
            qxTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (sa.getSelectedItemPositions().size() == sa.getItemCount()) {
                        sa.clearSelection();
                    } else {
                        sa.selectAll();
                        resultList = sa.getSelectedItems();
                    }
                }
            });
            //取消按钮
            closeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    resultList.clear();
                }
            });
            //确认按钮
            okBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String json = new Gson().toJson(resultList);
                    selectedListiner.onSelected(json);
                    dialog.dismiss();
                    resultList.clear();
                }
            });

            dialog.setOnDismissListener(new OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {

                }
            });
            //item点击事件
            sa.setOnItemClickListener(new SearchSelectAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    boolean selected = listData.get(position).isSelected();
                    result = listData.get(position).getItemName();
                    if (selected == true) {
                        resultList.add(result);
                    } else {
                        resultList.remove(result);
                    }

                    Log.i("U--", resultList.toString() + selected + "");
                }
            });

            dialog.setContentView(layout);
            //用户可以点击手机Back键取消对话框显示
            dialog.setCancelable(true);
            //用户不能通过点击对话框之外的地方取消对话框显示
            dialog.setCanceledOnTouchOutside(false);
            return dialog;

        }

        //在数据源中查找匹配的数据
        public List<ItemModel> searchItem(String name) {
            ArrayList<ItemModel> mSearchList = new ArrayList<ItemModel>();
            for (int i = 0; i < listData.size(); i++) {
                int index = listData.get(i).getItemName().indexOf(name);
                // 存在匹配的数据
                if (index != -1) {
                    mSearchList.add(listData.get(i));
                    Log.i("U--", i + "搜索位置");
                    searchPosition = i;
                }
            }
            return mSearchList;
        }

        //提供匹配后的的数据进行数据回调
        public void updateLayout(List<ItemModel> newList) {

            final SearchSelectAdapter sa = new SearchSelectAdapter(newList);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 3);
            itemLv.setLayoutManager(gridLayoutManager);
            itemLv.setAdapter(sa);


            //item点击事件
            sa.setOnItemClickListener(new SearchSelectAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    result = newList.get(position).getItemName();
                    boolean selected = listData.get(searchPosition).isSelected();

                    if (selected == true) {
                        resultList.add(result);
                    } else {
                        resultList.remove(result);
                    }

                    Log.i("U--", resultList.toString() + selected + "");
                }
            });
        }

        //自定义接口进行数据点击回传
        public static abstract class OnSelectedListiner {

            public abstract void onSelected(String String);
        }

        public void setSelectedListiner(SerachSelectDialog.Builder.OnSelectedListiner selectedListiner) {
            this.selectedListiner = selectedListiner;
        }

        //弹框展示
        public SerachSelectDialog show() {
            create();
            dialog.show();
            return dialog;
        }

    }
}
