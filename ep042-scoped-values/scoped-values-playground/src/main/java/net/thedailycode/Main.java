package net.thedailycode;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        runThreadLocalExample();
        //runScopedValueExample();
    }

    public static void runThreadLocalExample() throws InterruptedException {
        var server = new ThreadLocalExample.Server();
        var t1 = new Thread(() -> server.serve(new Request("ADMIN"), new Response()));
        var t2 = new Thread(() -> server.serve(new Request("GUEST"), new Response()));
        t1.start(); t2.start();
        t1.join(); t2.join();
    }

    public static void runScopedValueExample() throws InterruptedException {
        var server = new ScopedValueExample.Server();
        var t1 = Thread.startVirtualThread(() -> server.serve(new Request("ADMIN"), new Response()));
        var t2 = Thread.startVirtualThread(() -> server.serve(new Request("GUEST"), new Response()));
        t1.join(); t2.join();
    }
}

enum Principal {
    ADMIN, GUEST;

    boolean canOpen() {
        return switch (this) {
            case ADMIN -> true;
            case GUEST -> false;
        };
    }
}

class InvalidPrincipalException extends RuntimeException { }

record Request(String user) {
    boolean isAuthorized() {
        boolean isAuthorized = user.equals("ADMIN");
        System.out.println(Thread.currentThread() + " authorized? " + isAuthorized);
        return isAuthorized;
    }
};

record Response() {};

class DBConnection {};