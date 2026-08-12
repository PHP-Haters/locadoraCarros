package com.sword.aluguelCarros.ExceptionHandlers;

public class GenericExceptions {
    public static class NotFound extends RuntimeException {
        public NotFound(String message) { super(message); }
    }

    public static class AlreadyExists extends RuntimeException {
        public AlreadyExists(String message) { super(message); }
    }

    public static class InvalidData extends RuntimeException {
        public InvalidData(String message) { super(message); }
    }

    public static class Unauthorized extends RuntimeException {
        public Unauthorized(String message) { super(message); }
    }

    public static class General extends RuntimeException {
        public General(String message) { super(message); }
    }
}

