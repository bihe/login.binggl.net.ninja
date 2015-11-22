package net.binggl.login.core.entity;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Property;

@Entity(value="users", noClassnameStored=true)
public class User extends BaseEntity {
	private static final long serialVersionUID = 771096671730737730L;

	@Property("UserName")
	private String userName;
	@Property("Email")
	private String email;
	@Property("DisplayName")
	private String displayName;


	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	
}

