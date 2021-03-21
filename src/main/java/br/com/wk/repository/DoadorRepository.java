package br.com.wk.repository;

import br.com.wk.model.Doador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoadorRepository extends JpaRepository<Doador, Long> {

    Doador findByDocumentoCpf(String cpf);
    List<Doador> findBySexo(String sexo);

}
