package net.binggl.login.common.models;

import java.util.List;

/**
 * define a user
 * @author henrik
 */
public class User {

    private String email;
    private String displayName;
    private String id;
    private List<String> sitePermissions;

    public User(String email, String displayName, String id) {
        this.email = email;
        this.displayName = displayName;
        this.id = id;
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

	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (getEmail() != null ? !getEmail().equals(user.getEmail()) : user.getEmail() != null) return false;
        if (getDisplayName() != null ? !getDisplayName().equals(user.getDisplayName()) : user.getDisplayName() != null)
            return false;
        return !(getId() != null ? !getId().equals(user.getId()) : user.getId() != null);

    }

    @Override
    public int hashCode() {
        int result = getEmail() != null ? getEmail().hashCode() : 0;
        result = 31 * result + (getDisplayName() != null ? getDisplayName().hashCode() : 0);
        result = 31 * result + (getId() != null ? getId().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", displayName='" + displayName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
