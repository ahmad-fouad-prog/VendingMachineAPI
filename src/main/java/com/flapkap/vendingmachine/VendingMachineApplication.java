package com.flapkap.vendingmachine;

import com.flapkap.vendingmachine.model.Account;
import com.flapkap.vendingmachine.model.Product;
import com.flapkap.vendingmachine.model.User;
import com.flapkap.vendingmachine.model.UserRole;
import com.flapkap.vendingmachine.repositoy.AccountRepository;
import com.flapkap.vendingmachine.repositoy.ProductRepository;
import com.flapkap.vendingmachine.repositoy.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@SpringBootApplication
public class VendingMachineApplication {

	public static void main(String[] args) {
		SpringApplication.run(VendingMachineApplication.class, args);
	}
}
