package net.binggl.login.core.entity;

/**
 * define the possible login types
 * either the user directly loged in or the login process was requested by another application
 * @author henrik
 */
public enum LoginType {
	DIRECT, FORWARD
}
