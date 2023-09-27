package br.com.alura.adopet.api.controller;

import br.com.alura.adopet.api.model.Adocao;
import br.com.alura.adopet.api.service.AdocaoService;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/adocoes")
public class AdocaoController {

    @Autowired
    private AdocaoService adocaoService;

    @PostMapping
    @Transactional
    public ResponseEntity<String> solicitar(@RequestBody @Valid Adocao adocao) {
        try {
            adocaoService.solicitar(adocao);
            return ResponseEntity.ok("Adoção solicitada com sucesso!");
        } catch (ValidationException ve) {
            return ResponseEntity.badRequest().body(ve.getMessage());
        }
    }

    @PutMapping("/aprovar")
    @Transactional
    public ResponseEntity<String> aprovar(@RequestBody @Valid Adocao adocao) {
        adocaoService.aprovar(adocao);
        return ResponseEntity.ok("Adoção aprovada com sucesso!");
    }

    @PutMapping("/reprovar")
    @Transactional
    public ResponseEntity<String> reprovar(@RequestBody @Valid Adocao adocao) {
        adocaoService.reprovar(adocao);
        return ResponseEntity.ok("Adoção foi reprovada!");
    }

}
