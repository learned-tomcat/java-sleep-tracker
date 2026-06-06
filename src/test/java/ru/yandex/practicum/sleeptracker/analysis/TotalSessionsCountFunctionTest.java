package ru.yandex.practicum.sleeptracker.analysis;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.sleeptracker.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.SleepQuality;
import ru.yandex.practicum.sleeptracker.SleepingSession;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TotalSessionsCountFunctionTest {

    private final TotalSessionsCountFunction function = new TotalSessionsCountFunction();

    @Test
    void shouldReturnZeroForEmptyList() {
        SleepAnalysisResult<Integer> result = function.apply(List.of());

        assertEquals(0, result.getValue());
    }

    @Test
    void shouldCountAllSessions() {
        List<SleepingSession> sessions = List.of(
                session("2025-10-01T23:00", "2025-10-02T07:00", SleepQuality.GOOD),
                session("2025-10-02T23:00", "2025-10-03T07:00", SleepQuality.NORMAL),
                session("2025-10-03T14:00", "2025-10-03T15:00", SleepQuality.BAD)
        );

        SleepAnalysisResult<Integer> result = function.apply(sessions);

        assertEquals(3, result.getValue());
    }

    private SleepingSession session(String start, String end, SleepQuality quality) {
        return new SleepingSession(
                LocalDateTime.parse(start),
                LocalDateTime.parse(end),
                quality
        );
    }
}