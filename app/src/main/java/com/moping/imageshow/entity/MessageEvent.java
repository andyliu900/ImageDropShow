package com.moping.imageshow.entity;

public class MessageEvent<T> {

    private T t;
    private String message;

    public MessageEvent() {

    }

    public MessageEvent(T t) {
        this.t = t;
    }

    public MessageEvent(T t, String message) {
        this.t = t;
        this.message = message;
    }

    public MessageEvent(String message){
        this.message = message;
    }

    public T getT() {
        return t;
    }

    public void setT(T t) {
        this.t = t;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
