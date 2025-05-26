package hello.library.loan.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hello.library.loan.dto.LoanRequest;
import hello.library.loan.service.LoanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/loan")
@RequiredArgsConstructor
public class LoanController {

    private final LoanService loanService;

    @PostMapping
    public ResponseEntity<Void> registerLoan(@Valid @RequestBody LoanRequest request) {
        loanService.registerLoan(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
