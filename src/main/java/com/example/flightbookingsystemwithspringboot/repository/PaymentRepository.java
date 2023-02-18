package com.example.flightbookingsystemwithspringboot.repository;

import com.example.flightbookingsystemwithspringboot.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
