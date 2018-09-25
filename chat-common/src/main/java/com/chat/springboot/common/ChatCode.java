package com.chat.springboot.common;

/**
 * 定义状态、信息的枚举类
 */
public enum ChatCode {

    REMOVE("remove_random_friend");

    private String message;

    ChatCode(String message){
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static void main(String[] args){
        System.out.println(ChatCode.REMOVE.getMessage());
    }
}
