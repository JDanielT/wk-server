package br.com.wk.service;

import br.com.wk.model.Doador;
import br.com.wk.model.dto.DoadorEstadoResponse;
import br.com.wk.model.dto.IdadeMediaTipoSanguineo;
import br.com.wk.model.dto.ImcMedioFaixaEtariaResponse;
import br.com.wk.model.dto.PossivelDoadorResponse;
import br.com.wk.repository.DoadorRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DoadorService {

    private final DoadorRepository repository;
    private List<Map<String, List<String>>> map;

    @PostConstruct
    public void init() {
        map = new ArrayList<>();
        map.add(configurarDoadores("A+", new String[]{"A+", "A-", "O+", "O-"}));
        map.add(configurarDoadores("A-", new String[]{"A-", "O-"}));
        map.add(configurarDoadores("B+", new String[]{"B+", "B-", "O+", "O-"}));
        map.add(configurarDoadores("B-", new String[]{"B-", "O-"}));
        map.add(configurarDoadores("AB+", new String[]{"A+", "B+", "O+", "AB+", "A-", "B-", "O-", "AB-"}));
        map.add(configurarDoadores("AB-", new String[]{"A-", "B-", "O-", "AB-"}));
        map.add(configurarDoadores("O+", new String[]{"O+", "O-"}));
        map.add(configurarDoadores("O-", new String[]{"O-"}));
    }

    private Map<String, List<String>> configurarDoadores(String tipo, String... receptores) {
        Map<String, List<String>> map = new HashMap<>();
        map.put(tipo, new ArrayList<>(Arrays.asList(receptores)));
        return map;
    }

    public Doador findByCpf(String cpf) {
        return repository.findByDocumentoCpf(cpf);
    }

    public Doador save(Doador doador) {
        Doador persist = this.findByCpf(doador.getDocumento().getCpf());
        if (persist == null) {
            persist = doador;
        } else {
            Long id = persist.getId();
            Long filiacaoId = persist.getFiliacao().getId();
            Long documentoId = persist.getDocumento().getId();
            Long enderecoId = persist.getEndereco().getId();
            persist = doador;
            persist.setId(id);
            persist.getFiliacao().setId(filiacaoId);
            persist.getDocumento().setId(documentoId);
            persist.getEndereco().setId(enderecoId);
        }
        return repository.save(persist);
    }

    public List<DoadorEstadoResponse> porEstado() {
        List<Doador> doadores = repository.findAll();
        return doadores.stream()
                .collect(Collectors.groupingBy(d -> d.getEndereco().getEstado(), Collectors.counting()))
                .entrySet()
                .stream()
                .map(entry -> DoadorEstadoResponse.builder()
                        .estado(entry.getKey())
                        .quantidade(entry.getValue()).build())
                .collect(Collectors.toList());
    }

    public List<ImcMedioFaixaEtariaResponse> porFaixaEtaria() {
        List<Doador> doadores = repository.findAll();
        return doadores.stream()
                .collect(Collectors.groupingBy(d -> Math.ceil((d.getIdade()) / 10.0) * 10, Collectors.counting()))
                .entrySet()
                .stream()
                .map(entry -> ImcMedioFaixaEtariaResponse.builder()
                        .ateIdade(entry.getKey())
                        .quantidade(entry.getValue()).build())
                .collect(Collectors.toList());
    }

    public Double percentualObesos(String sexo) {
        List<Doador> doadores = repository.findBySexo(sexo);
        long obesos = doadores.stream()
                .filter(d -> d.getImc() > 30)
                .count();
        return (Double.valueOf(obesos) * 100) / doadores.size();
    }

    public List<IdadeMediaTipoSanguineo> porTipoSanguineo() {
        List<Doador> doadores = repository.findAll();
        return doadores.stream()
                .collect(Collectors.groupingBy(d -> d.getTipoSanguineo(), Collectors.counting()))
                .entrySet()
                .stream()
                .map(entry -> IdadeMediaTipoSanguineo.builder()
                        .tipoSanguineo(entry.getKey())
                        .quantidade(entry.getValue()).build())
                .collect(Collectors.toList());
    }

    public List<PossivelDoadorResponse> porPossivelDoador() {
        List<Doador> doadores = repository.findAll()
                .stream()
                .filter(Doador::isDoadorApto)
                .collect(Collectors.toList());
        return doadores.stream()
                .collect(Collectors.groupingBy(d -> d.getTipoSanguineo(),
                        Collectors.summarizingLong(d -> this.countPossiveisDoadores(d.getTipoSanguineo(), doadores))))
                .entrySet()
                .stream()
                .map(entry -> PossivelDoadorResponse.builder()
                        .tipoSanguineo(entry.getKey())
                        .quantidade(entry.getValue().getCount()).build())
                .collect(Collectors.toList());
    }

    public long countPossiveisDoadores(String receptor, List<Doador> doadores) {
        return doadores.stream()
                .filter(d -> validateDoacao(receptor, d.getTipoSanguineo()))
                .count();
    }

    private boolean validateDoacao(String receptor, String doador) {
        var resultado = map.stream()
                .filter(item -> item.containsKey(receptor))
                .findFirst()
                .get().get(receptor).contains(doador);
        return resultado;
    }

}
