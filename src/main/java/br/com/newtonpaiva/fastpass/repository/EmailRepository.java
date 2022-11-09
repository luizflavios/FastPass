package br.com.newtonpaiva.fastpass.repository;

import br.com.newtonpaiva.fastpass.model.Email;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailRepository extends JpaRepository<Email, Integer> {
}
