package com.philips.easykey.lock.bean;

import java.io.Serializable;

/**
 * author :
 * time   : 2021/4/15
 * E-mail : wengmaowei@kaadas.com
 * desc   :
 */
public abstract class SectionEntity<T> implements Serializable {
    public boolean isHeader;
    public T t;
    public String header;

    public SectionEntity(boolean isHeader, String header) {
        this.isHeader = isHeader;
        this.header = header;
        this.t = null;
    }

    public SectionEntity(T t) {
        this.isHeader = false;
        this.header = null;
        this.t = t;
    }
}