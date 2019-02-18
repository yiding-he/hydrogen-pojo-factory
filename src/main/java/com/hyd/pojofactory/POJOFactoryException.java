package com.hyd.pojofactory;

public class POJOFactoryException extends RuntimeException {

    public POJOFactoryException() {
    }

    public POJOFactoryException(String message) {
        super(message);
    }

    public POJOFactoryException(String message, Throwable cause) {
        super(message, cause);
    }

    public POJOFactoryException(Throwable cause) {
        super(cause);
    }
}
