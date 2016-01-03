package net.binggl.login.core.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Site implements Serializable {

	private static final long serialVersionUID = -7230046787984778632L;

	private final String name;
	private final String url;
	private final List<String> permissions;
	
	private Site(final String name, final String url, final List<String> permissions) {
		super();
		this.name = name;
		this.url = url;
		this.permissions = permissions;
	}
		
	public String getName() {
		return name;
	}
	
	public String getUrl() {
		return url;
	}
	
	public List<String> getPermissions() {
		return permissions;
	}
	
	
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((permissions == null) ? 0 : permissions.hashCode());
		result = prime * result + ((url == null) ? 0 : url.hashCode());
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
		Site other = (Site) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (permissions == null) {
			if (other.permissions != null)
				return false;
		} else if (!permissions.equals(other.permissions))
			return false;
		if (url == null) {
			if (other.url != null)
				return false;
		} else if (!url.equals(other.url))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "Site [name=" + name + ", url=" + url + "]";
	}


	public static class SiteBuilder {
		
		private String name;
		private String url;
		private List<String> permissions;
		
		public SiteBuilder() {
			this.permissions = new ArrayList<>();
		}
		
		public SiteBuilder name(final String name) {
			this.name = name;
			return this;
		}
		
		public SiteBuilder url(final String url) {
			this.url = url;
			return this;
		}
		
		public SiteBuilder permissions(final List<String> perm) {
			if(perm != null)
				this.permissions = perm;
			return this;
		}
		
		public Site build() {
			return new Site(name, url, permissions);
		}
		
	}
}