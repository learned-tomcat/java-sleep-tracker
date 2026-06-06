package ru.yandex.practicum.sleeptracker.analysis;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.sleeptracker.Chronotype;
import ru.yandex.practicum.sleeptracker.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.SleepQuality;
import ru.yandex.practicum.sleeptracker.SleepingSession;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ChronotypeFunctionTest {

    private final ChronotypeFunction function = new ChronotypeFunction();

    @Test
    void shouldReturnPigeonForEmptyList() {
        SleepAnalysisResult<Chronotype> result = function.apply(List.of());

        assertEquals(Chronotype.PIGEON, result.getValue());
    }

    @Test
    void shouldReturnOwlWhenOwlNightsAreMoreFrequent() {
        List<SleepingSession> sessions = List.of(
                session("2025-10-01T23:30", "2025-10-02T09:30"),
                session("2025-10-02T23:40", "2025-10-03T10:00"),
                session("2025-10-03T21:30", "2025-10-04T06:30")
        );

        SleepAnalysisResult<Chronotype> result = function.apply(sessions);

        assertEquals(Chronotype.OWL, result.getValue());
    }

    @Test
    void shouldReturnLarkWhenLarkNightsAreMoreFrequent() {
        List<SleepingSession> sessions = List.of(
                session("2025-10-01T21:30", "2025-10-02T06:30"),
                session("2025-10-02T21:45", "2025-10-03T06:50"),
                session("2025-10-03T23:30", "2025-10-04T09:30")
        );

        SleepAnalysisResult<Chronotype> result = function.apply(sessions);

        assertEquals(Chronotype.LARK, result.getValue());
    }

    @Test
    void shouldReturnPigeonWhenThereIsTie() {
        List<SleepingSession> sessions = List.of(
                session("2025-10-01T23:30", "2025-10-02T09:30"),
                session("2025-10-02T21:30", "2025-10-03T06:30")
        );

        SleepAnalysisResult<Chronotype> result = function.apply(sessions);

        assertEquals(Chronotype.PIGEON, result.getValue());
    }

    @Test
    void shouldIgnoreDaySleep() {
        List<SleepingSession> sessions = List.of(
                session("2025-10-01T13:00", "2025-10-01T14:00"),
                session("2025-10-01T23:30", "2025-10-02T09:30")
        );

        SleepAnalysisResult<Chronotype> result = function.apply(sessions);

        assertEquals(Chronotype.OWL, result.getValue());
    }

    private SleepingSession session(String start, String end) {
        return new SleepingSession(
                LocalDateTime.parse(start),
                LocalDateTime.parse(end),
                SleepQuality.GOOD
        );
    }
}
