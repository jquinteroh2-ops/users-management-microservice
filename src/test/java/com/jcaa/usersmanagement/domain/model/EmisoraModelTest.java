package com.jcaa.usersmanagement.domain.model;

import static org.junit.jupiter.api.Assertions.*;

import com.jcaa.usersmanagement.domain.enums.GeneroEmisora;
import com.jcaa.usersmanagement.domain.exception.InvalidEmisoraException;
import com.jcaa.usersmanagement.domain.valueobject.BandaAm;
import com.jcaa.usersmanagement.domain.valueobject.BandaFm;
import com.jcaa.usersmanagement.domain.valueobject.EmisoraId;
import com.jcaa.usersmanagement.domain.valueobject.EmisoraNombre;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("EmisoraModel y sus Value Objects")
class EmisoraModelTest {

  private static EmisoraModel emisora(final BandaFm fm, final BandaAm am, final int locutores) {
    return new EmisoraModel(
        new EmisoraId("emi-001"),
        new EmisoraNombre("  Radio   Cartagena  "),
        "HJKC",
        fm,
        am,
        locutores,
        GeneroEmisora.MUSICAL,
        "24 horas",
        null,
        "Colombia",
        null,
        10,
        2);
  }

  @Test
  @DisplayName("Crea una emisora válida y normaliza el nombre")
  void shouldCreateValidEmisora() {
    final EmisoraModel model = emisora(new BandaFm(95.5), null, 5);

    assertEquals("Radio Cartagena", model.getNombre().value());
    assertTrue(model.transmiteEnFm());
    assertFalse(model.transmiteEnAm());
    assertEquals("", model.getPatrocinador());
  }

  @Test
  @DisplayName("Rechaza una emisora que no transmite en FM ni en AM")
  void shouldRejectEmisoraWithoutBand() {
    assertThrows(InvalidEmisoraException.class, () -> emisora(null, null, 5));
  }

  @Test
  @DisplayName("Rechaza menos de un locutor")
  void shouldRejectZeroLocutores() {
    assertThrows(InvalidEmisoraException.class, () -> emisora(new BandaFm(100.0), null, 0));
  }

  @ParameterizedTest
  @ValueSource(doubles = {87.4, 108.1, 0, -5})
  @DisplayName("BandaFm fuera de 87.5-108.0 MHz es inválida")
  void shouldRejectFmOutOfRange(final double value) {
    assertThrows(InvalidEmisoraException.class, () -> new BandaFm(value));
  }

  @ParameterizedTest
  @ValueSource(ints = {529, 1711, 0})
  @DisplayName("BandaAm fuera de 530-1710 kHz es inválida")
  void shouldRejectAmOutOfRange(final int value) {
    assertThrows(InvalidEmisoraException.class, () -> new BandaAm(value));
  }

  @ParameterizedTest
  @ValueSource(strings = {"", "  ", "Ra"})
  @DisplayName("El nombre no puede ser vacío ni tener menos de 3 caracteres")
  void shouldRejectInvalidNombre(final String nombre) {
    assertThrows(InvalidEmisoraException.class, () -> new EmisoraNombre(nombre));
  }

  @Test
  @DisplayName("El género se convierte sin importar mayúsculas y rechaza valores desconocidos")
  void shouldParseGenero() {
    assertEquals(GeneroEmisora.NOTICIAS, GeneroEmisora.fromString(" noticias "));
    assertThrows(InvalidEmisoraException.class, () -> GeneroEmisora.fromString("ROCKOLA"));
  }
}
