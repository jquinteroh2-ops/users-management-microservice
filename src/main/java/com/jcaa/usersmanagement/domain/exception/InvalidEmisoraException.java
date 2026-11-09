package com.jcaa.usersmanagement.domain.exception;

import java.util.Locale;

/** Se lanza cuando un dato de la emisora no cumple las reglas del dominio. */
public final class InvalidEmisoraException extends DomainException {

  private static final String MESSAGE_ID_EMPTY = "The emisora id must not be empty.";
  private static final String MESSAGE_NOMBRE_EMPTY = "The emisora name must not be empty.";
  private static final String MESSAGE_NOMBRE_SHORT =
      "The emisora name must have at least %d characters.";
  private static final String MESSAGE_REQUIRED = "The field '%s' is required.";
  private static final String MESSAGE_FM_RANGE = "The FM band must be between %.1f and %.1f MHz.";
  private static final String MESSAGE_AM_RANGE = "The AM band must be between %d and %d kHz.";
  private static final String MESSAGE_NO_BAND = "The emisora must broadcast on FM, AM or both.";
  private static final String MESSAGE_NEGATIVE = "The field '%s' must not be negative.";
  private static final String MESSAGE_MIN = "The field '%s' must be at least %d.";
  private static final String MESSAGE_GENERO = "The genero '%s' is not valid.";

  private InvalidEmisoraException(final String message) {
    super(message);
  }

  public static InvalidEmisoraException becauseIdIsEmpty() {
    return new InvalidEmisoraException(MESSAGE_ID_EMPTY);
  }

  public static InvalidEmisoraException becauseNombreIsEmpty() {
    return new InvalidEmisoraException(MESSAGE_NOMBRE_EMPTY);
  }

  public static InvalidEmisoraException becauseNombreIsTooShort(final int minimum) {
    return new InvalidEmisoraException(String.format(MESSAGE_NOMBRE_SHORT, minimum));
  }

  public static InvalidEmisoraException becauseFieldIsRequired(final String field) {
    return new InvalidEmisoraException(String.format(MESSAGE_REQUIRED, field));
  }

  public static InvalidEmisoraException becauseFmIsOutOfRange(final double min, final double max) {
    return new InvalidEmisoraException(String.format(Locale.ROOT, MESSAGE_FM_RANGE, min, max));
  }

  public static InvalidEmisoraException becauseAmIsOutOfRange(final int min, final int max) {
    return new InvalidEmisoraException(String.format(MESSAGE_AM_RANGE, min, max));
  }

  public static InvalidEmisoraException becauseHasNoBand() {
    return new InvalidEmisoraException(MESSAGE_NO_BAND);
  }

  public static InvalidEmisoraException becauseValueIsNegative(final String field) {
    return new InvalidEmisoraException(String.format(MESSAGE_NEGATIVE, field));
  }

  public static InvalidEmisoraException becauseValueIsBelowMinimum(
      final String field, final int minimum) {
    return new InvalidEmisoraException(String.format(MESSAGE_MIN, field, minimum));
  }

  public static InvalidEmisoraException becauseGeneroIsInvalid(final String value) {
    return new InvalidEmisoraException(String.format(MESSAGE_GENERO, value));
  }
}
