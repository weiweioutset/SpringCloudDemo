package com.cloud.demo.idgenerator.entity;

/**
 * @Author weiwei
 * @Date 2022/6/18 下午6:51
 * @Version 1.0
 * @Desc
 */
public class Result {
    private int code;
    private long id;

    public Result(int code, long id) {
        this.code = code;
        this.id = id;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "[id:" + id + ",code:" + code + "]";
    }
}
