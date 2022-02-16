package core;
/**
 * Interface for object pool implementation.
 * */
public interface Pool<T> {
	/**
	 * <b>Blocking method.</b><br>
	 * Returns object back to the pool.
	 * @param object {@link PoolObject} to be returned
	 * */
	public void returnObject(PoolObject<T> object) throws InterruptedException;
	/**
	 * <b>Blocking method.</b><br>
	 * Gets an object from the pool.
	 * @return {@link PoolObject} containing the required object from the pool.
	 * */
	public PoolObject<T> getObject() throws InterruptedException;
}
