package com.waruna.richtexteditor;

import android.os.Bundle;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

import com.waruna.editor.EditorToolbar;
import com.waruna.editor.RichTextEditor;
import com.waruna.editor.util.Action;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RichTextEditor editor;
    private EditorToolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editor = findViewById(R.id.rte_editor);
        toolbar = findViewById(R.id.rte_toolbar);

        toolbar.setEditor(editor);

        List<Integer> options = new ArrayList<>();
        options.add(Action.BOLD);
        options.add(Action.ITALIC);
        options.add(Action.UNDERLINE);
        options.add(Action.STRIKETHROUGH);
        options.add(Action.BLOCK_QUOTE);
        options.add(Action.CODE_VIEW);
        options.add(Action.ORDERED);
        options.add(Action.UNORDERED);
        options.add(Action.SUBSCRIPT);
        options.add(Action.SUPERSCRIPT);
        options.add(Action.INDENT);
        options.add(Action.OUTDENT);
//        options.add(Action.FORE_COLOR); // todo
//        options.add(Action.BACK_COLOR); // todo
//        options.add(Action.CLEAR); // todo
        toolbar.setActions(options);

        WebView.setWebContentsDebuggingEnabled(true);
    }
}