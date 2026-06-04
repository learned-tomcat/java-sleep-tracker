package ru.yandex.practicum.sleeptracker;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertTrue;

class SleepTrackerAppTest {

    @Test
    void shouldPrintErrorWhenFilePathIsNotProvided() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;

        try {
            System.setOut(new PrintStream(outputStream, true, StandardCharsets.UTF_8));

            SleepTrackerApp.main(new String[]{});

            String output = outputStream.toString(StandardCharsets.UTF_8);

            assertTrue(output.contains("Ошибка: передайте путь к файлу с логом сна."));
        } finally {
            System.setOut(originalOut);
        }
    }

    @Test
    void shouldAnalyzeSleepLogFile() throws Exception {
        Path sleepLogFile = Files.createTempFile("sleep-log", ".txt");

        Files.writeString(
                sleepLogFile,
                """
                01.10.25 23:15;02.10.25 07:30;GOOD
                02.10.25 23:50;03.10.25 06:40;NORMAL
                03.10.25 14:10;03.10.25 15:00;NORMAL
                03.10.25 23:40;04.10.25 08:00;BAD
                """,
                StandardCharsets.UTF_8
        );

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;

        try {
            System.setOut(new PrintStream(outputStream, true, StandardCharsets.UTF_8));

            SleepTrackerApp.main(new String[]{sleepLogFile.toString()});

            String output = outputStream.toString(StandardCharsets.UTF_8);

            assertTrue(output.contains("Анализ сна"));
            assertTrue(output.contains("Всего сессий сна: 4"));
            assertTrue(output.contains("Минимальная продолжительность сессии сна в минутах: 50"));
            assertTrue(output.contains("Максимальная продолжительность сессии сна в минутах: 500"));
            assertTrue(output.contains("Количество сессий с плохим качеством сна: 1"));
            assertTrue(output.contains("Хронотип пользователя"));
        } finally {
            System.setOut(originalOut);
            Files.deleteIfExists(sleepLogFile);
        }
    }
}