package com.jcaa.usersmanagement.infrastructure.adapter.persistence.dto;

/** Datos de una emisora tal como se envían a la base de datos. */
public record EmisoraPersistenceDto(
    String id,
    String nombre,
    String canal,
    Double bandaFm,
    Integer bandaAm,
    int numLocutores,
    String genero,
    String horario,
    String patrocinador,
    String pais,
    String descripcion,
    int numProgramas,
    int numCiudades) {}
