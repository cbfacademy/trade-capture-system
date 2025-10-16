package com.technicalchallenge.applicationuser;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.technicalchallenge.userprofile.UserProfile;
import com.technicalchallenge.userprofile.UserProfileService;

@WebMvcTest(ApplicationUserController.class)
public class ApplicationUserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ApplicationUserService applicationUserService;
    @MockitoBean
    private ApplicationUserMapper applicationUserMapper;
    @MockitoBean
    private UserProfileService userProfileService;


    @BeforeEach
    public void setup() {
        ApplicationUser applicationUser = new ApplicationUser();
        applicationUser.setId(2L);
        applicationUser.setActive(true);
        applicationUser.setVersion(1);
        applicationUser.setFirstName("John");
        applicationUser.setLastName("Doe");
        UserProfile userProfile = new UserProfile();
        userProfile.setId(2L);
        userProfile.setUserType("TRADER");
        applicationUser.setUserProfile(userProfile);

        ApplicationUserDTO userDTO = new ApplicationUserDTO();

        userDTO.setId(2L);
        userDTO.setFirstName("John");
        userDTO.setLastName("Doe");
        userDTO.setActive(true);
        userDTO.setVersion(1);
        userDTO.setUserProfile("TRADER");

        when(userProfileService.getAllUserProfiles()).thenReturn(List.of(userProfile));
        when(applicationUserService.getAllUsers()).thenReturn(List.of(applicationUser));
        when(applicationUserMapper.toDto(any())).thenReturn(userDTO);
        when(applicationUserMapper.toEntity(any())).thenReturn(applicationUser);

    }

    @Test
    void shouldReturnAllUsers() throws Exception {
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk());
    }
    // Add more tests for POST, PUT, DELETE as needed
}
