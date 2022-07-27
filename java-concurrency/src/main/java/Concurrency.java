import java.util.Collection;
import java.util.LinkedList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class Concurrency {
	ExecutorService pool = Executors.newFixedThreadPool(4);

	public static void main(String[] args) throws Exception{
		Concurrency example = new Concurrency();
		example.lambdaThread();
//		example.createSupplierAsync();
//		example.submitMultipleFeature();
//		example.pool.shutdown();
		
	}
	
	private void createSupplierAsync() throws InterruptedException, ExecutionException {
		
		CompletableFuture<String> helloSupplyAsync = 
				CompletableFuture.supplyAsync(()->"Hello", pool);
		CompletableFuture<String> transformAsync = helloSupplyAsync.thenApplyAsync((s) -> {
			// execute more business logic here to transform it
			int a  = 10/0;
			
			return s+" world";
		}, pool);
		
		CompletableFuture<String> greetings = 
				CompletableFuture.supplyAsync(()->"how are you", pool);
//		greetings.thenCombineAsync(helloSupplyAsync, pool);
		System.out.println(transformAsync.get());
		
	}
	
	private void submitMultipleFeature() throws InterruptedException, ExecutionException, TimeoutException {
		Collection<Future<?>> futures = new LinkedList<Future<?>>();
		Future<String> first = pool.submit(() -> {
			System.out.println(Thread.currentThread().getName());
			Thread.sleep(2000);
			return "hi";
		});
		Future<String> second = pool.submit(() -> {
			System.out.println(Thread.currentThread().getName());
			Thread.sleep(2000);
			return "hello";
		});
		Future<String> third = pool.submit(() -> {

			System.out.println(Thread.currentThread().getName());
			Thread.sleep(3000);
			return "how are u ";
		});
		futures.add(first);
		futures.add(second);
		futures.add(third);
		
		for (Future<?> future : futures) {
			System.out.println("not blocking start");
			String object = (String) future.get(20,TimeUnit.SECONDS);
			System.out.println("not blocking"+object);
		}
		
	}

	private  void lambdaThread() throws InterruptedException, ExecutionException {
		Future<String> submit = pool.submit(() -> {
			System.out.println("Running suppied task"+ Thread.currentThread().getName());
			Thread.sleep(3000);
			return "hi";
		});
		
		submit.get(); // blocking call. main thread will wait for 3 sec unless taks completes 
		System.out.println("main thread" + Thread.currentThread().getName());
	}

}
