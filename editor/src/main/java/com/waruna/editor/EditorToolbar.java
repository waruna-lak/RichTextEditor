package com.waruna.editor;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.waruna.editor.adapter.ToolbarAdapter;
import com.waruna.editor.adapter.ToolbarItem;
import com.waruna.editor.color_picker.ColorPickerDialog;
import com.waruna.editor.color_picker.ColorPickerDialogListener;
import com.waruna.editor.util.Action;
import com.waruna.editor.util.ColorHelper;

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

public class EditorToolbar extends ConstraintLayout implements StyleUpdatedCallback, ToolbarAdapter.ItemClickListener, View.OnClickListener {

    private RecyclerView toolbar;
    private ImageView nav;
    private ConstraintLayout root;
    private EditorController editor;
    private ToolbarAdapter adapter;
    private int direction = 1;

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

        // init views
        toolbar = findViewById(R.id.rv_toolbar);
        nav = findViewById(R.id.iv_nav);
        root = findViewById(R.id.root);

        // int adapter
        adapter = new ToolbarAdapter(new ArrayList<>());
        adapter.setListener(this);

        toolbar.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        toolbar.setAdapter(adapter);

        // set colors
        root.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.toolbar_background));
        adapter.setSelectColor(ContextCompat.getColor(getContext(), R.color.toolbar_select));

        // init listeners
        nav.setOnClickListener(this);

        toolbar.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                boolean left = toolbar.canScrollHorizontally(-1);
                boolean right = toolbar.canScrollHorizontally(1);

                if (right && !left) {
                    nav.setImageResource(R.drawable.ic_chevron_right);
                    direction = 1;
                }
                if (left && !right) {
                    nav.setImageResource(R.drawable.ic_chevron_left);
                    direction = -1;
                }
            }
        });

        // hide nav
        handleNavButton();

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

    private void handleNavButton() {
        toolbar.post(new Runnable() {
            @Override
            public void run() {
                boolean right = toolbar.canScrollHorizontally(1);
                if (!right) {
                    hideNavButton();
                } else {
                    showNavButton();
                }
            }
        });
    }

    /**
     * Can select options for toolbar
     *
     * @param actions
     */
    public void setActions(List<Integer> actions) {
        List<ToolbarItem> toolbarItems = new ArrayList<>();
        for (int a : actions) {
            toolbarItems.add(getItem(a));
        }
        adapter.setItems(toolbarItems);
        handleNavButton();
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
            ToolbarItem item = new ToolbarItem(Action.INDENT, R.drawable.ic_format_indent_increase);
            item.setUpdate(false);
            return item;
        }
        if (Action.OUTDENT == type) {
            ToolbarItem item = new ToolbarItem(Action.OUTDENT, R.drawable.ic_format_indent_decrease);
            item.setUpdate(false);
            return item;
        }
        if (Action.FORE_COLOR == type) {
            ToolbarItem item = new ToolbarItem(Action.FORE_COLOR, R.drawable.ic_format_color_text);
            item.setUpdate(false);
            return item;
        }
        if (Action.BACK_COLOR == type) {
            ToolbarItem item = new ToolbarItem(Action.BACK_COLOR, R.drawable.ic_format_color_fill);
            item.setUpdate(false);
            return item;
        }
        if (Action.CLEAR == type) {
            return new ToolbarItem(Action.CLEAR, R.drawable.ic_format_clear);
        }
        if (Action.UNDO == type) {
            ToolbarItem item = new ToolbarItem(Action.UNDO, R.drawable.ic_undo);
            item.setUpdate(false);
            return item;
        }
        if (Action.REDO == type) {
            ToolbarItem item = new ToolbarItem(Action.REDO, R.drawable.ic_redo);
            item.setUpdate(false);
            return item;
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

    private void showColorPicker(ValueCallback<String> callback) {
        ColorPickerDialog dialog = ColorPickerDialog.newBuilder()
                .setDialogTitle(R.string.cpv_default_title)
                .setDialogType(ColorPickerDialog.TYPE_PRESETS)
                .setShowAlphaSlider(true)
                .setShowColorShades(true)
                .create();
        dialog.setColorPickerDialogListener(new ColorPickerDialogListener() {
            @Override
            public void onColorSelected(int dialogId, int color) {
                callback.onReceiveValue(ColorHelper.toRGBAString(color));
            }

            @Override
            public void onDialogDismissed(int dialogId) {

            }
        });

        AppCompatActivity activity = (AppCompatActivity) getContext();
        activity.getSupportFragmentManager()
                .beginTransaction()
                .add(dialog, "color_picker")
                .commit();
    }

    @Override
    public void onUpdate(int type, Object value) {
        post(() -> {
            boolean isActive;

            if (value instanceof Boolean) {
                isActive = (boolean) value;
            } else {
                isActive = false;
            }

            adapter.setActive(type, isActive);
        });
    }

    @Override
    public void onClick(ToolbarItem item) {
        // update list selection
        if (item.getType() == Action.ORDERED || item.getType() == Action.UNORDERED) {
            adapter.notifyDataSetChanged();
        }

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
                editor.blur();
                showColorPicker(new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String value) {
                        editor.command(item.getType(), value);
                    }
                });
                break;
            }
            case Action.BACK_COLOR: {
                editor.blur();
                showColorPicker(new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String color) {
                        editor.command(item.getType(), color);
                    }
                });
                break;
            }
            default: {
            }
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.iv_nav) {
            // nav
            boolean left = toolbar.canScrollHorizontally(-1);
            boolean right = toolbar.canScrollHorizontally(1);

            if (direction == 1 && right) {
                int position = adapter.getItemCount() - 1;
                toolbar.scrollToPosition(position);
                nav.setImageResource(R.drawable.ic_chevron_left);
                direction = -1;
            }

            if (direction == -1 && left) {
                toolbar.scrollToPosition(0);
                nav.setImageResource(R.drawable.ic_chevron_right);
                direction = 1;
            }
        }
    }
}
