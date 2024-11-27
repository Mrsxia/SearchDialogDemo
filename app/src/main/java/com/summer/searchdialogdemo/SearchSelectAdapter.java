package com.summer.searchdialogdemo;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by summer on 2024/10/18
 */

public class SearchSelectAdapter extends RecyclerView.Adapter<SearchSelectAdapter.ViewHolder> {


    private List<ItemModel> itemList;
    private List<Integer> selectedItemPositions;

    //声明接口
    private OnItemClickListener onItemClickListener;


    public SearchSelectAdapter(List<ItemModel> itemList) {
        this.itemList = itemList;
        selectedItemPositions = new ArrayList<>();
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }


    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 创建ViewHolder
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_cell_select_single, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        // 绑定数据到ViewHolder
        ItemModel item = itemList.get(position);
        holder.textView.setText(item.getItemName());

        //给条目布局设置点击事件
        holder.itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedItemPositions.contains(position)) {
                    selectedItemPositions.remove(Integer.valueOf(position));
                    holder.textView.setTextColor(Color.BLACK);
                    holder.itemView.setBackgroundResource(R.drawable.item_grey_layout_bg);
                    item.setSelected(false);
                } else {
                    selectedItemPositions.add(position);
                    holder.textView.setTextColor(Color.WHITE);
                    holder.itemView.setBackgroundResource(R.drawable.item_blue_layout_bg);
                    item.setSelected(true);
                }

                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(position);
                }
            }


        });

        if (selectedItemPositions.contains(position)) {
            holder.textView.setTextColor(Color.WHITE);
            holder.itemView.setBackgroundResource(R.drawable.item_blue_layout_bg);

        } else {
            holder.textView.setTextColor(Color.BLACK);
            holder.itemView.setBackgroundResource(R.drawable.item_grey_layout_bg);
        }

    }

    /**
     * 接口回调
     */
    public interface OnItemClickListener {
        void onItemClick(int position);
    }


    public void selectAll() {
        selectedItemPositions.clear();
        for (int i = 0; i < itemList.size(); i++) {
            selectedItemPositions.add(i);
        }
        notifyDataSetChanged();
    }

    public void clearSelection() {
        selectedItemPositions.clear();
        notifyDataSetChanged();
    }

    public List<Integer> getSelectedItemPositions() {
        return selectedItemPositions;
    }

    public List<String> getSelectedItems() {
        List<String> selectedItems = new ArrayList<>();
        for (int position : selectedItemPositions) {
            selectedItems.add(itemList.get(position).getItemName());
        }
        return selectedItems;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;
        private final LinearLayout itemLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tv_select_info);
            itemLayout = itemView.findViewById(R.id.item_layout);
        }
    }
}
