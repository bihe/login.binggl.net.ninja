package net.binggl.login.core.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.google.inject.Inject;

import net.binggl.login.core.exceptions.CacheKeyException;
import net.binggl.login.core.service.CacheService;
import ninja.cache.NinjaCache;

/**
 * cache implementation uses the provide cache of the NinjaFramework
 * @author henrik
 */
public class NinjaCacheService implements CacheService {

	private NinjaCache ninjaCache;
	private Map<String, Boolean> keys = new HashMap<>();
	
	@Inject
	public NinjaCacheService(NinjaCache cache) {
		this.ninjaCache = cache;
	}

	@Override
	public synchronized <T> void put(String id, T object) {
		this.check(id);
		if(keys.containsKey(id))
			throw new CacheKeyException("The id "  + id + " is not unique!");
		
		this.ninjaCache.add(id, object);
		this.keys.put(id, true);	
	}
	
	@Override
	public synchronized <T> void replace(String id, T object) {
		this.check(id);
		this.checkIdExists(id);
			
		this.ninjaCache.replace(id, object);
	}

	@Override
	public synchronized <T> T get(String id, Class<T> clasz) {
		this.check(id);
		if(!this.keys.containsKey(id))
			return null;
		T value = this.ninjaCache.get(id, clasz);
		if(value == null) {
			// the cache should not store null values!
			// correct the entry
			this.keys.remove(id);
		}
		return value;
	}

	@Override
	public synchronized <T> T remove(String id, Class<T> clasz) {
		this.check(id);
		this.checkIdExists(id);
		
		T entry = this.get(id, clasz);
		this.ninjaCache.delete(id);
		this.keys.remove(id);
		
		return entry;
	}

	@Override
	public synchronized void invalidate(String id) {
		this.check(id);
		this.checkIdExists(id);
		
		this.ninjaCache.delete(id);
		this.keys.remove(id);
	}
	
	@Override
	public synchronized void clear(String id) {
		this.ninjaCache.delete(id);
		this.keys.remove(id);
	}

	@Override
	public synchronized void clearAll() {
		this.ninjaCache.clear();
		this.keys.clear();
	}
	
	
	private void check(String id) {
		if(id == null || StringUtils.isEmpty(id))
			throw new CacheKeyException("The id is blank!");
	}
	
	private void checkIdExists(String id) {
		if(!keys.containsKey(id))
			throw new CacheKeyException("The id is not available: " + id + "!");
	}
}
