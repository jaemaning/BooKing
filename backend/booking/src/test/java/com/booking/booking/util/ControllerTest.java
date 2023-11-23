package com.booking.booking.util;

import com.booking.booking.meeting.controller.MeetingController;
import com.booking.booking.meeting.repository.MeetingRepository;
import com.booking.booking.meeting.service.MeetingService;
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
        MeetingController.class,
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
    protected MeetingService meetingService;


    // Repository
    @MockBean
    protected MeetingRepository meetingRepository;


    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation))
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .build();
    }
}
