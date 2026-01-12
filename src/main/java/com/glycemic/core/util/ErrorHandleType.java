package com.glycemic.core.util;

public enum ErrorHandleType {
	
	UNAUTHORIZED_ACCESS(1),
	ACCESS_DENIED(2),
	ARGUMENT_NOT_VALID(3),
	MEDIA_TYPE_NOT_SUPPORTED(4),
	METHOD_NOT_SUPPORTED(5),
	MESSAGE_NOT_READABLE(6),
	SERVER_ERROR(7),
	
	CONSTRAINT_VIOLATION(22),
	DATA_INTEGRITY_CONSTRAINT_VIOLATION(23),
	SQL_EXCEPTION(24),
	INVALID_DATA_ACCESS_API_USAGE(25),
	
	JWT_SIGNATURE(35),
    CLASS_CAST_EXCEPTION(36),
    SERIALIZATION_EXCEPTION(37);
	
	private final int value;
	
	ErrorHandleType(int value) {
		this.value = value;
	}
	
	public int value() {
		return this.value;
	}
}
