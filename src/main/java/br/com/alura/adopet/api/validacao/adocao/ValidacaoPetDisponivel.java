package br.com.alura.adopet.api.validacao.adocao;

import br.com.alura.adopet.api.dto.SolicitacaoAdocaoDTO;
import br.com.alura.adopet.api.exception.ValidacaoException;
import br.com.alura.adopet.api.model.Pet;
import br.com.alura.adopet.api.repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidacaoPetDisponivel implements ValidacaoSolicitacaoAdocao {

    @Autowired
    private PetRepository petRepository;

    public void validar(SolicitacaoAdocaoDTO solicitacaoAdocao){
        Pet pet = petRepository.getReferenceById(solicitacaoAdocao.idPet());
        if (Boolean.TRUE.equals(pet.getAdotado())) {
            throw new ValidacaoException("Pet já foi adotado!");
        }
    }

}
