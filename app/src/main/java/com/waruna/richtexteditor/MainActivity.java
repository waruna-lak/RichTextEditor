package com.waruna.richtexteditor;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.waruna.editor.EditorController;
import com.waruna.editor.EditorToolbar;
import com.waruna.editor.OnEditorReadyCallback;
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

        // connect toolbar with editor
        toolbar.setEditor(editor);

        // init editor options
        List<Integer> options = new ArrayList<>();
        options.add(Action.UNDO);
        options.add(Action.REDO);
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
        options.add(Action.FORE_COLOR);
        options.add(Action.BACK_COLOR);
//        options.add(Action.CLEAR); // todo
        toolbar.setActions(options);

        WebView.setWebContentsDebuggingEnabled(true);

        TextView textOutput = findViewById(R.id.tv_output);

        // check editor state and set content as html
        editor.onReady(new OnEditorReadyCallback() {
            @Override
            public void onReady() {
                editor.setHtmlContent("Hello World!", false);
            }
        });

        // get editor content as html
        findViewById(R.id.btn_html).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.getHtml(new EditorController.OnHtmlReturned() {
                    @Override
                    public void process(String html) {
                        textOutput.setText(html);
                    }
                });
            }
        });

        // get editor content as json (Delta Json object)
        findViewById(R.id.btn_content).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.getContent(new EditorController.OnContentsReturned() {
                    @Override
                    public void process(String contents) {
                        textOutput.setText(contents);
                    }
                });
            }
        });

        // get editor content as text
        findViewById(R.id.btn_text).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.getText(new EditorController.OnTextReturned() {
                    @Override
                    public void process(String html) {
                        textOutput.setText(html);
                    }
                });
            }
        });
    }
}