package com.innova.sharks.utils;

public class NetworkState {
    private volatile static NetworkState INSTANCE;
    private Status status;

    public NetworkState(Status status) {
        this.status = status;
    }

    public static NetworkState getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new NetworkState(Status.LOADED);
        }
        return INSTANCE;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public enum Status {
        LOADING,
        LOADED,
        FAILED
    }
}
