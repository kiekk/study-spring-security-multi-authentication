package com.example.multiauthentication.security.common;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

public class FormWebAuthenticationDetails extends WebAuthenticationDetails {

    public FormWebAuthenticationDetails(HttpServletRequest request) {
        super(request);
    }
}
