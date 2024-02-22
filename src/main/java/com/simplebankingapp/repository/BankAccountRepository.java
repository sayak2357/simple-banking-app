package com.simplebankingapp.repository;

import com.simplebankingapp.entity.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankAccountRepository extends JpaRepository<BankAccount,Long> {
}
