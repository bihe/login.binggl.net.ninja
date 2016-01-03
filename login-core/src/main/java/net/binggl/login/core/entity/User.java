package net.binggl.login.core.entity;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.PrePersist;
import org.mongodb.morphia.annotations.Property;

import net.binggl.login.core.util.HashHelper;

@Entity(value="users", noClassnameStored=true)
public class User extends BaseEntity {
	private static final long serialVersionUID = 771096671730737730L;

	@Property("AlternativeId")
	private String alternativeId;
	@Property("UserName")
	private String userName;
	@Property("Email")
	private String email;
	@Property("DisplayName")
	private String displayName;
	@Embedded("Sites")
	private List<UserSite> sites;
	

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
	
	public String getAlternativeId() {
		return alternativeId;
	}

	public void setAlternativeId(String alternativeId) {
		this.alternativeId = alternativeId;
	}

	public List<UserSite> getSites() {
		return sites;
	}

	public void setSites(List<UserSite> sites) {
		this.sites = sites;
	}
	

    // lifecycle actions

    @PrePersist
    @Override
    protected void prePersist() {
        super.prePersist();
        
        if(this.alternativeId == null || StringUtils.isEmpty(this.alternativeId)) {
        	this.alternativeId = HashHelper.getSHA(
        			this.toString(),
        			new Date().toString(),
        			UUID.randomUUID().toString());
        }
    }

	@Override
	public String toString() {
		return "User [userName=" + userName + ", email=" + email + ", displayName="
				+ displayName + "]";
	}
    
	
    
    
}