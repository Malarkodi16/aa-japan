package com.nexware.aajapan.exceptions;

public class AAJRuntimeException extends RuntimeException {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public AAJRuntimeException(String message) {
		super(message);
	}

	public AAJRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}
}
