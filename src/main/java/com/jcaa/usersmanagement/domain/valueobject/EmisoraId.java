package com.jcaa.usersmanagement.domain.valueobject;

import com.jcaa.usersmanagement.domain.exception.InvalidEmisoraException;
import java.util.Objects;

/** Identificador único de una emisora (por ejemplo "emi-001"). */
public record EmisoraId(String value) {

  public EmisoraId {
    final String normalizedValue = Objects.requireNonNull(value, "EmisoraId cannot be null").trim();
    if (normalizedValue.isEmpty()) {
      throw InvalidEmisoraException.becauseIdIsEmpty();
    }
    value = normalizedValue;
  }

  @Override
  public String toString() {
    return value;
  }
}
