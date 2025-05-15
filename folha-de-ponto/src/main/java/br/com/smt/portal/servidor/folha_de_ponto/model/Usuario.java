package br.com.smt.portal.servidor.folha_de_ponto.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Entity
@Data
@Table(name = "tb_usuarios")
public class Usuario implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String nome;
    @Email
    private String email;
    private String senha;
    private String cargo;
    private String departamento;

    @Enumerated(EnumType.STRING)
    private Perfil perfil;

    private LocalDateTime dataCriacao = LocalDateTime.now();

}
