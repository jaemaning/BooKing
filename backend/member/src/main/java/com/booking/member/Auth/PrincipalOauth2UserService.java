package com.booking.member.Auth;

import com.booking.member.members.domain.Member;
import com.booking.member.members.repository.MemberRepository;
import com.booking.member.members.domain.UserRole;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class PrincipalOauth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder encoder;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String provider = userRequest.getClientRegistration().getRegistrationId();
//        log.info("provider={}",provider);
//        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
//        log.info("userNameAttributeName={}",userNameAttributeName);

        OAuthAttributes oauthAttributes = OAuthAttributes.of(provider, oAuth2User.getAttributes());
//        log.info("attributes={}",oauthAttributes.toString());

        Map<String,Object> attribute=oauthAttributes.getAttributes();
        String loginId = provider+"_"+oauthAttributes.getId();
        log.info("loadUser {}, {}",provider,loginId);

        Member memberData = memberRepository.findByLoginId(loginId).block();
        Member member;

        if(memberData==null) {
            member=createMember(oauthAttributes,provider,loginId);
            memberRepository.save(member).block();
//            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다");
        } else {
            member=memberData;
        }

        return new PrincipalDetails(member, oAuth2User.getAttributes());
    }

    private Member createMember(OAuthAttributes oAuthAttributes,String provider,String loginId){
        Map<String,Object> attributes=oAuthAttributes.getAttributes();
        if(provider.equals("google")){
            String fullName=(String) attributes.get("family_name")+(String) attributes.get("given_name");
            return Member.builder()
                    .loginId(loginId)
                    .nickname((String) attributes.get("name"))
                    .provider(provider)
                    .role(UserRole.USER)
                    .profileImage((String) attributes.get("picture"))
                    .fullName(fullName)
                    .build();
        }
        else{
            Map<String,Object> profile= (Map<String, Object>) attributes.get("profile");
            return Member.builder()
                    .loginId(loginId)
                    .nickname((String) profile.get("nickname"))
                    .provider(provider)
                    .role(UserRole.USER)
                    .profileImage((String) profile.get("profile_image_url"))
                    .build();
        }
    }

}