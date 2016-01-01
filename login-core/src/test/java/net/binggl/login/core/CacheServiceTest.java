package net.binggl.login.core;

import static org.junit.Assert.*;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import net.binggl.login.core.exceptions.CacheKeyException;
import net.binggl.login.core.service.CacheService;
import net.binggl.login.core.service.impl.NinjaCacheService;
import ninja.Context;
import ninja.cache.NinjaCache;
import ninja.util.TestingNinjaContext;

@RunWith(MockitoJUnitRunner.class)
public class CacheServiceTest {
	
	@Mock
	NinjaCache cache;
	
	Context context = new TestingNinjaContext();
	CacheService cacheService;
	
	private final String ID = "__ID__";
	private final TestObject TESTOBJECT = new TestObject("NAME", "TITLE");
	
	@Before
	public void setup() {
		Map<String, Object> fakeCache = new HashMap<>();
		fakeCache.put(ID, TESTOBJECT);
		
		when(cache.get(ID)).then(invocation -> {
			String value = invocation.getArguments()[0].toString();
			return fakeCache.get(value);
		});
		
		when(cache.get(ID, TestObject.class)).then(invocation -> {
			String value = invocation.getArguments()[0].toString();
			return (TestObject)fakeCache.get(value);
		});
		
		doAnswer(invocation -> {
			String key = invocation.getArguments()[0].toString();
			Object value = invocation.getArguments()[1];
			return fakeCache.put(key, value);
		}).when(cache).set(ID, TESTOBJECT);
		
		doAnswer(invocation -> {
			String key = invocation.getArguments()[0].toString();
			Object value = invocation.getArguments()[1];
			return fakeCache.put(key, value);
		}).when(cache).add(ID, TESTOBJECT);
		
		doAnswer(invocation -> {
			String key = invocation.getArguments()[0].toString();
			return fakeCache.put(key, null);
		}).when(cache).replace(ID, null);
		
		doAnswer(invocation -> {
			fakeCache.clear();
			return null;
		}).when(cache).clear();
		
		cacheService = new NinjaCacheService(cache);
	}
	
	@Test
	public void testCacheServiceMethods() {
		cacheService.put(ID, TESTOBJECT);
		TestObject entry = cacheService.get(ID, TestObject.class);
		assertNotNull(entry);
		
		TestObject entry1 = cacheService.remove(ID, TestObject.class);
		assertNotNull(entry1);
		
		TestObject entry2 = cacheService.get(ID, TestObject.class);
		assertNull(entry2);
		
		cacheService.put(ID, TESTOBJECT);
		cacheService.invalidate(ID);
		
		TestObject entry3 = cacheService.get(ID, TestObject.class);
		assertNull(entry3);
		
		cacheService.put(ID, TESTOBJECT);
		cacheService.clearAll();
		
		TestObject entry4 = cacheService.get(ID, TestObject.class);
		assertNull(entry4);
		
		cacheService.put(ID, TESTOBJECT);
		cacheService.replace(ID, new TestObject(TESTOBJECT.getName(), TESTOBJECT.getTitle()));
		TestObject entry5 = cacheService.get(ID, TestObject.class);
		assertNotNull(entry5);
		assertEquals(TESTOBJECT, entry5);
	}
	
	@Test(expected = CacheKeyException.class )
	public void testCacheServiceUniqueCacheKey() {
		cacheService.put(ID, TESTOBJECT);
		cacheService.put(ID, TESTOBJECT);
	}
	
	
	private class TestObject implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private String name;
		private String title;
		
		
		
		public TestObject(String name, String title) {
			super();
			this.name = name;
			this.title = title;
		}
		public String getName() {
			return name;
		}
		public String getTitle() {
			return title;
		}
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + ((name == null) ? 0 : name.hashCode());
			result = prime * result + ((title == null) ? 0 : title.hashCode());
			return result;
		}
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			TestObject other = (TestObject) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (name == null) {
				if (other.name != null)
					return false;
			} else if (!name.equals(other.name))
				return false;
			if (title == null) {
				if (other.title != null)
					return false;
			} else if (!title.equals(other.title))
				return false;
			return true;
		}
		private CacheServiceTest getOuterType() {
			return CacheServiceTest.this;
		}
		
		
	}
}
