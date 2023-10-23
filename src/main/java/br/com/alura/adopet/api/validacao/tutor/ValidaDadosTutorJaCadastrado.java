package br.com.alura.adopet.api.validacao.tutor;

import br.com.alura.adopet.api.dto.CadastroTutorDTO;
import br.com.alura.adopet.api.exception.ValidacaoException;
import br.com.alura.adopet.api.repository.TutorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ValidaDadosTutorJaCadastrado implements ValidacaoCadastroTutor {

    @Autowired
    TutorRepository tutorRepository;

    @Override
    public void validar(CadastroTutorDTO tutor) {
        if (tutorRepository.existsByEmailOrTelefone(tutor.email(), tutor.telefone())) {
            throw new ValidacaoException("Dados já cadastrados para outro tutor!");
        }

    }
}
