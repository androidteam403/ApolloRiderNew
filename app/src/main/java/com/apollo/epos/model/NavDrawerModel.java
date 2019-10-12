package com.apollo.epos.model;

public class NavDrawerModel {
    public String name;
    public boolean isHeader;
    public boolean isSelected;

    public NavDrawerModel(String name, boolean isHeader, boolean isSelected) {
        this.name = name;
        this.isHeader = isHeader;
        this.isSelected = isSelected;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isHeader() {
        return isHeader;
    }

    public void setHeader(boolean header) {
        isHeader = header;
    }
}
