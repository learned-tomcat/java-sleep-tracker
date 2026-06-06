package ru.yandex.practicum.sleeptracker.analysis;

import ru.yandex.practicum.sleeptracker.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.SleepingSession;

import java.util.List;
import java.util.function.Function;

public class TotalSessionsCountFunction implements Function<List<SleepingSession>, SleepAnalysisResult<Integer>> {

    private static final String DESCRIPTION = "Всего сессий сна";

    @Override
    public SleepAnalysisResult<Integer> apply(List<SleepingSession> sessions) {
        if (sessions == null) {
            throw new IllegalArgumentException("Список сессий сна не может быть null.");
        }

        return new SleepAnalysisResult<>(DESCRIPTION, sessions.size());
    }
}
