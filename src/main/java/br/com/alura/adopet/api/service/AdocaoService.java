package br.com.alura.adopet.api.service;

import br.com.alura.adopet.api.dto.AprovacaoAdocaoDTO;
import br.com.alura.adopet.api.dto.ReprovacaoAdocaoDTO;
import br.com.alura.adopet.api.dto.SolicitacaoAdocaoDTO;
import br.com.alura.adopet.api.exception.ValidacaoException;
import br.com.alura.adopet.api.model.Adocao;
import br.com.alura.adopet.api.model.Pet;
import br.com.alura.adopet.api.model.StatusAdocao;
import br.com.alura.adopet.api.model.Tutor;
import br.com.alura.adopet.api.repository.AdocaoRepository;
import br.com.alura.adopet.api.repository.PetRepository;
import br.com.alura.adopet.api.repository.TutorRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static br.com.alura.adopet.api.model.StatusAdocao.AGUARDANDO_AVALIACAO;

@Service
public class AdocaoService {

    @Autowired
    private AdocaoRepository adocaoRepository;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private TutorRepository tutorRepository;

    @Autowired
    private EmailService emailService;

    public void solicitar(@Valid SolicitacaoAdocaoDTO solicitacaoAdocao) {

        Pet pet = petRepository.getReferenceById(solicitacaoAdocao.idPet());
        Tutor tutor = tutorRepository.getReferenceById(solicitacaoAdocao.idTutor());

        if (pet.getAdotado()) {
            throw new ValidacaoException("Pet já foi adotado!");
        } else {
            List<Adocao> adocoes = adocaoRepository.findAll();
            for (Adocao a : adocoes) {
                if (a.getTutor() == tutor && a.getStatus() == AGUARDANDO_AVALIACAO) {
                    throw new ValidacaoException("Tutor já possui outra adoção aguardando avaliação!");
                }
            }
            for (Adocao a : adocoes) {
                if (a.getPet() == pet && a.getStatus() == AGUARDANDO_AVALIACAO) {
                    throw new ValidacaoException("Pet já está aguardando avaliação para ser adotado!");
                }
            }
            for (Adocao a : adocoes) {
                int contador = 0;
                if (a.getTutor() == tutor && a.getStatus() == StatusAdocao.APROVADO) {
                    contador = contador + 1;
                }
                if (contador == 5) {
                    throw new ValidacaoException("Tutor chegou ao limite máximo de 5 adoções!");
                }
            }
        }

        adocaoRepository.save(
                Adocao.builder()
                        .data(LocalDateTime.now())
                        .status(AGUARDANDO_AVALIACAO)
                        .tutor(tutor)
                        .pet(pet)
                        .motivo(solicitacaoAdocao.motivo())
                        .build()
        );

        emailService.enviar(
                pet.getAbrigo().getEmail(),
                "Solicitação de adoção",
                String.format(
                        "Olá %s !%n%nUma solicitação de adoção foi registrada hoje para o pet: %s. %nFavor avaliar para aprovação ou reprovação.",
                        pet.getAbrigo().getNome(),
                        pet.getNome())
        );
    }

    public void aprovar(@Valid AprovacaoAdocaoDTO aprovacaoAdocao) {

        Adocao adocao = adocaoRepository.getReferenceById(aprovacaoAdocao.idAdocao());
        adocao.setStatus(StatusAdocao.APROVADO);

        emailService.enviar(
                adocao.getTutor().getEmail(),
                "Adoção aprovada",
                "Parabéns " + adocao.getTutor().getNome() + "!\n\nSua adoção do pet " + adocao.getPet().getNome() + ", solicitada em " + adocao.getData().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")) + ", foi aprovada.\nFavor entrar em contato com o abrigo " + adocao.getPet().getAbrigo().getNome() + " para agendar a busca do seu pet."
        );
    }

    public void reprovar(@Valid ReprovacaoAdocaoDTO reprovacaoAdocao) {

        Adocao adocao = adocaoRepository.getReferenceById(reprovacaoAdocao.idAdocao());
        adocao.setStatus(StatusAdocao.REPROVADO);
        adocao.setMotivo(reprovacaoAdocao.justificativa());

        emailService.enviar(
                adocao.getTutor().getEmail(),
                "Adoção reprovada",
                "Olá " + adocao.getTutor().getNome() + "!\n\nInfelizmente sua adoção do pet " + adocao.getPet().getNome() + ", solicitada em " + adocao.getData().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")) + ", foi reprovada pelo abrigo " + adocao.getPet().getAbrigo().getNome() + " com a seguinte justificativa: " + adocao.getJustificativaStatus()
        );
    }
}
