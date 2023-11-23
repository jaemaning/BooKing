package com.booking.member.util;

import com.booking.member.follows.Repository.FollowRepository;
import com.booking.member.follows.controller.FollowController;
import com.booking.member.follows.service.FollowService;
import com.booking.member.members.controller.MemberController;
import com.booking.member.members.repository.MemberRepository;
import com.booking.member.members.service.MemberService;
import com.booking.member.payments.Repository.PaymentRepository;
import com.booking.member.payments.Service.PaymentService;
import com.booking.member.payments.controller.PaymentController;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;

@AutoConfigureRestDocs
@ActiveProfiles("local")
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(RestDocumentationExtension.class)
@WebMvcTest({
    MemberController.class, PaymentController.class, FollowController.class
})
public class ControllerTest {

    //setup
    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private RestDocumentationContextProvider restDocumentation;

    // Util
    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    // Service
    @MockBean
    protected MemberService memberService;
    @MockBean
    protected PaymentService paymentService;
    @MockBean
    protected FollowService followService;

    // Repository
    @MockBean
    protected MemberRepository memberRepository;
    @MockBean
    protected PaymentRepository paymentRepository;
    @MockBean
    protected FollowRepository followRepository;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                                      .apply(documentationConfiguration(restDocumentation))
                                      .addFilters(new CharacterEncodingFilter("UTF-8", true))
                                      .build();
    }
}
