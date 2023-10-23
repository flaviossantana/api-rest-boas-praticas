package br.com.alura.adopet.api.controller;

import br.com.alura.adopet.api.dto.CadastroTutorDTO;
import br.com.alura.adopet.api.service.TutorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tutores")
public class TutorController {

    @Autowired
    private TutorService tutorService;

    @PostMapping
    @Transactional
    public ResponseEntity<String> cadastrar(@RequestBody @Valid CadastroTutorDTO tutor) {
        this.tutorService.cadastrar(tutor);
        return ResponseEntity.ok().build();
    }

    @PutMapping
    @Transactional
    public ResponseEntity<String> atualizar(@RequestBody @Valid CadastroTutorDTO tutor) {
        this.tutorService.atualizar(tutor);
        return ResponseEntity.ok().build();
    }

}
