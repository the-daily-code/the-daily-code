package net.thedailycode;

import jdk.incubator.concurrent.ScopedValue;
import jdk.incubator.concurrent.StructuredTaskScope;

import static net.thedailycode.Principal.ADMIN;
import static net.thedailycode.Principal.GUEST;

public class ScopedValueExample {

    public static class Server {
        final static ScopedValue<Principal> PRINCIPAL =  ScopedValue.newInstance(); // (1)

        void serve(Request request, Response response) {
            var principal = (request.isAuthorized() ? ADMIN : GUEST);
            System.out.println(Thread.currentThread() + " will run application with scoped value " + principal);
            ScopedValue
                    .where(PRINCIPAL, principal)
                    .run(() -> Application.handle(request, response));

            // get will throw NoSuchElementException
            System.out.println(Thread.currentThread() + " application terminated and current scoped value is " + Server.PRINCIPAL.orElse(null));
        }
    }

    public static class Application {
        static void handle(Request request, Response response) {
            new DBAccess().open();
            System.out.println(Thread.currentThread() + " connection acquired with success");

            // create another thread inheriting the scoped value
            try (var scope = new StructuredTaskScope<String>()) {
                scope.fork(Application::readScopedValue);
            }
        }

        static String readScopedValue() {
            var principal = Server.PRINCIPAL.get();
            System.out.println(Thread.currentThread() + " reading inherited principal from a child thread " + principal);
            return principal.toString();
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
