package ninja.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ninja.Cookie;
import ninja.utils.FakeContext;

public class TestingNinjaContext extends FakeContext {

	private Map<String, Cookie> cookieStore = new HashMap<>();
	
	@Override
	public Cookie getCookie(String cookieName) {
		return cookieStore.get(cookieName);
	}

	@Override
	public List<Cookie> getCookies() {
		return new ArrayList<Cookie>(cookieStore.values());
	}

	@Override
	public void addCookie(Cookie cookie) {
		cookieStore.put(cookie.getName(), cookie);
	}
	
	@Override
    public void unsetCookie(Cookie cookie) {
        cookieStore.remove(cookie.getName());       
    }
}
