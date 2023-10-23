package com.SeedOasis.config.oauth;

import com.SeedOasis.config.auth.PrincipalDetails;
import com.SeedOasis.config.oauth.provider.GoogleUserInfo;
import com.SeedOasis.config.oauth.provider.NaverUserInfo;
import com.SeedOasis.config.oauth.provider.OAuth2UserInfo;
import com.SeedOasis.entity.Member;
import com.SeedOasis.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);
        OAuth2UserInfo oAuth2UserInfo = null;

        if (userRequest.getClientRegistration().getRegistrationId().equals("google")) {
            oAuth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes());
        } else if (userRequest.getClientRegistration().getRegistrationId().equals("naver")) {
            oAuth2UserInfo = new NaverUserInfo((Map) oAuth2User.getAttributes().get("response"));
        }

        String provider = oAuth2UserInfo.getProvider();
        String providerId = oAuth2UserInfo.getProviderId();
        String memberId = provider + "_" + providerId;
        String password = bCryptPasswordEncoder.encode("임시");
        String email = oAuth2UserInfo.getEmail();

        String name = oAuth2UserInfo.getName();
        String role = "ROLE_USER";

        Member member = memberRepository.findById(memberId).orElse(null);

        if (member == null) {

            Random random = new Random();
            int r;

            while (memberRepository.existsByName(name)) {
                r = random.nextInt(10);
                name += r;
            }

            member = Member.builder()
                    .memberId(memberId)
                    .password(password)
                    .role(role)
                    .email(email)
                    .name(name)
                    .build();

            memberRepository.save(member);
        }

        return new PrincipalDetails(member, oAuth2User.getAttributes());
    }
}
