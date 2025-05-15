package br.com.smt.portal.servidor.agenda.repository;

import br.com.smt.portal.servidor.agenda.model.Agenda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EventRepository extends JpaRepository<Agenda, UUID> {
    List<Agenda> findByUserId(String userId);
}
