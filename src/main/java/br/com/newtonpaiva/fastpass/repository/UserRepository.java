package br.com.newtonpaiva.fastpass.repository;

import br.com.newtonpaiva.fastpass.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmailAndPassword(String email, String password);

    Optional<User> findByEmail(String username);

    Optional<User> findByCode(String code);

    Boolean existsByEmail(String email);
}
