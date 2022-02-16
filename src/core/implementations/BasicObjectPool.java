package core.implementations;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import core.Pool;
import core.PoolObject;

/**
 * Basic Object pool implementation, has static number of pre-constructed objects.<br>
 * Uses {@link BlockingQueue} for storing objects.
 * */
public class BasicObjectPool<T> implements Pool<T> {
	private final BlockingQueue<PoolObject<T>> queue;
	/**
	 * @param objects array of pre-constructed objects to be stored in pool.
	 * */
	public BasicObjectPool(T[] objects) {
		this.queue = new LinkedBlockingQueue<PoolObject<T>>(objects.length);
		for (T t : objects) {
			this.queue.offer(new PoolObject<T>(t, this));
		}
	}
	@Override
	public void returnObject(PoolObject<T> object) throws InterruptedException {
		this.queue.put(object);
	}

	@Override
	public PoolObject<T> getObject() throws InterruptedException {
		return this.queue.take();
	}
}
