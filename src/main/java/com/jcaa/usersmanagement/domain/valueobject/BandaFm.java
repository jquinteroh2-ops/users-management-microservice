package com.jcaa.usersmanagement.domain.valueobject;

import com.jcaa.usersmanagement.domain.exception.InvalidEmisoraException;

/** Frecuencia en la banda FM, expresada en MHz (87.5 a 108.0). */
public record BandaFm(double megahertz) {

  public static final double MINIMUM = 87.5;
  public static final double MAXIMUM = 108.0;

  public BandaFm {
    if (megahertz < MINIMUM || megahertz > MAXIMUM) {
      throw InvalidEmisoraException.becauseFmIsOutOfRange(MINIMUM, MAXIMUM);
    }
  }

  /** Devuelve null si la emisora no transmite en FM. */
  public static BandaFm ofNullable(final Double megahertz) {
    return megahertz == null ? null : new BandaFm(megahertz);
  }
}
