package core;

/**
 * A wrapper class for the objects in the pool.<br>
 * Its main purpose is to return the object back to the pool on close. 
 * */
public class PoolObject<T> implements AutoCloseable {
	private final T object;
	private final Pool<T> pool;
	/**
	 * @param object the object.
	 * @param pool the {@link Pool} the object is from.
	 * */
	public PoolObject(T object, Pool<T> pool) {
		this.object = object;
		this.pool = pool;
	}
	/**
	 * @return the object
	 * */
	public T getObject() {
		return object;
	}
	/**
	 * returns the object back to the pool.
	 * */
	@Override
	public void close() throws InterruptedException {
		this.pool.returnObject(this);
	}
}
