package com.truper.saen.authenticator;

import org.apache.http.client.methods.RequestBuilder;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.truper.saen.commons.dto.AuthenticationRequest;

public class Requests {
	private Requests() {}

    public static MockHttpServletRequestBuilder getAllRoundsByUserId(String userId,String password) {
    	AuthenticationRequest req=new AuthenticationRequest(userId,password);
        return MockMvcRequestBuilders
                .post("/authenticate/",req)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);
    }
}
