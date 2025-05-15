package br.com.smt.portal.servidor.folha_de_ponto.model;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Entity
@Data
@Table(name = "tb_justificativa")
public class Justificativa implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @JoinColumn(name = "id")
    private RegistroPonto registroPonto;

    private String texto;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID gestor_id;

    private Date dataResposta;

}
