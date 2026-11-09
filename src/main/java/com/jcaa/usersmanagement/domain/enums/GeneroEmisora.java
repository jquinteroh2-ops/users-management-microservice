package com.jcaa.usersmanagement.domain.enums;

import com.jcaa.usersmanagement.domain.exception.InvalidEmisoraException;

/** Género o formato principal de la programación de una emisora. */
public enum GeneroEmisora {
  NOTICIAS,
  MUSICAL,
  DEPORTIVA,
  CULTURAL,
  RELIGIOSA,
  EDUCATIVA,
  COMUNITARIA,
  VARIEDADES;

  public static GeneroEmisora fromString(final String value) {
    final String normalized = value == null ? "" : value.trim();
    for (final GeneroEmisora genero : values()) {
      if (genero.name().equalsIgnoreCase(normalized)) {
        return genero;
      }
    }
    throw InvalidEmisoraException.becauseGeneroIsInvalid(value);
  }
}
