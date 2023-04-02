package com.zong.call.event;

public class MessageWrap {

    public final boolean isUnlock;

    public static MessageWrap getInstance(boolean isUnlock) {
        return new MessageWrap(isUnlock);
    }

    private MessageWrap(boolean isUnlock) {
        this.isUnlock = isUnlock;
    }
}