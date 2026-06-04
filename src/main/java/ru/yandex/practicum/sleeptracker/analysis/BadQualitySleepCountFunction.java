package ru.yandex.practicum.sleeptracker.analysis;

import ru.yandex.practicum.sleeptracker.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.SleepingSession;

import java.util.List;
import java.util.function.Function;

public class BadQualitySleepCountFunction implements Function<List<SleepingSession>, SleepAnalysisResult<Long>> {

    @Override
    public SleepAnalysisResult<Long> apply(List<SleepingSession> sessions) {
        long badQualitySessions = sessions.stream()
                .filter(SleepingSession::hasBadQuality)
                .count();

        return new SleepAnalysisResult<>(
                "Количество сессий с плохим качеством сна",
                badQualitySessions
        );
    }
}