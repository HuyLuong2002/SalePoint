package com.example.salepoint;

public class NotificationData {
    private Message message;

    public NotificationData(Message message) {
        this.message = message;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }
}
