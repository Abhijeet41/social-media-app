package com.abhi41.socialmediaapp.untils;

public class Resource<T> {


    public final Status status;

    public final T data;

    private final String message;


    public Resource(Status status, T data, String message) {
        this.status = status;
        this.data = data;
        this.message = message;

    }


    public static <T> Resource<T> success ( T data)
    {
        return new Resource<>(Status.SUCCESS,data,null);
    }

    public static <T> Resource <T> error (String msg,T data)
    {
        return new Resource<>(Status.ERROR,data,msg);
    }

    public static <T> Resource<T> loading(T data)
    {
        return new Resource<>(Status.LOADING,data,null);
    }

    public String getMessage() {
        return message;
    }
}

 enum Status {
    SUCCESS,
    ERROR,
    LOADING
}
