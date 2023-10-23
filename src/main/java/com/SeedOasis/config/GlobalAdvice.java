package com.SeedOasis.config;

import com.SeedOasis.config.auth.PrincipalDetails;
import com.SeedOasis.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Arrays;
import java.util.List;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalAdvice {

    private final MemberService memberService;

    @ModelAttribute("profileImg")
    public String memberImg() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!authentication.getPrincipal().equals("anonymousUser")) {
            PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
            String avatar = principalDetails.getMember().getAvatar();
            return avatar;
        }

        return "";
    }
}
