package com.hz.demo.core;

/**
 * 返回tree格式类
 */
public class Tree {
    public String id;
    public String pid;
    public String text;
    public int checked;//是否默认选中
    //树需要的其他属性字段，多个可以拼接成一个attributes，没有可以为空
    public String attributes;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pId) {
        this.pid = pId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getChecked(){return checked;}

    public void setChecked(int checked){this.checked=checked;}

    public String getAttributes() {
        return attributes;
    }

    public void setAttributes(String attributes) {
        this.attributes = attributes;
    }


}
