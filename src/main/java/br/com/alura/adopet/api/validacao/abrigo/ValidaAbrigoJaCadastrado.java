package br.com.alura.adopet.api.validacao.abrigo;

import br.com.alura.adopet.api.dto.CadastroAbrigoDTO;
import br.com.alura.adopet.api.exception.ValidacaoException;
import br.com.alura.adopet.api.repository.AbrigoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ValidaAbrigoJaCadastrado implements ValidacaoCadastroAbrigo {

    @Autowired
    private AbrigoRepository repository;

    @Override
    public void validar(CadastroAbrigoDTO abrigo) {
        if (repository.existsByNomeOrTelefoneOrEmail(abrigo.nome(), abrigo.telefone(), abrigo.email())) {
            throw new ValidacaoException("Dados já cadastrados para outro abrigo!");
        }
    }
}
