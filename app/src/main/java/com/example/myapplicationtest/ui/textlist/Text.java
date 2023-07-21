package com.example.myapplicationtest.ui.textlist;

import java.io.Serializable;

public class Text implements Serializable {
        private String name;
        private  int unit;
        private  String content;
        String url;
        public Text(String name, int unit, String content, String url){
            this.content = content;
            this.unit = unit;
            this.name=name;
            this.url = url;
        }

    public String getUrl() {
        return url;
    }

   public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getUnit() {
        return unit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}