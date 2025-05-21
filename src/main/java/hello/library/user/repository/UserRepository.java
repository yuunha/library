package hello.library.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import hello.library.user.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
