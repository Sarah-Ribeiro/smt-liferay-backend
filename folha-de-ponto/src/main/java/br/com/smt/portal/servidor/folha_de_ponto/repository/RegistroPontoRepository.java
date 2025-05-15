package br.com.smt.portal.servidor.folha_de_ponto.repository;

import br.com.smt.portal.servidor.folha_de_ponto.model.RegistroPonto;
import br.com.smt.portal.servidor.folha_de_ponto.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RegistroPontoRepository extends JpaRepository<RegistroPonto, UUID> {
    List<RegistroPonto> findByUsuarioIdAndDataHoraBetween(UUID usuarioId, LocalDateTime inicio, LocalDateTime fim);

    List<RegistroPonto> findByUsuarioId(UUID usuarioId);

    Optional<RegistroPonto> findByUsuarioAndData(Usuario usuario, LocalDate hoje);

}

