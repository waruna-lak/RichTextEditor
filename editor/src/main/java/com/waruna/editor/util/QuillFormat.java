package com.waruna.editor.util;

import com.google.gson.annotations.SerializedName;

public class QuillFormat {
    @SerializedName("bold")
    Boolean isBold;
    @SerializedName("italic")
    Boolean isItalic;
    @SerializedName("underline")
    Boolean isUnderline;
    @SerializedName("strike")
    Boolean isStrike;
    @SerializedName("header")
    int header;
    @SerializedName("script")
    String script;
    @SerializedName("align")
    String align;
    @SerializedName("list")
    String list;
    @SerializedName("code")
    Boolean isCode;
    @SerializedName("size")
    String size;
    @SerializedName("link")
    String link;

    public QuillFormat() {
        size = "normal";
        script = "";
        align = "";
        list = "";
        size = "";
        link = "";
    }

    public Boolean getBold() {
        return isBold;
    }

    public void setBold(Boolean bold) {
        isBold = bold;
    }

    public Boolean getItalic() {
        return isItalic;
    }

    public void setItalic(Boolean italic) {
        isItalic = italic;
    }

    public Boolean getUnderline() {
        return isUnderline;
    }

    public void setUnderline(Boolean underline) {
        isUnderline = underline;
    }

    public Boolean getStrike() {
        return isStrike;
    }

    public void setStrike(Boolean strike) {
        isStrike = strike;
    }

    public int getHeader() {
        return header;
    }

    public void setHeader(int header) {
        this.header = header;
    }

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }

    public String getAlign() {
        return align;
    }

    public void setAlign(String align) {
        this.align = align;
    }

    public String getList() {
        return list;
    }

    public void setList(String list) {
        this.list = list;
    }

    public Boolean getCode() {
        return isCode;
    }

    public void setCode(Boolean code) {
        isCode = code;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public boolean isEmpty() {
        return isBold == null && isItalic == null && isUnderline == null && isStrike == null && header == 0 && script == null && align == null && list == null && isCode == null && size.equals("normal") && link == null;
    }
}
