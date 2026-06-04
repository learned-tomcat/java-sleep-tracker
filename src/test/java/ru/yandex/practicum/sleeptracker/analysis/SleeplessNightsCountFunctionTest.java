package ru.yandex.practicum.sleeptracker.analysis;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.sleeptracker.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.SleepQuality;
import ru.yandex.practicum.sleeptracker.SleepingSession;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SleeplessNightsCountFunctionTest {

    private final SleeplessNightsCountFunction function = new SleeplessNightsCountFunction();

    @Test
    void shouldReturnZeroForEmptyList() {
        SleepAnalysisResult<Long> result = function.apply(List.of());

        assertEquals(0, result.getValue());
    }

    @Test
    void shouldNotCountNightAsSleeplessWhenSleepFrom23To3() {
        List<SleepingSession> sessions = List.of(
                session("2025-10-01T23:00", "2025-10-02T03:00")
        );

        SleepAnalysisResult<Long> result = function.apply(sessions);

        assertEquals(0, result.getValue());
    }

    @Test
    void shouldNotCountNightAsSleeplessWhenSleepFrom2To7() {
        List<SleepingSession> sessions = List.of(
                session("2025-10-02T02:00", "2025-10-02T07:00")
        );

        SleepAnalysisResult<Long> result = function.apply(sessions);

        assertEquals(0, result.getValue());
    }

    @Test
    void shouldCountNightAsSleeplessWhenSleepOnlyFrom7To11() {
        List<SleepingSession> sessions = List.of(
                session("2025-10-02T07:00", "2025-10-02T11:00")
        );

        SleepAnalysisResult<Long> result = function.apply(sessions);

        assertEquals(1, result.getValue());
    }

    @Test
    void shouldCountSleeplessNightsBetweenOctoberAndNovember() {
        List<SleepingSession> sessions = List.of(
                session("2025-10-30T23:00", "2025-10-31T06:00"),
                session("2025-11-02T23:00", "2025-11-03T06:00")
        );

        SleepAnalysisResult<Long> result = function.apply(sessions);

        assertEquals(2, result.getValue());
    }

    private SleepingSession session(String start, String end) {
        return new SleepingSession(
                LocalDateTime.parse(start),
                LocalDateTime.parse(end),
                SleepQuality.GOOD
        );
    }
}
