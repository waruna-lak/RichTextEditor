package com.waruna.editor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.util.AttributeSet;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

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

public class RichTextEditor extends WebView {

    private static final String SETUP_HTML = "file:///android_asset/editor.html";

    private EditorController controller;

    public RichTextEditor(@NonNull Context context) {
        super(context);
        init();
    }

    public RichTextEditor(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RichTextEditor(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public RichTextEditor(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    @SuppressLint({"SetJavaScriptEnabled", "JavascriptInterface"})
    private void init() {

        controller = new EditorController();
        controller.setPlaceholder("Note");
        controller.setWebView(this);

        setVerticalScrollBarEnabled(false);
        setHorizontalScrollBarEnabled(false);
        getSettings().setJavaScriptEnabled(true);
        getSettings().setDomStorageEnabled(true);
        setWebChromeClient(new WebChromeClient());
        setWebViewClient(new WebViewClient());
        addJavascriptInterface(controller, "RichTextEditor");
        loadUrl(SETUP_HTML);

        setBackgroundColor(Color.TRANSPARENT);
    }

    /**
     * return controller of rich text editor
     *
     * @return
     */
    public EditorController getController() {
        return controller;
    }

    /**
     * allows to get content as a json (delta)
     *
     * @param callback OnContentsReturned
     */
    public void getContent(EditorController.OnContentsReturned callback) {
        controller.getContents(callback);
    }

    /**
     * allows to get content as a text
     *
     * @param callback OnTextReturned
     */
    public void getText(EditorController.OnTextReturned callback) {
        controller.getText(callback);
    }

    /**
     * allows to get content as a html
     *
     * @param callback OnHtmlReturned
     */
    public void getHtml(EditorController.OnHtmlReturned callback) {
        controller.getHtmlContent(callback);
    }

    /**
     * method for set json content to editor
     *
     * @param data json string
     */
    public void setContent(String data) {
        controller.setContents(data);
    }

    /**
     * method for set html content to editor
     *
     * @param html                  html string
     * @param replaceCurrentContent set to true replace the whole content, false to concatenate
     */
    public void setHtmlContent(String html, boolean replaceCurrentContent) {
        controller.setHtmlContent(html, replaceCurrentContent);
    }

    private void handleNightMode() {
        int mode = getContext().getResources().getConfiguration().uiMode &
                Configuration.UI_MODE_NIGHT_MASK;
        if (mode == Configuration.UI_MODE_NIGHT_YES) {
            controller.setDarkMode(true);
        }
    }
}
