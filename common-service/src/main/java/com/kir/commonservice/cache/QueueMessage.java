package com.kir.commonservice.cache;

public class QueueMessage <T> {
    private String type;
    private T payload;

    public QueueMessage(String type, T payload) {
        this.type = type;
        this.payload = payload;
    }

    public QueueMessage() {
    }

    public static class Builder <T> {
        private String type;
        private T payload;

        public Builder<T> type(String type){
            this.type = type;
            return this;
        }

        public Builder<T> payload(T payload){
            this.payload = payload;
            return this;
        }

        public QueueMessage<T> build(){
            return new QueueMessage<>(type, payload);
        }
    }

    public static <T> Builder<T> builder(){
        return new Builder<>();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public T getPayload() {
        return payload;
    }

    public void setPayload(T payload) {
        this.payload = payload;
    }

}
