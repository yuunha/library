package hello.library.loan.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import hello.library.loan.entity.Loan;

public interface LoanRepository extends JpaRepository<Loan, Long> {

}
