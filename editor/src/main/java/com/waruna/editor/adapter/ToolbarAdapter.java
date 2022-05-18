package com.waruna.editor.adapter;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.waruna.editor.R;
import com.waruna.editor.util.Action;

import java.util.List;

/**
 * Copyright 2022 Waruna
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

public class ToolbarAdapter extends RecyclerView.Adapter<ToolbarAdapter.ViewHolder> {

    private List<ToolbarItem> items;
    private ItemClickListener listener;
    private int selectColor;

    public ToolbarAdapter(List<ToolbarItem> items) {
        this.items = items;
        this.selectColor = Color.CYAN;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_icon, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ToolbarItem item = items.get(position);
        holder.onBind(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    /**
     * Used to set needed options
     *
     * @param items
     */
    public void setItems(List<ToolbarItem> items) {
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    /**
     * Used to set item listener
     *
     * @param listener
     */
    public void setListener(ItemClickListener listener) {
        this.listener = listener;
    }

    /**
     * Used to select item
     *
     * @param type
     */
    public void setActive(int type, boolean active) {
        for (int i = 0; i < items.size(); i++) {
            if (type == Action.NONE) {
                items.get(i).setActive(false);
                notifyItemChanged(i);
            }

            if (items.get(i).getType() == type) {
                items.get(i).setActive(active);
                notifyItemChanged(i);
            }
        }
    }

    /**
     * Set color for apply when click on edit options
     *
     * @param selectColor
     */
    public void setSelectColor(int selectColor) {
        this.selectColor = selectColor;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.iv_item);
        }

        @SuppressLint("ResourceType")
        public void onBind(ToolbarItem item) {
            imageView.setImageResource(item.getDrawable());
            updateColor(item.isActive());

            itemView.setOnClickListener(view -> {
                if (listener != null) {
                    setActive(item);
                    updateColor(item.isActive());
                    listener.onClick(item);
                }
            });
        }

        private void updateColor(boolean active) {
            if (active) {
                imageView.setColorFilter(selectColor, android.graphics.PorterDuff.Mode.SRC_IN);
            } else {
                imageView.clearColorFilter();
            }
        }

        private void setActive(ToolbarItem selectItem) {
            if (!selectItem.isUpdate()) return;
            for (ToolbarItem item : items) {
                if (selectItem.getType() == item.getType()) {
                    item.setActive(!item.isActive());
                } else {
                    item.setActive(false);
                }
            }
        }
    }

    public interface ItemClickListener {
        void onClick(ToolbarItem item);
    }
}
