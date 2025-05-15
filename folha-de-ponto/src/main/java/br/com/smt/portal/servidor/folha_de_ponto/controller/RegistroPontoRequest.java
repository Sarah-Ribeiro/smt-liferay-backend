package br.com.smt.portal.servidor.folha_de_ponto.controller;

import br.com.smt.portal.servidor.folha_de_ponto.model.TipoRegistro;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegistroPontoRequest {

    private TipoRegistro tipo;
    private String justificativa;

}
