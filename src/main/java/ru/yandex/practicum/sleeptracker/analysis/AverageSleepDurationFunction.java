package ru.yandex.practicum.sleeptracker.analysis;

import ru.yandex.practicum.sleeptracker.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.SleepingSession;

import java.util.List;
import java.util.function.Function;

public class AverageSleepDurationFunction implements Function<List<SleepingSession>, SleepAnalysisResult<Long>> {

    private static final String DESCRIPTION = "Средняя продолжительность сессии сна в минутах";

    @Override
    public SleepAnalysisResult<Long> apply(List<SleepingSession> sessions) {
        if (sessions == null) {
            throw new IllegalArgumentException("Список сессий сна не может быть null.");
        }

        long averageDuration = Math.round(
                sessions.stream()
                        .mapToLong(SleepingSession::getDurationInMinutes)
                        .average()
                        .orElse(0)
        );

        return new SleepAnalysisResult<>(DESCRIPTION, averageDuration);
    }
}
