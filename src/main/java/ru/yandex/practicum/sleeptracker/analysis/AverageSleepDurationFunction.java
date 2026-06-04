package ru.yandex.practicum.sleeptracker.analysis;

import ru.yandex.practicum.sleeptracker.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.SleepingSession;

import java.util.List;
import java.util.function.Function;

public class AverageSleepDurationFunction implements Function<List<SleepingSession>, SleepAnalysisResult<Long>> {

    @Override
    public SleepAnalysisResult<Long> apply(List<SleepingSession> sessions) {
        long averageDuration = Math.round(
                sessions.stream()
                        .mapToLong(SleepingSession::getDurationInMinutes)
                        .average()
                        .orElse(0)
        );

        return new SleepAnalysisResult<>(
                "Средняя продолжительность сессии сна в минутах",
                averageDuration
        );
    }
}
