package com.jcaa.usersmanagement.application.service.dto.command;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

/** Comando CQRS: datos para actualizar una emisora existente (identificada por id). */
public record UpdateEmisoraCommand(
    @NotBlank(message = "id must not be blank") String id,
    @NotBlank(message = "nombre must not be blank")
        @Size(min = 3, message = "nombre must have at least 3 characters")
        String nombre,
    @NotBlank(message = "canal must not be blank") String canal,
    Double bandaFm,
    Integer bandaAm,
    @NotNull(message = "numLocutores is required")
        @Min(value = 1, message = "numLocutores must be at least 1")
        Integer numLocutores,
    @NotBlank(message = "genero must not be blank") String genero,
    @NotBlank(message = "horario must not be blank") String horario,
    String patrocinador,
    @NotBlank(message = "pais must not be blank") String pais,
    String descripcion,
    @NotNull(message = "numProgramas is required")
        @PositiveOrZero(message = "numProgramas must not be negative")
        Integer numProgramas,
    @NotNull(message = "numCiudades is required")
        @Min(value = 1, message = "numCiudades must be at least 1")
        Integer numCiudades) {}
