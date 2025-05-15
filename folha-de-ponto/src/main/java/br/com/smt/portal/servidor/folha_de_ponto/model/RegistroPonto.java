package br.com.smt.portal.servidor.folha_de_ponto.model;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Data
@Table(name = "tb_registro_de_ponto")
public class RegistroPonto implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    private LocalDate data;
    private LocalTime horaEntrada;
    private LocalTime horaAlmocoInicio;
    private LocalTime horaAlmocoRetorno;
    private LocalTime horaSaida;

    @Enumerated(EnumType.STRING)
    private TipoRegistro tipo;

    private boolean atrasos;
    private boolean faltas;

    private String justificativa;

}
