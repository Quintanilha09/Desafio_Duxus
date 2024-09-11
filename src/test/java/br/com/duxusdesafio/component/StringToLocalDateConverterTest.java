package br.com.duxusdesafio.component;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class StringToLocalDateConverterTest {

    @Autowired
    private StringToLocalDateConverter converter;

    @Test
    public void testConvert_ValidDate() {
        String source = "2024-09-15";

        LocalDate result = converter.convert(source);

        LocalDate expected = LocalDate.of(2024, 9, 15);
        assertEquals(expected, result, "O conversor deve converter corretamente a string para LocalDate.");
    }

    @Test
    public void testConvert_InvalidDateFormat() {
        String source = "15-09-2024";

        assertThrows(java.time.format.DateTimeParseException.class, () -> {
            converter.convert(source);
        }, "O conversor deve lançar uma exceção ao encontrar um formato de data inválido.");
    }
}
