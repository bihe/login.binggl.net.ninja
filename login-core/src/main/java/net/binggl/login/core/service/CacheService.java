package net.binggl.login.core.service;

/**
 * definition of a cache service
 * rather basic methods like put, get, remove
 * @author henrik
 */
public interface CacheService {

	/**
	 * add a new entry to the cache
	 * @param id unique id for a cache entry
	 * @param object the object to put into the cache
	 */
	<T> void put(String id, T object);
	
	/**
	 * replace an existing entry available in the cache
	 * @param id unique id for a cache entry
	 * @param object the object to put into the cache
	 */
	<T> void replace(String id, T object);
	
	/**
	 * retrieve a value from the cache
	 * @param id unique id for a cache entry
	 * @param clasz the class/type of the object
	 * @return the object or null if not found
	 */
	<T> T get(String id, Class<T> clasz);
	
	/**
	 * retrieve and delete the value from the cache
	 * @param id unique id for a cache entry
	 * @param clasz the class/type of the object
	 * @return the object or null if not found
	 */
	<T> T remove(String id, Class<T> clasz);
	
	/**
	 * invalidate the object with the given id
	 * throws an exception if the id is not available
	 * @param id
	 */
	void invalidate(String id);
	
	/**
	 * invalidate the object with the given id
	 * @param id
	 */
	void clear(String id);
	
	/**
	 * clear the whole cache
	 */
	void clearAll();
}
