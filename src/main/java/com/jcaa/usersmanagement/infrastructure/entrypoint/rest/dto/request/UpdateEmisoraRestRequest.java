package com.jcaa.usersmanagement.infrastructure.entrypoint.rest.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

@Schema(description = "Datos para actualizar una emisora. El id va en la URL.")
public record UpdateEmisoraRestRequest(
    @Schema(example = "Radio Cartagena Stereo")
        @NotBlank(message = "nombre must not be blank")
        @Size(min = 3, max = 100, message = "nombre must have between 3 and 100 characters")
        String nombre,
    @Schema(example = "HJKC") @NotBlank(message = "canal must not be blank") String canal,
    @Schema(example = "95.5")
        @DecimalMin(value = "87.5", message = "bandaFm must be at least 87.5")
        @DecimalMax(value = "108.0", message = "bandaFm must be at most 108.0")
        Double bandaFm,
    @Schema(example = "1170")
        @Min(value = 530, message = "bandaAm must be at least 530")
        @Max(value = 1710, message = "bandaAm must be at most 1710")
        Integer bandaAm,
    @Schema(example = "8")
        @NotNull(message = "numLocutores is required")
        @Min(value = 1, message = "numLocutores must be at least 1")
        Integer numLocutores,
    @Schema(example = "VARIEDADES") @NotBlank(message = "genero must not be blank") String genero,
    @Schema(example = "05:00 - 23:00") @NotBlank(message = "horario must not be blank") String horario,
    @Schema(example = "Alcaldía de Cartagena") String patrocinador,
    @Schema(example = "Colombia") @NotBlank(message = "pais must not be blank") String pais,
    @Schema(example = "Programación variada") String descripcion,
    @Schema(example = "15")
        @NotNull(message = "numProgramas is required")
        @PositiveOrZero(message = "numProgramas must not be negative")
        Integer numProgramas,
    @Schema(example = "4")
        @NotNull(message = "numCiudades is required")
        @Min(value = 1, message = "numCiudades must be at least 1")
        Integer numCiudades) {}
