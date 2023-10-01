package com.flapkap.vendingmachine.repositoy;

import com.flapkap.vendingmachine.model.Account;
import com.flapkap.vendingmachine.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface AccountRepository  extends JpaRepository<Account,Long> {
    Optional<Account> findByUser(User user);
}
