package br.com.alura.adopet.api.validacao.adocao;

import br.com.alura.adopet.api.dto.SolicitacaoAdocaoDTO;
import br.com.alura.adopet.api.exception.ValidacaoException;
import br.com.alura.adopet.api.repository.AdocaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static br.com.alura.adopet.api.model.StatusAdocao.AGUARDANDO_AVALIACAO;

@Component
public class ValidaPetAdocaoEmAndamento implements ValidacaoSolicitacaoAdocao {

    @Autowired
    private AdocaoRepository adocaoRepository;

    public void validar(SolicitacaoAdocaoDTO solicitacaoAdocao) {
        if (adocaoRepository.existsByPetIdAndStatus(solicitacaoAdocao.idPet(), AGUARDANDO_AVALIACAO)) {
            throw new ValidacaoException("Pet já está aguardando avaliação para ser adotado!");
        }
    }

}
