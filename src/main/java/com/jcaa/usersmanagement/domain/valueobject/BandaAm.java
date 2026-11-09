package com.jcaa.usersmanagement.domain.valueobject;

import com.jcaa.usersmanagement.domain.exception.InvalidEmisoraException;

/** Frecuencia en la banda AM, expresada en kHz (530 a 1710). */
public record BandaAm(int kilohertz) {

  public static final int MINIMUM = 530;
  public static final int MAXIMUM = 1710;

  public BandaAm {
    if (kilohertz < MINIMUM || kilohertz > MAXIMUM) {
      throw InvalidEmisoraException.becauseAmIsOutOfRange(MINIMUM, MAXIMUM);
    }
  }

  /** Devuelve null si la emisora no transmite en AM. */
  public static BandaAm ofNullable(final Integer kilohertz) {
    return kilohertz == null ? null : new BandaAm(kilohertz);
  }
}
