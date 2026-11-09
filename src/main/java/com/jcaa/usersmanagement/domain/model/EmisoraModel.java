package com.jcaa.usersmanagement.domain.model;

import com.jcaa.usersmanagement.domain.enums.GeneroEmisora;
import com.jcaa.usersmanagement.domain.exception.InvalidEmisoraException;
import com.jcaa.usersmanagement.domain.valueobject.BandaAm;
import com.jcaa.usersmanagement.domain.valueobject.BandaFm;
import com.jcaa.usersmanagement.domain.valueobject.EmisoraId;
import com.jcaa.usersmanagement.domain.valueobject.EmisoraNombre;
import lombok.Value;

/**
 * Entidad de dominio Emisora (ejercicio 25).
 *
 * <p>Es inmutable. Las reglas de un solo atributo viven en sus Value Objects (EmisoraNombre,
 * BandaFm, BandaAm); las reglas que dependen de varios atributos, como "debe transmitir al menos
 * en FM o en AM", se validan aquí en el constructor.
 */
@Value
public class EmisoraModel {

  EmisoraId id;
  EmisoraNombre nombre;
  String canal;
  BandaFm bandaFm;
  BandaAm bandaAm;
  int numLocutores;
  GeneroEmisora genero;
  String horario;
  String patrocinador;
  String pais;
  String descripcion;
  int numProgramas;
  int numCiudades;

  @SuppressWarnings("java:S107") // La entidad del ejercicio tiene 12 atributos de negocio.
  public EmisoraModel(
      final EmisoraId id,
      final EmisoraNombre nombre,
      final String canal,
      final BandaFm bandaFm,
      final BandaAm bandaAm,
      final int numLocutores,
      final GeneroEmisora genero,
      final String horario,
      final String patrocinador,
      final String pais,
      final String descripcion,
      final int numProgramas,
      final int numCiudades) {
    if (id == null) {
      throw InvalidEmisoraException.becauseFieldIsRequired("id");
    }
    if (nombre == null) {
      throw InvalidEmisoraException.becauseFieldIsRequired("nombre");
    }
    if (bandaFm == null && bandaAm == null) {
      throw InvalidEmisoraException.becauseHasNoBand();
    }
    if (genero == null) {
      throw InvalidEmisoraException.becauseFieldIsRequired("genero");
    }
    this.id = id;
    this.nombre = nombre;
    this.canal = required(canal, "canal");
    this.bandaFm = bandaFm;
    this.bandaAm = bandaAm;
    this.numLocutores = atLeast(numLocutores, 1, "numLocutores");
    this.genero = genero;
    this.horario = required(horario, "horario");
    this.patrocinador = optional(patrocinador);
    this.pais = required(pais, "pais");
    this.descripcion = optional(descripcion);
    this.numProgramas = notNegative(numProgramas, "numProgramas");
    this.numCiudades = atLeast(numCiudades, 1, "numCiudades");
  }

  public boolean transmiteEnFm() {
    return bandaFm != null;
  }

  public boolean transmiteEnAm() {
    return bandaAm != null;
  }

  private static String required(final String value, final String field) {
    if (value == null || value.isBlank()) {
      throw InvalidEmisoraException.becauseFieldIsRequired(field);
    }
    return value.trim();
  }

  private static String optional(final String value) {
    return value == null ? "" : value.trim();
  }

  private static int notNegative(final int value, final String field) {
    if (value < 0) {
      throw InvalidEmisoraException.becauseValueIsNegative(field);
    }
    return value;
  }

  private static int atLeast(final int value, final int minimum, final String field) {
    if (value < minimum) {
      throw InvalidEmisoraException.becauseValueIsBelowMinimum(field, minimum);
    }
    return value;
  }
}
