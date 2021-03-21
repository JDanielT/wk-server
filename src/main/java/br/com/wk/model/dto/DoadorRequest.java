package br.com.wk.model.dto;

import br.com.wk.model.*;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class DoadorRequest {

    private String nome;
    private String cpf;
    private String rg;
    private String dataNasc;
    private String sexo;
    private String mae;
    private String pai;
    private String email;
    private String cep;
    private String endereco;
    private String numero;
    private String bairro;
    private String cidade;
    private String estado;
    private String telefoneFixo;
    private String celular;
    private double altura;
    private double peso;
    private String tipoSanguineo;

    public Doador toDoador() {
        return Doador.builder()
                .nome(nome)
                .documento(Documento.builder().cpf(cpf).rg(rg).build())
                .filiacao(Filiacao.builder().mae(mae).pai(pai).build())
                .contato(Contato.builder().email(email).celular(celular).telefoneFixo(telefoneFixo).build())
                .endereco(Endereco.builder().cep(cep)
                        .endereco(endereco)
                        .numero(numero)
                        .bairro(bairro)
                        .cidade(cidade)
                        .estado(estado)
                        .build())
                .nascimento(LocalDate.parse(dataNasc, DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                .sexo(sexo)
                .altura(altura)
                .peso(peso)
                .tipoSanguineo(tipoSanguineo).build();
    }

}
