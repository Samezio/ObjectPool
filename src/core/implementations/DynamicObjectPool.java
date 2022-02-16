package core.implementations;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.IntStream;

import core.Pool;
import core.PoolObject;

/**
 * <b>Abstract class</b><br>
 * Variable size of pool.
 * */
abstract public class DynamicObjectPool<T> implements Pool<T> {

	private final int minimumPoolSize, maximumPoolSize;
	private int currentPoolSize;
	private BlockingQueue<PoolObject<T>> queue;
	
	/**
	 * @param minimumPoolSize Minimum pool size
	 * @param maximumPoolSize Maximum pool size
	 * */
	public DynamicObjectPool(int minimumPoolSize, int maximumPoolSize) {
		this.minimumPoolSize = minimumPoolSize;
		this.maximumPoolSize = maximumPoolSize;
		this.queue = new LinkedBlockingQueue<>();
		IntStream.range(0, minimumPoolSize).forEach(i->this.queue.offer(new PoolObject<T>(createObject(), this)));
		this.currentPoolSize = minimumPoolSize;
	}
	/**
	 * Creates new object of T.
	 * */
	abstract protected T createObject();
	@Override
	public void returnObject(PoolObject<T> object) throws InterruptedException {
		this.queue.put(object);
	}

	@Override
	synchronized public PoolObject<T> getObject() throws InterruptedException {
		if(this.queue.isEmpty() && this.currentPoolSize <= this.maximumPoolSize) {
			this.queue.put(new PoolObject<T>(createObject(), this));
			this.currentPoolSize++;
		}
		return this.queue.take();
	}
	/**
	 * @return Minimum pool size
	 * */
	public int getMinimumPoolSize() {
		return minimumPoolSize;
	}
	/**
	 * @return Maximum pool size
	 * */
	public int getMaximumPoolSize() {
		return maximumPoolSize;
	}
	/**
	 * @return Current pool size
	 * */
	public int getCurrentPoolSize() {
		return currentPoolSize;
	}
	/**
	 * @return Available objects in pool
	 * */
	public int getAvailableObjects(){
		return this.queue.size();
	}
}
