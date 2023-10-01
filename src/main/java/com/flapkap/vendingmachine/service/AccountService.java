package com.flapkap.vendingmachine.service;

import com.flapkap.vendingmachine.model.Account;
import com.flapkap.vendingmachine.model.Product;
import com.flapkap.vendingmachine.model.User;
import com.flapkap.vendingmachine.dto.AccountDTO;
import com.flapkap.vendingmachine.dto.ProductDTO;
import com.flapkap.vendingmachine.mapper.AccountMapper;
import com.flapkap.vendingmachine.model.UserRole;
import com.flapkap.vendingmachine.repositoy.AccountRepository;
import com.flapkap.vendingmachine.exception.ErrorCode;
import com.flapkap.vendingmachine.exception.CustomException;
import com.flapkap.vendingmachine.repositoy.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Arrays;
import java.util.List;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductService productService;

    private static final List<Double> VALID_DENOMINATIONS = Arrays.asList(0.05, 0.10, 0.20, 0.50, 1.00);

    public AccountDTO addDepositForAuthenticatedUser(String username, Double amount) {
        //the else is not likely to occur as the user is already authenticated from the same source but no problem to do so.
        User user = validateUserExistence(username);


        if (!UserRole.BUYER.equals(user.getRole())) {
            throw new CustomException(ErrorCode.ACCESS_DENIED);
        }

        Account account = user.getAccount();
        if (account == null) {
            account = new Account(0.0, user);
        }

        if (!isValidDepositAmount(amount)) {
            throw new CustomException(ErrorCode.INVALID_INPUT);
        }

        Double newDeposit = account.getDeposit() + amount;
        account.setDeposit(newDeposit);
        accountRepository.save(account);
        return accountMapper.toDTO(account);
    }

    public AccountDTO buyProductForAuthenticatedUser(String username, Long productId, Integer quantity) {

        User user = validateUserExistence(username);

        Product productEntity = productService.findById(productId);
        double productCost = productEntity.getCost() * quantity;

        if (productEntity.getAmountAvailable() < quantity) {
            throw new CustomException(ErrorCode.OUT_OF_STOCK);
        }

        Account account = user.getAccount();
        if (account.getDeposit() < productCost) {
            throw new CustomException(ErrorCode.INSUFFICIENT_FUNDS);
        }

        double change = account.getDeposit() - productCost;
      //  account.setDeposit(0.0); // this could be changed to Once a product is bought, the deposit resets to 0 based on the business requirement
          account.setDeposit(change);
        accountRepository.save(account);
        productService.updateProduct(productEntity.getSeller().getId(), productId,
                new ProductDTO(productEntity.getProductName(), productEntity.getCost(), productEntity.getAmountAvailable() - quantity, productEntity.getSeller().getId()));

        return new AccountDTO(account.getDeposit(), user.getId());
    }

    public AccountDTO resetDepositForAuthenticatedUser(String username) {
        User user = validateUserExistence(username);
        Account account = user.getAccount();
        account.setDeposit(0.0);
        accountRepository.save(account);
        return accountMapper.toDTO(account);
    }

    private boolean isValidDepositAmount(Double amount) {
        return amount != null && VALID_DENOMINATIONS.contains(amount);
    }
    private User validateUserExistence(String username) {
      return  userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }
}
