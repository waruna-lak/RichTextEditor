package com.waruna.editor.adapter;

import androidx.annotation.IdRes;

public class ToolbarItem {
    private int type;
    @IdRes
    private int drawable;
    private boolean active;
    private boolean update;


    public ToolbarItem(int type, int drawable) {
        this.type = type;
        this.drawable = drawable;
        this.active = false;
        this.update = true;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getDrawable() {
        return drawable;
    }

    public void setDrawable(int drawable) {
        this.drawable = drawable;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isUpdate() {
        return update;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }
}
