package com.fpt.fptproducthunt.common.custom;

import org.springframework.security.core.userdetails.UserDetails;

public interface BindUpUserDetails extends UserDetails {
    public String getUserId();
}
