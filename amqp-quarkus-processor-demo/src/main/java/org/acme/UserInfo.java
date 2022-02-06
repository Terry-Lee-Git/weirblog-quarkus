package org.acme;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class UserInfo {
    public String id;
    public String name;
	public UserInfo() {
		super();
	}
	public UserInfo(String id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	@Override
	public String toString() {
		return "UserInfo [id=" + id + ", name=" + name + "]";
	}
    
}
