package br.com.smt.portal.servidor.folha_de_ponto.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@Table(name = "tb_folho_de_ponto")
public class FolhaDePonto implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @JoinColumn(name = "id")
    private Usuario usuario;

    @DateTimeFormat(pattern = "MM-yyyy")
    private Date mesReferencia;

    private Integer horasTrabalhadas;
    private Integer horasExtras;
    private Integer bancoHoras;
    private Integer compensacoes;

    private LocalDateTime geradoEm;

}
