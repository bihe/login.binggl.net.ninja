package net.binggl.login.core.exceptions;

public class CacheKeyException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public CacheKeyException() {
		super();
	}

	public CacheKeyException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public CacheKeyException(String message, Throwable cause) {
		super(message, cause);
	}

	public CacheKeyException(String message) {
		super(message);
	}

	public CacheKeyException(Throwable cause) {
		super(cause);
	}
}
