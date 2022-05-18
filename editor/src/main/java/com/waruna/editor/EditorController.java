package com.waruna.editor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebView;

import com.google.gson.Gson;
import com.waruna.editor.util.Action;
import com.waruna.editor.util.QuillFormat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

public class EditorController {

    private final Gson gson;
    private WebView webView;
    private String placeholder;
    private QuillFormat currentFormat;

    private final ArrayList<Integer> fontBlockGroup;
    private final HashMap<Integer, String> textAlignGroup;
    private final HashMap<Integer, String> listStyleGroup;

    private StyleUpdatedCallback styleUpdatedCallback;

    public EditorController() {
        gson = new Gson();

        currentFormat = new QuillFormat();
        fontBlockGroup = new ArrayList<>();
        textAlignGroup = new HashMap<>();
        listStyleGroup = new HashMap<>();

        fontBlockGroup.add(Action.NORMAL);
        fontBlockGroup.add(Action.H1);
        fontBlockGroup.add(Action.H2);
        fontBlockGroup.add(Action.H3);
        fontBlockGroup.add(Action.H4);
        fontBlockGroup.add(Action.H5);
        fontBlockGroup.add(Action.H6);

        textAlignGroup.put(Action.JUSTIFY_LEFT, "");
        textAlignGroup.put(Action.JUSTIFY_CENTER, "center");
        textAlignGroup.put(Action.JUSTIFY_RIGHT, "right");
        textAlignGroup.put(Action.JUSTIFY_FULL, "justify");

        listStyleGroup.put(Action.ORDERED, "ordered");
        listStyleGroup.put(Action.UNORDERED, "bullet");
    }

    /**
     * Allow to set webview with editor
     *
     * @param webView
     */
    void setWebView(WebView webView) {
        this.webView = webView;
    }

    /**
     * Allow set listener for get style update
     *
     * @param styleUpdatedCallback
     */
    void setStyleUpdatedCallback(StyleUpdatedCallback styleUpdatedCallback) {
        this.styleUpdatedCallback = styleUpdatedCallback;
    }

    /**
     * Allow user to set placeholder
     *
     * @param placeholder
     */
    void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }

    @SuppressLint("JavascriptInterface")
    @JavascriptInterface
    public String getPlaceholder() {
        return placeholder;
    }

    /**
     * Method to allow the lower webview layer to be able to set the current style data
     *
     * @param currentStyle String
     */
    @SuppressLint("JavascriptInterface")
    @JavascriptInterface
    public void updateCurrentStyle(String currentStyle) {
        try {
            Log.d("FontStyle", currentStyle);
            updateStyle(gson.fromJson(currentStyle, QuillFormat.class));
        } catch (Exception e) {
        } // ignored
    }

    /**
     * Private function to update the current style of the editor
     *
     * @param quillFormat QuillFormat
     */
    private void updateStyle(QuillFormat quillFormat) {
        // Log.d("FontStyle", gson.toJson(fontStyle))
        notifyFontStyleChange(Action.NONE, false);

        notifyFontStyleChange(Action.BOLD, quillFormat.getBold());

        notifyFontStyleChange(Action.ITALIC, quillFormat.getItalic());

        notifyFontStyleChange(Action.UNDERLINE, quillFormat.getUnderline());

        notifyFontStyleChange(Action.STRIKETHROUGH, quillFormat.getStrike());

        notifyFontStyleChange(Action.CODE_VIEW, quillFormat.getCode());

        int size = fontBlockGroup.size();
        for (int i = 0; i < size; i++) {
            notifyFontStyleChange(fontBlockGroup.get(i), (quillFormat.getHeader() == i));
        }

        notifyFontStyleChange(Action.SUBSCRIPT, (quillFormat.getScript().equals("sub")));
        notifyFontStyleChange(Action.SUPERSCRIPT, (quillFormat.getScript().equals("super")));

        quillFormat.setAlign(quillFormat.getAlign() == null ? "" : quillFormat.getAlign());

        for (Map.Entry<Integer, String> entry : textAlignGroup.entrySet()) {
            notifyFontStyleChange(entry.getKey(), (quillFormat.getAlign().equals(entry.getValue())));
        }

        for (Map.Entry<Integer, String> entry : listStyleGroup.entrySet()) {
            boolean result = (quillFormat.getList() != null && quillFormat.getList().equals(entry.getValue()));
            notifyFontStyleChange(entry.getKey(), result);
        }

        notifyFontStyleChange(Action.SIZE, quillFormat.getSize());

        notifyFontStyleChange(Action.LINK, !quillFormat.getLink().equals(""));

        currentFormat = quillFormat;
    }

    /**
     * Private method to notify when the style has been changed
     *
     * @param type  Int of the action button
     * @param value Any data for the action
     */
    private void notifyFontStyleChange(int type, Object value) {
        if (styleUpdatedCallback != null)
            styleUpdatedCallback.onUpdate(type, value);
    }

    // general commands

    /**
     * Method to change mode
     *
     * @param enable
     */
    public void setDarkMode(Boolean enable) {
        load("javascript:darkMode(" + enable + ")", null);
    }

    /**
     * Method to undo the last change
     */
    private void undo() {
        load("javascript:undo()", null);
    }

    /**
     * Method to redo the last undo
     */
    private void redo() {
        load("javascript:redo()", null);
    }

    /**
     * Method to make the cursor to focus into view
     */
    void focus(Boolean showKeyboard) {
        if (showKeyboard == null) showKeyboard = false;
        webView.requestFocus();
        load("javascript:focus()", null);
        if (showKeyboard) {
            InputMethodManager im = (InputMethodManager) webView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            im.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
        }
    }

    /**
     * Method to disable the editor
     */
    void disable() {
        load("javascript:disable()", null);
    }

    /**
     * Method to enable back the editor
     */
    void enable() {
        load("javascript:enable()", null);
    }

    // font

    /**
     * Method to bold to current text or the next at the cursor
     */
    private void bold() {
        load("javascript:bold()", null);
    }

    /**
     * Method to italic to current text or the next at the cursor
     */
    private void italic() {
        load("javascript:italic()", null);
    }

    /**
     * Method to underline to current text or the next at the cursor
     */
    private void underline() {
        load("javascript:underline()", null);
    }

    /**
     * Method to strike to current text or the next at the cursor
     */
    private void strikethrough() {
        load("javascript:strikethrough()", null);
    }

    /**
     * Method to apply sub/superscript to current text or the next at the cursor
     *
     * @param style String available styles ['sub', 'super', '']
     */
    private void script(String style) {
        load("javascript:script('" + style + "')", null);
    }

    /**
     * Method to change the background color of current text selection or the next at the cursor
     *
     * @param color String
     */
    private void backColor(String color) {
        load("javascript:background('" + color + "')", null);
    }

    /**
     * Method to change the foreground color of current text selection or the next at the cursor
     *
     * @param color String
     */
    private void foreColor(String color) {
        load("javascript:color('" + color + "')", null);
    }

    /**
     * Method to change font size to current text or the next at the cursor
     *
     * @param size String Available font sizes ['small', 'large', 'huge', '']
     */
    private void fontSize(String size) {
        load("javascript:fontSize(" + size + ")", null);
    }

    // paragraph

    /**
     * Method to align the current paragraph
     *
     * @param style String Available styles ['center', 'right', 'justify', '']
     */
    private void align(String style) {
        load("javascript:align(" + style + ")", null);
    }

    /**
     * Method to insert an ordered list
     */
    private void insertOrderedList() {
        load("javascript:insertOrderedList()", null);
    }

    /**
     * Method to insert an un-ordered list
     */
    private void insertUnorderedList() {
        load("javascript:insertUnorderedList()", null);
    }

    /**
     * Method to insert a check list
     */
    private void insertCheckList() {
        load("javascript:insertCheckList()", null);
    }

    /**
     * Method to indent the current paragraph
     */
    private void indent() {
        load("javascript:indent()", null);
    }

    /**
     * Method to outdent the current paragraph
     */
    private void outdent() {
        load("javascript:outdent()", null);
    }

    /**
     * Method to format the current paragraph into a blockquote
     */
    private void formatBlockquote() {
        load("javascript:formatBlock('blockquote')", null);
    }

    /**
     * Method to format the current paragraph into a code block
     */
    private void formatBlockCode() {
        load("javascript:formatBlock('pre')", null);
    }

    /**
     * Method to get the current selection and do (any) actions on the result
     *
     * @param callback ValueCallback<String> action to do
     */
    void getSelection(ValueCallback<String> callback) {
        load("javascript:getSelection()", callback);
    }

    /**
     * Method to get the current style at the cursor and do (any) actions on it
     *
     * @param callback ValueCallback<String> action to do
     */
    private void getStyle(ValueCallback<String> callback) {
        load("javascript:getStyle()", callback);
    }

    /**
     * Private method to get the HTML content and do (any) actions on the result
     * This method is to be utilized by the public ones
     *
     * @param callBack ValueCallback<String> action to do
     */
    // Correcting javascript method name from `getHtmlContent()' to 'getHtml()'
    // private void getHtmlContent(callBack: ValueCallback<String>) {;} load("javascript:getHtmlContent()", callBack)
    private void getHtmlContent(ValueCallback<String> callBack) {
        load("javascript:getHtml()", html -> {
            String escapedData = html
                    // There a bug? that the returned result has the unicode for < instead of the  char
                    // and has double \\. So we are escaping them here
                    .replace("\\u003C", "<")
                    .replace("\\\"", "\"");
            callBack.onReceiveValue(escapedData.substring(1, escapedData.length() - 1));
        });
    }

    /**
     * Method to allow setting the HTML content to the editor
     *
     * @param htmlContent           String
     * @param replaceCurrentContent Boolean set to true replace the whole content, false to concatenate
     */
    void setHtmlContent(String htmlContent, Boolean replaceCurrentContent) {
        load("javascript:setHtml(" + htmlContent + ", " + replaceCurrentContent + ")", null);
    }

    /**
     * Method to get the HTML content and do (any) actions on the result - Java version
     *
     * @param callback ValueCallback<String> action to do
     */
    /*void getHtmlContent(OnHtmlReturned callback) {
        getHtmlContent(s -> {callback.process(it)});
    }*/

    /**
     * Private method to get the HTML content and do (any) actions on the result
     *
     * @param callback ValueCallback<String> action to do
     */
    private void getText(ValueCallback<String> callback) {
        load("javascript:getText()", s -> callback.onReceiveValue(s.substring(1, s.length() - 1).replace("\\n", "\n")));
    }

    /**
     * Method to get the text content and do (any) actions on the result - Java version
     *
     * @param callback ValueCallback<String> action to do
     */
    /*void getText(callback:OnTextReturned) =

    getText {
        callback.process(it)
    }*/

    /**
     * Private method to get the delta content and do (any) actions on the result
     *
     * @param callback ValueCallback<String> action to do
     */
    private void getContents(ValueCallback<String> callback) {
        load("javascript:getContents()", callback);
    }

    /**
     * Method to get the delta content and do (any) actions on the result
     *
     * @param callback ValueCallback<String> action to do
     */
    /*void getContents(callback:((text:String) ->Unit)?)=

    getContents(ValueCallback {
        callback ?.invoke(it)
    } )*/

    /**
     * Method to get the delta content and do (any) actions on the result - Java version
     *
     * @param callback ValueCallback<String> action to do
     */
    /*void getContents(callback:OnContentsReturned) =

    getContents {
        callback.process(it)
    }*/

    /**
     * Method to set the delta content to the editor
     *
     * @param data String delta content as string
     */
    void setContents(String data) {
        load("javascript:setContents($data)", null);
    }

    /**
     * Method to format the current paragraph to the header styles
     *
     * @param level Int 1 -> 6
     */
    private void header(int level) {
        load("javascript:header(" + level + ")", null);
    }

    /**
     * Method to toggle the code view to the current paragraph
     */
    private void codeView() {
        load("javascript:codeView()", null);
    }


    // advanced formatting

    /**
     * Method to allow inserting a hyperlink
     *
     * @param linkUrl String
     */
    private void createLink(String linkUrl) {
        load("javascript:createLink(" + linkUrl + ")", null);
    }


    /**
     * Method to insert a horizontal line
     */
    private void insertHorizontalRule() {
        load("javascript:insertHorizontalRule()", null);
    }

    // end

    /**
     * Method to evaluate a script and do action on result via the callback
     *
     * @param trigger  String script to be evaluated
     * @param callBack ValueCallback<String>
     */
    private void load(String trigger, ValueCallback<String> callBack) {
        webView.post(new Runnable() {
            @Override
            public void run() {
                // Make sure every calls would be run on ui thread
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    webView.evaluateJavascript(trigger, callBack);
                } else {
                    webView.loadUrl(trigger);
                }
            }
        });
    }

    /**
     * A bridge between Jvm api and JS api
     *
     * @param actionType Int type of calling action
     * @param option     String
     */
    void command(int actionType, String option) {
        switch (actionType) {
            case Action.UNDO: {
                undo();
                break;
            }
            case Action.REDO: {
                redo();
                break;
            }
            case Action.BOLD: {
                bold();
                break;
            }
            case Action.ITALIC: {
                italic();
                break;
            }
            case Action.UNDERLINE: {
                underline();
                break;
            }
            case Action.SUBSCRIPT: {
                script("sub");
                break;
            }
            case Action.SUPERSCRIPT: {
                script("super");
                break;
            }
            case Action.STRIKETHROUGH: {
                strikethrough();
                break;
            }
            case Action.NORMAL: {
                header(0);
                break;
            }
            case Action.H1: {
                header(1);
                break;
            }
            case Action.H2: {
                header(2);
                break;
            }
            case Action.H3: {
                header(3);
                break;
            }
            case Action.H4: {
                header(4);
                break;
            }
            case Action.H5: {
                header(5);
                break;
            }
            case Action.H6: {
                header(6);
                break;
            }
            case Action.JUSTIFY_LEFT: {
                align("");
                break;
            }
            case Action.JUSTIFY_CENTER: {
                align("center");
                break;
            }
            case Action.JUSTIFY_RIGHT: {
                align("right");
                break;
            }
            case Action.JUSTIFY_FULL: {
                align("justify");
                break;
            }
            case Action.ORDERED: {
                insertOrderedList();
                break;
            }
            case Action.UNORDERED: {
                insertUnorderedList();
                break;
            }
            case Action.CHECK: {
                insertCheckList();
                break;
            }
            case Action.INDENT: {
                indent();
                break;
            }
            case Action.OUTDENT: {
                outdent();
                break;
            }
            case Action.LINE: {
                insertHorizontalRule();
                break;
            }
            case Action.BLOCK_QUOTE: {
                formatBlockquote();
                break;
            }
            case Action.BLOCK_CODE: {
                formatBlockCode();
                break;
            }
            case Action.CODE_VIEW: {
                codeView();
                break;
            }
            case Action.LINK: {
                createLink(option);
                break;
            }
            case Action.IMAGE: {
                break;
            }
            case Action.SIZE: {
                fontSize(option);
                break;
            }
            case Action.FORE_COLOR: {
                foreColor(option);
                break;
            }
            case Action.BACK_COLOR: {
                backColor(option);
                break;
            }
            case Action.CLEAR: {
                // clear styles
                break;
            }
            default:
                throw new IllegalStateException("Unexpected value: " + actionType);
        }
    }
}
