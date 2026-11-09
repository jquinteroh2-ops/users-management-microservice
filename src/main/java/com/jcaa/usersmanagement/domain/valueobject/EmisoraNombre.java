package com.jcaa.usersmanagement.domain.valueobject;

import com.jcaa.usersmanagement.domain.exception.InvalidEmisoraException;
import java.util.Objects;

/** Nombre comercial de la emisora. Mínimo 3 caracteres, sin espacios sobrantes. */
public record EmisoraNombre(String value) {

  private static final int MINIMUM_LENGTH = 3;

  public EmisoraNombre {
    final String normalizedValue =
        Objects.requireNonNull(value, "EmisoraNombre cannot be null").trim().replaceAll("\\s+", " ");
    if (normalizedValue.isEmpty()) {
      throw InvalidEmisoraException.becauseNombreIsEmpty();
    }
    if (normalizedValue.length() < MINIMUM_LENGTH) {
      throw InvalidEmisoraException.becauseNombreIsTooShort(MINIMUM_LENGTH);
    }
    value = normalizedValue;
  }

  @Override
  public String toString() {
    return value;
  }
}
