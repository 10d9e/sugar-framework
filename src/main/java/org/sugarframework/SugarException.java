package org.sugarframework;

@SuppressWarnings("serial")
public class SugarException extends RuntimeException {

	public SugarException() {
		super();
	}

	public SugarException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public SugarException(String message, Throwable cause) {
		super(message, cause);
	}

	public SugarException(String message) {
		super(message);
	}

	public SugarException(Throwable cause) {
		super(cause);
	}

}
