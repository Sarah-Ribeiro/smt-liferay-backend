package br.com.smt.portal.servidor.ferias.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "tb_ferias")
public class Ferias {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String author;
    private LocalDateTime start;
    private LocalDateTime end;

    @Column(nullable = false)
    private String userId;

}

