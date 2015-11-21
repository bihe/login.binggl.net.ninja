package models;

import java.util.Date;

/**
 * simple bean holding session/authentication information
 * @author henrik
 */
public class Token {
	// members
	private String value;
	private Date timeStamp;
	private String checksum;
	
	/**
	 * default
	 */
	public Token() {
		super();
	}
	
	/**
	 * @return the sessionValue
	 */
	public String getValue() {
		return value;
	}
	/**
	 * @param sessionValue 
	 */
	public void setValue(String sessionValue) {
		this.value = sessionValue;
		this.checksum = getCalculatedChecksum();
	}
	/**
	 * @return the timeStamp
	 */
	public Date getTimeStamp() {
		return timeStamp;
	}
	/**
	 * @param timeStamp the timeStamp to set
	 */
	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
		this.checksum = getCalculatedChecksum();
	}
	
	public String getCalculatedChecksum() {
		return org.apache.commons.codec.digest.DigestUtils.sha256Hex(this.getHashValues());   
	}
	
	public String getChecksum() {
		return checksum;   
	}
	
	public void setChecksum(String checksum) {
		this.checksum = checksum;
	}
	
	@Override
	public String toString() {
		return "Token [value, timeStamp=" + timeStamp.toString() + "]";
	}
	
	private String getHashValues() {
		return "value: " + this.value + ";timeStamp: " + this.timeStamp.getTime();
	}
	
}