package com.waruna.editor;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.waruna.editor.adapter.ToolbarAdapter;
import com.waruna.editor.adapter.ToolbarItem;
import com.waruna.editor.util.Action;

import java.util.ArrayList;
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

public class EditorToolbar extends ConstraintLayout implements StyleUpdatedCallback, ToolbarAdapter.ItemClickListener {

    private RecyclerView toolbar;
    private ImageView nav;
    private EditorController editor;
    private ToolbarAdapter adapter;
    private List<Integer> actions;

    public EditorToolbar(@NonNull Context context) {
        super(context);
        init();
    }

    public EditorToolbar(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EditorToolbar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.layout_toolbar, this);
        toolbar = findViewById(R.id.rv_toolbar);
        nav = findViewById(R.id.iv_nav);
        actions = new ArrayList<>();

        adapter = new ToolbarAdapter(new ArrayList<>());
        adapter.setListener(this);

        toolbar.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        toolbar.setAdapter(adapter);

        /*ArrayList<ToolbarItem> options = new ArrayList<>();
        options.add(new ToolbarItem(Action.BOLD, R.drawable.ic_format_bold));
        options.add(new ToolbarItem(Action.ITALIC, R.drawable.ic_format_italic));
        options.add(new ToolbarItem(Action.UNDERLINE, R.drawable.ic_format_underlined));
        options.add(new ToolbarItem(Action.STRIKETHROUGH, R.drawable.ic_strikethrough_s));
        options.add(new ToolbarItem(Action.BLOCK_QUOTE, R.drawable.ic_format_quote));
        options.add(new ToolbarItem(Action.CODE_VIEW, R.drawable.ic_code));
        options.add(new ToolbarItem(Action.ORDERED, R.drawable.ic_format_list_numbered));
        options.add(new ToolbarItem(Action.UNORDERED, R.drawable.ic_format_list_bulleted));
        options.add(new ToolbarItem(Action.SUBSCRIPT, R.drawable.ic_subscript));
        options.add(new ToolbarItem(Action.SUPERSCRIPT, R.drawable.ic_superscript));
        options.add(new ToolbarItem(Action.INDENT, R.drawable.ic_format_indent_increase));
        options.add(new ToolbarItem(Action.OUTDENT, R.drawable.ic_format_indent_decrease));
        options.add(new ToolbarItem(Action.FORE_COLOR, R.drawable.ic_format_color_text));
        options.add(new ToolbarItem(Action.BACK_COLOR, R.drawable.ic_format_color_fill));
        options.add(new ToolbarItem(Action.NONE, R.drawable.ic_format_clear));
        adapter.setItems(options);*/
    }

    /**
     * Can select options for toolbar
     *
     * @param actions
     */
    public void setActions(List<Integer> actions) {
        this.actions = actions;

        List<ToolbarItem> toolbarItems = new ArrayList<>();
        for (int a : actions) {
            toolbarItems.add(getItem(a));
        }
        adapter.setItems(toolbarItems);
    }

    private ToolbarItem getItem(int type) {
        if (Action.BOLD == type) {
            return new ToolbarItem(Action.BOLD, R.drawable.ic_format_bold);
        }
        if (Action.ITALIC == type) {
            return new ToolbarItem(Action.ITALIC, R.drawable.ic_format_italic);
        }
        if (Action.UNDERLINE == type) {
            return new ToolbarItem(Action.UNDERLINE, R.drawable.ic_format_underlined);
        }
        if (Action.STRIKETHROUGH == type) {
            return new ToolbarItem(Action.STRIKETHROUGH, R.drawable.ic_strikethrough_s);
        }
        if (Action.BLOCK_QUOTE == type) {
            return new ToolbarItem(Action.BLOCK_QUOTE, R.drawable.ic_format_quote);
        }
        if (Action.CODE_VIEW == type) {
            return new ToolbarItem(Action.CODE_VIEW, R.drawable.ic_code);
        }
        if (Action.ORDERED == type) {
            return new ToolbarItem(Action.ORDERED, R.drawable.ic_format_list_numbered);
        }
        if (Action.UNORDERED == type) {
            return new ToolbarItem(Action.UNORDERED, R.drawable.ic_format_list_bulleted);
        }
        if (Action.SUBSCRIPT == type) {
            return new ToolbarItem(Action.SUBSCRIPT, R.drawable.ic_subscript);
        }
        if (Action.SUPERSCRIPT == type) {
            return new ToolbarItem(Action.SUPERSCRIPT, R.drawable.ic_superscript);
        }
        if (Action.INDENT == type) {
            return new ToolbarItem(Action.INDENT, R.drawable.ic_format_indent_increase);
        }
        if (Action.OUTDENT == type) {
            return new ToolbarItem(Action.OUTDENT, R.drawable.ic_format_indent_decrease);
        }
        if (Action.FORE_COLOR == type) {
            return new ToolbarItem(Action.FORE_COLOR, R.drawable.ic_format_color_text);
        }
        if (Action.BACK_COLOR == type) {
            return new ToolbarItem(Action.BACK_COLOR, R.drawable.ic_format_color_fill);
        }
        if (Action.CLEAR == type) {
            return new ToolbarItem(Action.CLEAR, R.drawable.ic_format_clear);
        }
        return null;
    }

    /**
     * Enable navigation button
     */
    public void showNavButton() {
        nav.setVisibility(View.VISIBLE);
    }

    /**
     * Hide navigation button
     */
    public void hideNavButton() {
        nav.setVisibility(View.GONE);
    }

    /**
     * Enable editor to bind with toolbar
     *
     * @param editor
     */
    public void setEditor(RichTextEditor editor) {
        this.editor = editor.getController();
        this.editor.setStyleUpdatedCallback(this);
    }

    @Override
    public void onUpdate(int type, Object value) {
        post(() -> {
            boolean isActive;

            if (value instanceof Boolean){
                isActive = (boolean) value;
            } else {
                isActive = false;
            }

            adapter.setActive(type, isActive);
        });
    }

    @Override
    public void onClick(ToolbarItem item) {
        switch (item.getType()) {
            case Action.UNDO:
            case Action.REDO:
            case Action.BOLD:
            case Action.ITALIC:
            case Action.UNDERLINE:
            case Action.STRIKETHROUGH:
            case Action.NORMAL:
            case Action.H1:
            case Action.H2:
            case Action.H3:
            case Action.H4:
            case Action.H5:
            case Action.H6:
            case Action.JUSTIFY_LEFT:
            case Action.JUSTIFY_CENTER:
            case Action.JUSTIFY_RIGHT:
            case Action.JUSTIFY_FULL:
            case Action.ORDERED:
            case Action.UNORDERED:
            case Action.CHECK:
            case Action.INDENT:
            case Action.OUTDENT:
            case Action.LINE:
            case Action.BLOCK_QUOTE:
            case Action.BLOCK_CODE:
            case Action.CODE_VIEW:
            case Action.SUBSCRIPT:
            case Action.CLEAR:
            case Action.SUPERSCRIPT: {
                editor.command(item.getType(), "");
                break;
            }
            case Action.LINK: {
                editor.command(item.getType(), "link");
                break;
            }
            case Action.IMAGE: {
                break;
            }
            case Action.SIZE: {
                editor.command(item.getType(), "size");
                break;
            }
            case Action.FORE_COLOR: {
                editor.command(item.getType(), "f_color");
                break;
            }
            case Action.BACK_COLOR: {
                editor.command(item.getType(), "b_color");
                break;
            }
            default: {
            }
        }
    }
}