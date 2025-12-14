package com.microbank.userservice.services;

import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {

    String getToken(UserDetails userDetails);

    String getUsernameFromToken(String token);

    boolean isTokenValid(String token, UserDetails userDetails);

    Long getExpirationFromToken(String token);

}