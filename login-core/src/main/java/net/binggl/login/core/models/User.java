package net.binggl.login.core.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * define a user
 * @author henrik
 */
public class User implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private final String id;
	private final String email;
    private final String displayName;
    private final String userName;
    private final List<Site> sites;

    private User(final String id, final String email, final String displayName, final String userName, final List<Site> sites) {
        this.email = email;
        this.displayName = displayName;
        this.id = id;
        this.userName = userName;
        this.sites = sites;
    }

    public String getEmail() {
        return email;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getId() {
        return id;
    }

    public List<Site>  getSitePermissions() {
		return sites;
	}

	public String getUserName() {
		return userName;
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
	
	
	public static class UserBuilder {
		
		private String id;
		private String email;
	    private String displayName;
	    private String userName;
	    private List<Site> sites;
		
		public UserBuilder() {
			this.sites = new ArrayList<>();
		}
		
		public UserBuilder id(final String id) {
			this.id = id;
			return this;
		}
		
		public UserBuilder email(final String email) {
			this.email = email;
			return this;
		}
		
		public UserBuilder displayName(final String displayName) {
			this.displayName = displayName;
			return this;
		}
		
		public UserBuilder userName(final String userName) {
			this.userName = userName;
			return this;
		}
		
		public UserBuilder sites(final List<Site> siteList) {
			if(siteList != null)
				this.sites = siteList;
			return this;
		}
		
		public User build() {
			return new User(id, email, displayName,userName, sites);
		}
		
	}
	
}
