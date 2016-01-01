package net.binggl.login.core.models;

import java.io.Serializable;
import java.util.List;

/**
 * define a user
 * @author henrik
 */
public class User implements Serializable{

	private static final long serialVersionUID = 1L;
	private String email;
    private String displayName;
    private String userName;
    private String id;
    private List<String> sitePermissions;

    public User(String email, String displayName, String id, String userName) {
        this.email = email;
        this.displayName = displayName;
        this.id = id;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    public List<String> getSitePermissions() {
		return sitePermissions;
	}

	public void setSitePermissions(List<String> sitePermissions) {
		this.sitePermissions = sitePermissions;
	}
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	

    @Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((displayName == null) ? 0 : displayName.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((userName == null) ? 0 : userName.hashCode());
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
		User other = (User) obj;
		if (displayName == null) {
			if (other.displayName != null)
				return false;
		} else if (!displayName.equals(other.displayName))
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (userName == null) {
			if (other.userName != null)
				return false;
		} else if (!userName.equals(other.userName))
			return false;
		return true;
	}

	@Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", displayName='" + displayName + '\'' +
                ", email='" + email + '\'' +
                ", userName='" + userName + '\'' +
                '}';
    }
}
