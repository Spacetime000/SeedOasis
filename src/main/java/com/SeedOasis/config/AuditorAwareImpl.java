package com.SeedOasis.config;

import com.SeedOasis.config.auth.PrincipalDetails;
import com.SeedOasis.entity.Member;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collection;
import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<String> {

    /*@Override
    public Optional<Member> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.empty();
        }
        return Optional.ofNullable(((PrincipalDetails) authentication.getPrincipal()).getMember());
    }*/

    @Override
    public Optional<String> getCurrentAuditor() {
        /*return Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .map(authentication -> {
                    Collection<? extends GrantedAuthority> auth = authentication.getAuthorities();
                    boolean isUser = auth.contains(new SimpleGrantedAuthority("ROLE_USER"));
                    if (isUser) return authentication.getName();
                    return null;
                });*/
        return Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .map(Authentication::getName);
    }
}
