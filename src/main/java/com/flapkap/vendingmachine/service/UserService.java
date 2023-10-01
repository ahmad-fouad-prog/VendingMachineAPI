package com.flapkap.vendingmachine.service;

import com.flapkap.vendingmachine.dto.UserDTO;
import com.flapkap.vendingmachine.exception.CustomException;
import com.flapkap.vendingmachine.mapper.AccountMapper;
import com.flapkap.vendingmachine.mapper.UserMapper;
import com.flapkap.vendingmachine.model.Account;
import com.flapkap.vendingmachine.model.Product;
import com.flapkap.vendingmachine.model.User;
import com.flapkap.vendingmachine.model.UserRole;
import com.flapkap.vendingmachine.exception.ErrorCode;
import com.flapkap.vendingmachine.repositoy.AccountRepository;
import com.flapkap.vendingmachine.repositoy.ProductRepository;
import com.flapkap.vendingmachine.repositoy.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public UserDTO createUser(UserDTO userDTO) {
        Optional<User> existingUser = userRepository.findByUsername(userDTO.getUsername());
        if (existingUser.isPresent()) {
            throw new CustomException(ErrorCode.USER_ALREADY_EXIST);
        }

        User user = userMapper.toEntity(userDTO);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user = userRepository.save(user);

        Account account;
        if (userDTO.getAccount() != null && userDTO.getAccount().getDeposit()>=0) {
            account = accountMapper.toEntity(userDTO.getAccount());
        } else {
            account = new Account(0.0, user);
        }
        account.setUser(user);
        accountRepository.save(account);

        user.setAccount(account);

        return userMapper.toDTO(user);
    }

    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map(userMapper::toDTO).collect(Collectors.toList());
    }

    public UserDTO findById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND));
        return userMapper.toDTO(user);
    }

    public User findByIdEntity(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND));
    }



    public UserDTO updateUser(Long id, String password, UserRole role) {
        User user = userRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND));
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role);
        User updatedUser = userRepository.save(user);
        return userMapper.toDTO(updatedUser);
    }
    public void deleteUser(Long id) {
        Optional<User> userOPT = userRepository.findById(id);

        if (userOPT.isEmpty()) {
            throw new CustomException(ErrorCode.RESOURCE_NOT_FOUND);
        }

        User user = userOPT.get();
        List<Product> userProducts = productRepository.findBySeller(user);

        if (!userProducts.isEmpty()) {
            throw new CustomException(ErrorCode.SELLER_HAS_PRODUCTS);
        }

        userRepository.deleteById(id);
    }

    public void verifySellerOrAdminRole(Long userId) {
        UserDTO user = this.findById(userId);
        if (user == null || (!user.getRole().equals(UserRole.SELLER) && !user.getRole().equals(UserRole.ADMIN))) {
            throw new CustomException(ErrorCode.ACCESS_DENIED);
        }
    }
    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                getAuthorities(user)
        );
    }

    private Collection<? extends GrantedAuthority> getAuthorities(User user) {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));
    }
}
