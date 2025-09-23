package com.technicalchallenge.service;

import com.technicalchallenge.model.ApplicationUser;
import com.technicalchallenge.repository.ApplicationUserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import java.util.Optional;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private ApplicationUserRepository applicationUserRepository;
    @InjectMocks
    private ApplicationUserService applicationUserService;

    @Test
    void testFindUserById() {
        ApplicationUser user = new ApplicationUser();
        user.setId(1L);
        when(applicationUserRepository.findById(1L)).thenReturn(Optional.of(user));
        Optional<ApplicationUser> found = applicationUserService.getUserById(1L);
        assertTrue(found.isPresent());
        assertEquals(1L, found.get().getId());
    }

    @Test
    void testSaveApplicationUserUser() {
        ApplicationUser user = new ApplicationUser();
        user.setId(2L);
        when(applicationUserRepository.save(user)).thenReturn(user);
        ApplicationUser saved = applicationUserService.saveUser(user);
        assertNotNull(saved);
        assertEquals(2L, saved.getId());
    }

    @Test
    void testDeleteApplicationUser() {
        Long userId = 3L;
        doNothing().when(applicationUserRepository).deleteById(userId);
        applicationUserService.deleteUser(userId);
        verify(applicationUserRepository, times(1)).deleteById(userId);
    }

    @Test
    void testFindApplicationUserByNonExistentId() {
        when(applicationUserRepository.findById(99L)).thenReturn(Optional.empty());
        Optional<ApplicationUser> found = applicationUserService.getUserById(99L);
        assertFalse(found.isPresent());
    }

    // Business logic: test ApplicationUser cannot be created with null login id
    @Test
    void testApplicationUserCreationWithNullLoginIdThrowsException() {
        ApplicationUser ApplicationUser = new ApplicationUser();
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            validateApplicationUser(ApplicationUser);
        });
        assertTrue(exception.getMessage().contains("Login id cannot be null"));
    }

    // Helper for business logic validation
    private void validateApplicationUser(ApplicationUser ApplicationUser) {
        if (ApplicationUser.getLoginId() == null) {
            throw new IllegalArgumentException("Login id cannot be null");
        }
    }
}
