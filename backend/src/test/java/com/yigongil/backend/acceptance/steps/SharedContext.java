package com.yigongil.backend.acceptance.steps;

import com.yigongil.backend.config.auth.JwtTokenProvider;
import io.cucumber.spring.ScenarioScope;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@ScenarioScope
public class SharedContext {

    public static final String REFRESH_TOKEN_PARAMETER_KEY_SUFFIX = "Refresh Token";

    private ExtractableResponse<Response> response;
    private final Map<String, Object> parameters = new HashMap<>();
    private final Map<String, String> tokens = new HashMap<>();

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    public ExtractableResponse<Response> getResponse() {
        return response;
    }

    public Long getId(String key) {
        return Long.parseLong(String.valueOf(parameters.get(key)));
    }

    public void setId(String key, String token) {
        parameters.put(key, jwtTokenProvider.parseToken(token));
    }

    public void setEntityId(String key, String value) {
        parameters.put(key, value);
    }

    public void setRefresh(String key, String refreshToken) {
        parameters.put(key + REFRESH_TOKEN_PARAMETER_KEY_SUFFIX, refreshToken);
    }

    public String getRefresh(String key) {
        return (String) parameters.get(key + REFRESH_TOKEN_PARAMETER_KEY_SUFFIX);
    }

    public void setResponse(ExtractableResponse<Response> response) {
        this.response = response;
    }

    public void setParameter(String key, Object value) {
        parameters.put(key, value);
    }

    public Object getParameter(String key) {
        return parameters.get(key);
    }

    public void setTokens(String userName, String token) {
        tokens.put(userName, token);
    }

    public String getToken(String userName) {
        return tokens.get(userName);
    }
}
