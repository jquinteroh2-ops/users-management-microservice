package com.jcaa.usersmanagement.infrastructure.adapter.persistence.entity;

/** Fila de la tabla "emisoras" leída desde MySQL. */
public record EmisoraEntity(
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
    int numCiudades,
    String createdAt,
    String updatedAt) {}
