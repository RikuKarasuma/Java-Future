package practice.future;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ExecutionException;
import java.util.List;
import java.util.ArrayList;

/**
 * Practice with Futures and the ExecutorService thread pool.
 * Runs Fifty-Thousand Integer computations asynchronously, then sums each of the results.
 * @author Owen McMonagle
 * @since 25/11/2019
 * @version 0.1
 */
public class FuturePractice
{

    public static void main(String[] args)
    {
        // Create a thread pool for the futures to operate on.
        ExecutorService thread_pool = Executors.newFixedThreadPool(5);

        // Create a Callable computation.
        Callable<Integer> computation = () ->
        {
            final int random_multiplier = new Random().nextInt(50);
            System.out.println("Computing... multiplier is:" + random_multiplier);
            return 3 * random_multiplier;
        };

        // Add multiple callable computations to a list for
        List<Callable<Integer>> computations = new ArrayList<>();
        // Create Fifty thousand computations to execute.
        for(int i = 0; i < 50000; i ++)
            computations.add(computation);

        List<Future<Integer>> computation_results = null;
        try {
            // Invoke the computations to be run on the thread pool.
            computation_results = thread_pool.invokeAll(computations);
            // Map each of the futures to the integer result.
            int sum_of_computations = computation_results.stream()
                    .mapToInt((future) -> {
                        try {
                            return future.get();
                        } catch (InterruptedException | ExecutionException e) {
                            e.printStackTrace();
                        }
                        return 0;
                    })
                    // Sum the results into the final calculation.
                    .sum();

            // Print out the final calculation from the asynchronous computations.
            System.out.println("Final Summation:" + sum_of_computations);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Shutdown our thread pool or else the program won't end.
        thread_pool.shutdownNow();
    }
}
