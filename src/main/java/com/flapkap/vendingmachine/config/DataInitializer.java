package com.flapkap.vendingmachine.config;

import com.flapkap.vendingmachine.model.Account;
import com.flapkap.vendingmachine.model.Product;
import com.flapkap.vendingmachine.model.User;
import com.flapkap.vendingmachine.model.UserRole;
import com.flapkap.vendingmachine.repositoy.AccountRepository;
import com.flapkap.vendingmachine.repositoy.ProductRepository;
import com.flapkap.vendingmachine.repositoy.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Configuration
public class DataInitializer {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner initData(UserRepository userRepository,
                                      AccountRepository accountRepository,
                                      ProductRepository productRepository) {
        return args -> {
            // Encoding the passwords
            String encodedSellerPassword = passwordEncoder.encode("s123");
            String encodedBuyerPassword = passwordEncoder.encode("b123");
            String encodedAdminPassword = passwordEncoder.encode("admin");

            // Inserting sample users with roles
            User seller = new User("seller", encodedSellerPassword, UserRole.SELLER);
            User buyer = new User("buyer", encodedBuyerPassword, UserRole.BUYER);
            User admin = new User("admin", encodedAdminPassword, UserRole.ADMIN);

            userRepository.saveAll(List.of(seller, buyer, admin));

            // Inserting sample accounts
            Account sellerAccount = new Account(0.0, seller);
            Account buyerAccount = new Account(100.0, buyer);
            Account adminAccount = new Account(0.0, admin);

            accountRepository.saveAll(List.of(sellerAccount, buyerAccount, adminAccount));

            // Inserting sample products
            Product coke = new Product("Coke", 0.75, 10.0, seller);
            Product pepsi = new Product("Pepsi", 1.00, 5.0, seller);
            Product chips = new Product("Chips", 0.50, 15.0, seller);

            productRepository.saveAll(List.of(coke, pepsi, chips));
        };
    }
}
