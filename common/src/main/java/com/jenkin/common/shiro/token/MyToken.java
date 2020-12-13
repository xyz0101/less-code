package com.jenkin.common.shiro.token;

import com.jenkin.common.entity.dtos.system.UserDto;
import org.apache.shiro.authc.AuthenticationToken;

public class MyToken implements AuthenticationToken {
	
	private static final long serialVersionUID = 1L;
	private UserDto userDto;
    private String token;
    public MyToken(UserDto userDto) {
        this.userDto = userDto;
    }
 
    @Override
    public UserDto getPrincipal() {
        return userDto;
    }
 
    @Override
    public String getCredentials() {
        return userDto.getPassword();
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
