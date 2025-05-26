package hello.library.loan.service;

import org.springframework.stereotype.Service;

import hello.library.loan.dto.LoanRequest;
import hello.library.loan.repository.LoanRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LoanService {
    private final LoanRepository loanRepository;

    public void registerLoan(LoanRequest request) {
    }
}
