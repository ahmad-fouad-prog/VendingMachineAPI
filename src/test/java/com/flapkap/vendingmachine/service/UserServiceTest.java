package com.flapkap.vendingmachine.service;

import com.flapkap.vendingmachine.dto.UserDTO;
import com.flapkap.vendingmachine.exception.CustomException;
import com.flapkap.vendingmachine.exception.ErrorCode;
import com.flapkap.vendingmachine.mapper.AccountMapper;
import com.flapkap.vendingmachine.mapper.UserMapper;
import com.flapkap.vendingmachine.model.Product;
import com.flapkap.vendingmachine.model.User;
import com.flapkap.vendingmachine.model.UserRole;
import com.flapkap.vendingmachine.repositoy.AccountRepository;
import com.flapkap.vendingmachine.repositoy.ProductRepository;
import com.flapkap.vendingmachine.repositoy.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private AccountMapper accountMapper;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void createUser_UserAlreadyExists_ThrowsException() {
        UserDTO userDTO = new UserDTO(null, "fo2sh", "password", UserRole.BUYER, null);
        when(userRepository.findByUsername("fo2sh")).thenReturn(Optional.of(new User()));

        assertThrows(CustomException.class, () -> userService.createUser(userDTO));
        CustomException exception = assertThrows(CustomException.class, () -> userService.createUser(userDTO));
        assertEquals(ErrorCode.USER_ALREADY_EXIST, exception.getErrorCode());
    }

    @Test
    public void findById_UserDoesNotExist_ThrowsException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        CustomException exception =   assertThrows(CustomException.class, () -> userService.findById(1L));
        assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    public void updateUser_UserExists_UpdatesAndReturnsUser() {
        User existingUser = new User("fo2sh", "password", UserRole.BUYER);
        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.encode("new_password")).thenReturn("encoded_new_password");
        when(userRepository.save(any(User.class))).thenReturn(existingUser);
        when(userMapper.toDTO(existingUser)).thenReturn(new UserDTO(1L, "fo2sh", "new_password", UserRole.ADMIN, null));

        UserDTO resultDTO = userService.updateUser(1L, "new_password", UserRole.ADMIN);

        assertEquals("fo2sh", resultDTO.getUsername());
        assertEquals("new_password", resultDTO.getPassword());
        assertEquals(UserRole.ADMIN, resultDTO.getRole());
    }

    @Test
    public void deleteUser_UserDoesNotExist_ThrowsException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        CustomException exception =   assertThrows(CustomException.class, () -> userService.deleteUser(1L));
        assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    public void deleteUser_UserIsSellerAndHasProducts_ThrowsException() {
        User seller = new User("fo2sh_seller", "password", UserRole.SELLER);
        when(userRepository.findById(1L)).thenReturn(Optional.of(seller));
        when(productRepository.findBySeller(seller)).thenReturn(Collections.singletonList(new Product()));

        CustomException exception =    assertThrows(CustomException.class, () -> userService.deleteUser(1L));
        assertEquals(ErrorCode.SELLER_HAS_PRODUCTS, exception.getErrorCode());
    }


    @Test
    public void findByUsername_UserDoesNotExist_ThrowsException() {
        when(userRepository.findByUsername("non_existent_user")).thenReturn(Optional.empty());

        CustomException exception =      assertThrows(CustomException.class, () -> userService.findByUsername("non_existent_user"));
        assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    public void loadUserByUsername_UserExists_ReturnsUserDetails() {
        User user = new User("fo2sh", "password", UserRole.BUYER);
        when(userRepository.findByUsername("fo2sh")).thenReturn(Optional.of(user));

        UserDetails userDetails = userService.loadUserByUsername("fo2sh");

        assertEquals("fo2sh", userDetails.getUsername());
        assertEquals("password", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_BUYER")));
    }

    @Test
    public void loadUserByUsername_UserDoesNotExist_ThrowsUsernameNotFoundException() {
        when(userRepository.findByUsername("non_existent_user")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername("non_existent_user"));
    }


}
