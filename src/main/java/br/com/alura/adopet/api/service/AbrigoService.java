package br.com.alura.adopet.api.service;

import br.com.alura.adopet.api.dto.CadastroAbrigoDTO;
import br.com.alura.adopet.api.model.Abrigo;
import br.com.alura.adopet.api.model.Pet;
import br.com.alura.adopet.api.repository.AbrigoRepository;
import br.com.alura.adopet.api.validacao.abrigo.ValidacaoCadastroAbrigo;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
public class AbrigoService {

    @Autowired
    private AbrigoRepository repository;

    @Autowired
    private List<ValidacaoCadastroAbrigo> validacoes;

    public List<Abrigo> buscarTodos() {
        return repository.findAll();
    }

    public void cadastrar(@Valid CadastroAbrigoDTO abrigo) {

        validacoes.forEach(v -> v.validar(abrigo));

        repository.save(
                Abrigo.builder()
                        .email(abrigo.email())
                        .nome(abrigo.nome())
                        .telefone(abrigo.telefone())
                        .build());
    }

    public List<Pet> listarPets(String idOuNome) {
        try {
            Long id = Long.parseLong(idOuNome);
            return repository.getReferenceById(id).getPets();
        } catch (EntityNotFoundException enfe) {
            return List.of();
        } catch (NumberFormatException e) {
            try {
                return repository.findByNome(idOuNome).getPets();
            } catch (EntityNotFoundException enfe) {
                return List.of();
            }
        }
    }

    public void cadastrarPet(@PathVariable String idOuNome, @RequestBody @Valid Pet pet) {
        try {
            Long id = Long.parseLong(idOuNome);
            Abrigo abrigo = repository.getReferenceById(id);
            pet.setAbrigo(abrigo);
            pet.setAdotado(false);
            abrigo.getPets().add(pet);
            repository.save(abrigo);
        } catch (NumberFormatException nfe) {

            Abrigo abrigo = repository.findByNome(idOuNome);
            pet.setAbrigo(abrigo);
            pet.setAdotado(false);
            abrigo.getPets().add(pet);
            repository.save(abrigo);


        }
    }

}
