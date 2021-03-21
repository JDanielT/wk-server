package br.com.wk.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Doador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private LocalDate nascimento;
    private String sexo;
    private double altura;
    private double peso;
    private String tipoSanguineo;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "filiacao_id", referencedColumnName = "id")
    private Filiacao filiacao;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "contato_id", referencedColumnName = "id")
    private Contato contato;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "endereco_id", referencedColumnName = "id")
    private Endereco endereco;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "documento_id", referencedColumnName = "id")
    private Documento documento;

    public Double getImc() {
        return this.peso / (altura * altura);
    }

    public int getIdade() {
        return LocalDate.now().getYear() - nascimento.getYear();
    }

    public boolean isDoadorApto(){
        return getIdade() >= 16 && getIdade() <= 69 && peso >= 50.0;
    }

}
