package com.summer.searchdialogdemo;

/**
 * Created by summer on 2024/10/18
 */

public class ItemModel {

    private String itemName;
    private boolean isSelected;

    public ItemModel(String itemName, boolean isSelected) {
        this.itemName = itemName;
        this.isSelected = isSelected;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
