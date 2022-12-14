package net.thedailycode;

import jdk.incubator.concurrent.StructuredTaskScope;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static net.thedailycode.Main.somniloquy;

public class StructuredConcurrency {

    record Response(String user, Integer order) {};

    Response handle() throws ExecutionException, InterruptedException {
        try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
            Future<String> user  = scope.fork(this::findUser);
            Future<Integer> order = scope.fork(this::fetchOrder);

            scope.join();           // Join both forks
            scope.throwIfFailed();  // ... and propagate errors

            // Here, both forks have succeeded, so compose their results
            return new Response(user.resultNow(), order.resultNow());
        }
    }

    String findUser() {
        // Case 1. Fail while fetchOrder keeps running
        //throw new RuntimeException("Failed to findUser");

        somniloquy(1000L, 5, "Finding user...");

        // Regular case
        return "Foo";
    }

    Integer fetchOrder() {
        // Case 1. Keep running even if findUser failed
        somniloquy(1000L, 5, "Fetching order...");

        // Case 3. If findUser is running and fetchOrder already failed `handle` has to wait for findUser to finish
        //throw new RuntimeException("Failed to fetch order");

        return 1234;
    }
}
