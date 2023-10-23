package br.com.alura.adopet.api.dto;

import br.com.alura.adopet.api.model.Pet;
import br.com.alura.adopet.api.model.TipoPet;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PetDTO(
        Long id,
        @NotNull
        TipoPet tipoPet,
        @NotBlank
        String nome,
        @NotBlank
        String raca,
        @NotNull
        Integer idade) {

    public PetDTO(Pet pet) {
        this(
            pet.getId(),
            pet.getTipo(),
            pet.getNome(),
            pet.getRaca(),
            pet.getIdade()
        );
    }
}
