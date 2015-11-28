package net.binggl.login.core.entity;

import java.util.List;

import org.mongodb.morphia.annotations.Property;

public class UserSite {

	@Property("Name")
	private String name;
	@Property("Url")
	private String url;
	@Property("Permissions")
	private List<String> permissions;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public List<String> getPermissions() {
		return permissions;
	}
	public void setPermissions(List<String> permissions) {
		this.permissions = permissions;
	}
}
