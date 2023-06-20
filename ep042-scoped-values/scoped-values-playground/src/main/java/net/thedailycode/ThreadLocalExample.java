package net.thedailycode;

import static net.thedailycode.Principal.ADMIN;
import static net.thedailycode.Principal.GUEST;

public class ThreadLocalExample {

    public static class Server {
        final static ThreadLocal<Principal> PRINCIPAL = new ThreadLocal<>();

        void serve(Request request, Response response) {
            var principal  = (request.isAuthorized() ? ADMIN : GUEST);
            PRINCIPAL.set(principal);
            System.out.println(Thread.currentThread() + " setting principal to " + PRINCIPAL.get());
            Application.handle(request, response);
        }
    }

    public static class Application {
        static void handle(Request request, Response response) {
            new DBAccess().open();
            System.out.println(Thread.currentThread() + " DB connection acquired with success");
        }
    }

    static class DBAccess {
        DBConnection open() {
            var principal = Server.PRINCIPAL.get();
            if (!principal.canOpen()) throw new InvalidPrincipalException();
            return new DBConnection();
        }
    }
}
