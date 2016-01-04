package net.binggl.login.core.models;

import java.util.ArrayList;
import java.util.List;

public class Token {

	private final String userId;
	private final String userName;
	private final String displayName;
	private final String email;
	private final List<String> claims;
	
	private Token(final String userId, final String userName, final String displayName, final String email, final List<String> claims) {
		super();
		this.userId = userId;
		this.userName = userName;
		this.displayName = displayName;
		this.email = email;
		this.claims = claims;
	}
	
	public String getUserId() {
		return userId;
	}
	public String getUserName() {
		return userName;
	}
	public String getDisplayName() {
		return displayName;
	}
	public String getEmail() {
		return email;
	}
	public List<String> getClaims() {
		return claims;
	}
	
	public static class TokenBuilder {
		
		private String userId;
		private String userName;
		private String displayName;
		private String email;
		private List<String> claims;
		
		public TokenBuilder() {
			this.claims = new ArrayList<>();
		}
		
		public TokenBuilder userId(final String userId) {
			this.userId = userId;
			return this;
		}
		
		public TokenBuilder userName(final String userName) {
			this.userName = userName;
			return this;
		}
		
		public TokenBuilder displayName(final String displayName) {
			this.displayName = displayName;
			return this;
		}
		
		public TokenBuilder email(final String email) {
			this.email = email;
			return this;
		}
		
		public TokenBuilder claims(final List<String> claimList) {
			if(claimList != null)
				this.claims = claimList;
			return this;
		}

		public Token build() {
			return new Token(userId, userName, displayName, email, claims);
		}
	}

	@Override
	public String toString() {
		return "Token [userId=" + userId + ", userName=" + userName + ", displayName=" + displayName + ", email="
				+ email + "]";
	}
}
