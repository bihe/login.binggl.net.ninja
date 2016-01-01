package net.binggl.login.core.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.google.inject.Inject;

import net.binggl.login.core.exceptions.CacheKeyException;
import net.binggl.login.core.service.CacheService;
import ninja.cache.NinjaCache;

public class NinjaCacheService implements CacheService {

	private NinjaCache ninjaCache;
	private Map<String, Boolean> keys = new HashMap<>();
	
	@Inject
	public NinjaCacheService(NinjaCache cache) {
		this.ninjaCache = cache;
	}

	@Override
	public <T> void put(String id, T object) {
		if(StringUtils.isEmpty(id))
			throw new CacheKeyException("The id is blank!");
		if(keys.containsKey(id))
			throw new CacheKeyException("The id "  + id + " is not unique!");
		
		this.ninjaCache.add(id, object);
		this.keys.put(id, true);	
		
	}
	
	@Override
	public <T> void replace(String id, T object) {
		if(StringUtils.isEmpty(id))
			throw new CacheKeyException("The id is blank!");
		if(!keys.containsKey(id))
			throw new CacheKeyException("The id is not available: " + id + "!");
			
		this.ninjaCache.replace(id, object);
		
	}

	@Override
	public <T> T get(String id, Class<T> clasz) {
		if(StringUtils.isEmpty(id))
			throw new CacheKeyException("The id is blank!");
		if(!this.keys.containsKey(id))
			return null;
		return this.ninjaCache.get(id, clasz);	
	}

	@Override
	public <T> T remove(String id, Class<T> clasz) {
		if(StringUtils.isEmpty(id))
			throw new CacheKeyException("The id is blank!");
		if(!keys.containsKey(id))
			throw new CacheKeyException("The id is not available: " + id + "!");
		
		T entry = this.get(id, clasz);
		this.ninjaCache.replace(id, null);
		this.keys.remove(id);
		
		return entry;
	}

	@Override
	public void invalidate(String id) {
		if(StringUtils.isEmpty(id))
			throw new CacheKeyException("The id is blank!");
		if(!keys.containsKey(id))
			throw new CacheKeyException("The id is not available: " + id + "!");
		
		this.ninjaCache.set(id, null);
		this.keys.remove(id);
	}

	@Override
	public void clearAll() {
		this.ninjaCache.clear();
		this.keys.clear();
	}
}
