package net.thedailycode;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static net.thedailycode.Main.somniloquy;

class UnstructuredConcurrency {
    record Response(String user, Integer order) {};

    ExecutorService esvc = Executors.newCachedThreadPool();

    Response handle() throws ExecutionException, InterruptedException {
        Future<String> user  = esvc.submit(this::findUser);
        Future<Integer> order = esvc.submit(this::fetchOrder);
        String theUser = user.get();   // Join findUser
        int theOrder = order.get();  // Join fetchOrder
        return new Response(theUser, theOrder);
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

    void shutdown() { esvc.shutdown(); }
}
