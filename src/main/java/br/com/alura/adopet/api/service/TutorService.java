package br.com.alura.adopet.api.service;

import br.com.alura.adopet.api.dto.CadastroTutorDTO;
import br.com.alura.adopet.api.model.Tutor;
import br.com.alura.adopet.api.repository.TutorRepository;
import br.com.alura.adopet.api.validacao.tutor.ValidacaoCadastroTutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TutorService {

    @Autowired
    private TutorRepository tutorRepository;

    @Autowired
    private List<ValidacaoCadastroTutor> validacoes;

    public void cadastrar(CadastroTutorDTO tutor) {

        validacoes.forEach(validacao -> validacao.validar(tutor));

        tutorRepository.save(Tutor.builder()
                .email(tutor.email())
                .telefone(tutor.telefone())
                .nome(tutor.nome())
                .build());
    }

    public void atualizar(CadastroTutorDTO tutor) {

        Tutor tutorCadastrado = tutorRepository.getReferenceById(tutor.id());

        tutorCadastrado.setNome(tutor.nome());
        tutorCadastrado.setTelefone(tutor.telefone());
        tutorCadastrado.setEmail(tutor.email());

        tutorRepository.save(tutorCadastrado);
    }
}
