package core.implementations;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.IntStream;

import core.Pool;
import core.PoolObject;

/**
 * <b>Abstract class</b><br>
 * Holds some pre_cooked objects and if no object is available at the time of call, it will create new one.
 * */
abstract public class PsudoObjectPool<T> implements Pool<T> {

	private final int minimumPoolSize;
	private BlockingQueue<PoolObject<T>> queue;
	
	/**
	 * @param minimumPoolSize Minimum pool size
	 * */
	public PsudoObjectPool(int minimumPoolSize) {
		this.minimumPoolSize = minimumPoolSize;
		this.queue = new LinkedBlockingQueue<>();
		IntStream.range(0, minimumPoolSize).forEach(i->this.queue.offer(new PoolObject<T>(createObject(), this)));
	}
	/**
	 * Creates new object of T.
	 * */
	abstract protected T createObject();
	/**
	 * Destroys new object of T. The is called on extra object.<br>
	 * Operation like closing IO, etc should be done here. 
	 * */
	abstract protected T destroyObject(T object);
	@Override
	public void returnObject(PoolObject<T> object) throws InterruptedException {
		if(this.queue.size() == minimumPoolSize) {
			destroyObject(object.getObject());
		}else {
			this.queue.put(object);			
		}
	}

	@Override
	synchronized public PoolObject<T> getObject() throws InterruptedException {
		if(this.queue.isEmpty()) {
			this.queue.put(new PoolObject<T>(createObject(), this));
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
	 * @return Available objects in pool
	 * */
	public int getAvailableObjects(){
		return this.queue.size();
	}
}
