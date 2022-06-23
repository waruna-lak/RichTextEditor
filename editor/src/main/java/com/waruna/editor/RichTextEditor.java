package com.waruna.editor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.waruna.editor.util.ColorHelper;

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
    private OnEditorReadyCallback readyCallback;

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
        controller.setPlaceholder("Type something here...");
        controller.setWebView(this);

        setVerticalScrollBarEnabled(false);
        setHorizontalScrollBarEnabled(false);
        getSettings().setJavaScriptEnabled(true);
        getSettings().setDomStorageEnabled(true);
        setWebChromeClient(new WebChromeClient());
        setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (readyCallback != null) readyCallback.onReady();
                handleNightMode();
                setTextColor();
                setPlaceholderTextColor();
            }
        });
        addJavascriptInterface(controller, "RichTextEditor");
        loadUrl(SETUP_HTML);

        setBackgroundColor(Color.TRANSPARENT);

    }

    /**
     * set OnEditorReadyCallback for get know about ideal time for use editor
     *
     * @param callback OnEditorReadyCallback
     */
    public void onReady(OnEditorReadyCallback callback) {
        readyCallback = callback;
    }

    /**
     * return controller of rich text editor
     *
     * @return EditorController
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

    /**
     * allows to listen content as a json (delta)
     *
     * @param callback OnContentsReturned
     */
    public void listenContentChange(EditorController.OnContentsReturned callback) {
        controller.listenContentChange(callback);
    }

    /**
     * allows to listen content as a text
     *
     * @param callback OnTextReturned
     */
    public void listenTextChange(EditorController.OnTextReturned callback) {
        controller.listenTextChange(callback);
    }

    /**
     * allows to listen content as a html
     *
     * @param callback OnHtmlReturned
     */
    public void listenHtmlChange(EditorController.OnHtmlReturned callback) {
        controller.listenHtmlChange(callback);
    }

    /**
     * allows to enable editor
     */
    public void enable() {
        controller.enable();
    }

    /**
     * allows to disable editor
     */
    public void disable() {
        controller.disable();
    }

    private void handleNightMode() {
        int mode = getContext().getResources().getConfiguration().uiMode &
                Configuration.UI_MODE_NIGHT_MASK;
        if (mode == Configuration.UI_MODE_NIGHT_YES) {
            controller.setDarkMode(true);
        }
    }

    private void setTextColor() {
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = getContext().getTheme();
        theme.resolveAttribute(android.R.attr.textColorPrimary, typedValue, true);
        TypedArray arr =
                getContext().obtainStyledAttributes(typedValue.data, new int[]{
                        android.R.attr.textColorPrimary});
        int textColor = arr.getColor(0, -1);
        arr.recycle();
        controller.setTextColor(ColorHelper.toRGBAString(textColor));
    }

    private void setPlaceholderTextColor() {
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = getContext().getTheme();
        theme.resolveAttribute(android.R.attr.textColorHint, typedValue, true);
        TypedArray arr =
                getContext().obtainStyledAttributes(typedValue.data, new int[]{
                        android.R.attr.textColorHint});
        int textColor = arr.getColor(0, -1);
        arr.recycle();
        controller.setPlaceholderColor(ColorHelper.toRGBAString(textColor));
    }
}
