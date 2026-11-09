package com.jcaa.usersmanagement.application.service.dto.query;

import jakarta.validation.constraints.NotBlank;

/** Consulta CQRS: obtener una emisora por su id. */
public record GetEmisoraByIdQuery(@NotBlank(message = "id must not be blank") String id) {}
