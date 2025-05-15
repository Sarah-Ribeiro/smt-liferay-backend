package br.com.smt.portal.servidor.ferias.repository;

import br.com.smt.portal.servidor.ferias.model.Ferias;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FeriasRepository extends JpaRepository<Ferias, UUID> {
    List<Ferias> findByUserId(String userId);
}
