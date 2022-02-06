package org.acme;

public class UserInfo {
    public String id;
    public String name;
	public UserInfo() {
	}
	public UserInfo(String id, String name) {
		this.id = id;
		this.name = name;
	}
	@Override
	public String toString() {
		return "UserInfo [id=" + id + ", name=" + name + "]";
	}
    
}
