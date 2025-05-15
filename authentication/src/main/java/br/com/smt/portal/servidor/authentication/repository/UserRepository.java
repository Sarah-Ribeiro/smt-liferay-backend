package br.com.smt.portal.servidor.authentication.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.smt.portal.servidor.authentication.entity.User;

import javax.swing.text.html.Option;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByUtilizer(String utilizer);

    boolean existsByMatricula(String matriculaGerada);

    Optional<User> findUserByMatricula(String matricula);

    Optional<User> findByCpf(String cpf);

    Optional<User> findByMatricula(String matricula);

    Optional<User> findByEmail(String email);
}
