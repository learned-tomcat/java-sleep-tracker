package ru.yandex.practicum.sleeptracker;

import ru.yandex.practicum.sleeptracker.analysis.AverageSleepDurationFunction;
import ru.yandex.practicum.sleeptracker.analysis.BadQualitySleepCountFunction;
import ru.yandex.practicum.sleeptracker.analysis.ChronotypeFunction;
import ru.yandex.practicum.sleeptracker.analysis.MaxSleepDurationFunction;
import ru.yandex.practicum.sleeptracker.analysis.MinSleepDurationFunction;
import ru.yandex.practicum.sleeptracker.analysis.SleeplessNightsCountFunction;
import ru.yandex.practicum.sleeptracker.analysis.TotalSessionsCountFunction;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

public class SleepTrackerApp {

    private static final DateTimeFormatter DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");

    private static final List<Function<List<SleepingSession>, ? extends SleepAnalysisResult<?>>> ANALYSIS_FUNCTIONS =
            List.of(
                    new TotalSessionsCountFunction(),
                    new MinSleepDurationFunction(),
                    new MaxSleepDurationFunction(),
                    new AverageSleepDurationFunction(),
                    new BadQualitySleepCountFunction(),
                    new SleeplessNightsCountFunction(),
                    new ChronotypeFunction()
            );

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Ошибка: передайте путь к файлу с логом сна.");
            System.out.println("Пример запуска:");
            System.out.println("src/main/resources/sleep_log.txt");
            return;
        }

        Path path = Path.of(args[0]);

        try {
            List<SleepingSession> sessions = readSleepingSessions(path);

            System.out.println("Анализ сна:");

            ANALYSIS_FUNCTIONS.stream()
                    .map(function -> function.apply(sessions))
                    .map(result -> result.getDescription() + ": " + result.getValue())
                    .forEach(System.out::println);
        } catch (IOException exception) {
            System.out.println("Не удалось прочитать файл: " + exception.getMessage());
        } catch (RuntimeException exception) {
            System.out.println("Ошибка при анализе файла: " + exception.getMessage());
        }
    }

    private static List<SleepingSession> readSleepingSessions(Path path) throws IOException {
        try (Stream<String> lines = Files.lines(path, StandardCharsets.UTF_8)) {
            return lines
                    .filter(line -> !line.isBlank())
                    .map(SleepTrackerApp::parseSleepingSession)
                    .toList();
        }
    }

    private static SleepingSession parseSleepingSession(String line) {
        String[] parts = line.split(";");

        if (parts.length != 3) {
            throw new IllegalArgumentException("Некорректная строка лога: " + line);
        }

        LocalDateTime sleepStart = LocalDateTime.parse(parts[0].trim(), DATE_TIME_FORMATTER);
        LocalDateTime sleepEnd = LocalDateTime.parse(parts[1].trim(), DATE_TIME_FORMATTER);
        SleepQuality quality = SleepQuality.valueOf(parts[2].trim());

        return new SleepingSession(sleepStart, sleepEnd, quality);
    }
}