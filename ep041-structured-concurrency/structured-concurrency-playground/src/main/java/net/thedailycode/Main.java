package net.thedailycode;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        //var experiment = new Thread(Main::runUnstructuredConcurrencyExperiment);
        //experiment.start();
        // Case 2. The thread is interrupted but the findUser and fetchOrder keep running
        //experiment.interrupt();

        var experiment = Thread.startVirtualThread(Main::runStructuredConcurrencyExperiment);
        //somniloquy(100, 2, null);
        //experiment.interrupt();
        experiment.join();
    }

    public static void runUnstructuredConcurrencyExperiment() {
        var uc = new UnstructuredConcurrency();
        try {
            UnstructuredConcurrency.Response resp = uc.handle();
            System.out.println(resp);
        } catch (Exception e) {
            System.err.println(Thread.currentThread() + " Exception runUnstructuredConcurrencyExperiment" + e);
        } finally {
            uc.shutdown();
        }
    }

    public static void runStructuredConcurrencyExperiment() {
        var sc = new StructuredConcurrency();
        try {
            var resp = sc.handle();
            System.out.println(resp);
        } catch (Exception e) {
            System.err.println("Exception runStructuredConcurrencyExperiment " + e);
        }
    }

    public static void somniloquy(long nap, int times, String talk) {
        try {
            while (times-- > 0) {
                if (talk != null)
                    System.out.println(Thread.currentThread() + " " + talk);
                Thread.sleep(nap);
            }
        } catch (InterruptedException e) {
            System.err.println(Thread.currentThread() + " Interrupted somniloquy " + talk + " " + e);
            throw new RuntimeException(e);
        }
    }
}
