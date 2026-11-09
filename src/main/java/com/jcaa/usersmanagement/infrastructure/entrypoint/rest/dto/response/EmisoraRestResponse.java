package com.jcaa.usersmanagement.infrastructure.entrypoint.rest.dto.response;

public record EmisoraRestResponse(
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
