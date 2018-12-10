package com.hz.demo.core;

import java.util.List;

/**
 * Created by Administrator on 2016/10/24.
 */

/**
 * 返回TABLE格式基本类
 */
public class TableReturn {
    public int total;
    public List<?> rows;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<?> getRows() {
        return rows;
    }

    public void setRows(List<?> rows) {
        this.rows = rows;
    }
}
