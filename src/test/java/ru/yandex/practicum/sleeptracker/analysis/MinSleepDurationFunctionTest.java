package ru.yandex.practicum.sleeptracker.analysis;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.sleeptracker.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.SleepQuality;
import ru.yandex.practicum.sleeptracker.SleepingSession;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MinSleepDurationFunctionTest {

    private final MinSleepDurationFunction function = new MinSleepDurationFunction();

    @Test
    void shouldReturnZeroForEmptyList() {
        SleepAnalysisResult<Long> result = function.apply(List.of());

        assertEquals(0, result.getValue());
    }

    @Test
    void shouldReturnMinDurationInMinutes() {
        List<SleepingSession> sessions = List.of(
                session("2025-10-01T23:00", "2025-10-02T07:00"),
                session("2025-10-02T14:00", "2025-10-02T14:45"),
                session("2025-10-03T23:00", "2025-10-04T06:00")
        );

        SleepAnalysisResult<Long> result = function.apply(sessions);

        assertEquals(45, result.getValue());
    }

    private SleepingSession session(String start, String end) {
        return new SleepingSession(
                LocalDateTime.parse(start),
                LocalDateTime.parse(end),
                SleepQuality.GOOD
        );
    }
}
