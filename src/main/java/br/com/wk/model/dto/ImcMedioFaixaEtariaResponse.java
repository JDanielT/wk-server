package br.com.wk.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ImcMedioFaixaEtariaResponse {

    private Double ateIdade;
    private Long quantidade;

}
