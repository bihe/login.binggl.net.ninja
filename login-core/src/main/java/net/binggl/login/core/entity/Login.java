package net.binggl.login.core.entity;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Property;

@Entity(value="logins", noClassnameStored=true)
public class Login extends BaseEntity {

	private static final long serialVersionUID = 6144997230131947987L;

	@Property("UserName")
	private String userName;
	@Property("UserId")
	private String userId;
	@Property("Type")
	private String type;
	
	public Login() {
		super();
	}
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
}