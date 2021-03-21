package br.com.wk.controller;

import br.com.wk.model.Doador;
import br.com.wk.model.dto.*;
import br.com.wk.repository.DoadorRepository;
import br.com.wk.service.DoadorService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/doadores")
@AllArgsConstructor
@Slf4j
public class DoadorController {

    private final DoadorService service;

    @PostMapping("/upload")
    public ResponseEntity upload(@RequestBody List<DoadorRequest> request) {
        try {
            request.forEach(d -> service.save(d.toDoador()));
        } catch (Exception ex) {
            log.error("Um problema ocorreu ao persistir um doador", ex);
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/por-estado")
    public ResponseEntity<List<DoadorEstadoResponse>> porEstado() {
        try {
            return ResponseEntity.ok(service.porEstado());
        } catch (Exception ex) {
            log.error("Um problema ocorreu ao buscar dados por estado", ex);
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/por-faixa-etaria")
    public ResponseEntity<List<ImcMedioFaixaEtariaResponse>> porFaixaEtaria() {
        try {
            return ResponseEntity.ok(service.porFaixaEtaria());
        } catch (Exception ex) {
            log.error("Um problema ocorreu ao buscar dados por faixa etária", ex);
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/obesos/{sexo}")
    public ResponseEntity<Double> percentualObesos(@PathVariable String sexo) {
        try {
            return ResponseEntity.ok(service.percentualObesos(sexo));
        } catch (Exception ex) {
            log.error("Um problema ocorreu ao buscar dados percentuais de obesos", ex);
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/por-tipo-sanguineo")
    public ResponseEntity<List<IdadeMediaTipoSanguineo>> porTipoSanguineo() {
        try {
            return ResponseEntity.ok(service.porTipoSanguineo());
        } catch (Exception ex) {
            log.error("Um problema ocorreu ao buscar dados por tipo sanguineo", ex);
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/por-possivel-doador")
    public ResponseEntity<List<PossivelDoadorResponse>> porPossivelDoador() {
        try {
            return ResponseEntity.ok(service.porPossivelDoador());
        } catch (Exception ex) {
            log.error("Um problema ocorreu ao buscar dados por tipo sanguineo", ex);
            return ResponseEntity.badRequest().build();
        }
    }

}
