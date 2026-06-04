package ru.yandex.practicum.sleeptracker.analysis;

import ru.yandex.practicum.sleeptracker.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.SleepingSession;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.function.Function;
import java.util.stream.LongStream;

public class SleeplessNightsCountFunction implements Function<List<SleepingSession>, SleepAnalysisResult<Long>> {

    private static final LocalTime NOON = LocalTime.NOON;

    @Override
    public SleepAnalysisResult<Long> apply(List<SleepingSession> sessions) {
        long sleeplessNights = countSleeplessNights(sessions);

        return new SleepAnalysisResult<>(
                "Количество бессонных ночей",
                sleeplessNights
        );
    }

    private long countSleeplessNights(List<SleepingSession> sessions) {
        if (sessions.isEmpty()) {
            return 0;
        }

        LocalDate firstNightDate = getFirstPotentialNightDate(sessions);
        LocalDate lastNightDateExclusive = getLastPotentialNightDateExclusive(sessions);

        long nightsCount = ChronoUnit.DAYS.between(firstNightDate, lastNightDateExclusive);

        return LongStream.range(0, nightsCount)
                .mapToObj(firstNightDate::plusDays)
                .filter(nightDate -> isSleeplessNight(nightDate, sessions))
                .count();
    }

    private LocalDate getFirstPotentialNightDate(List<SleepingSession> sessions) {
        SleepingSession firstSession = sessions.get(0);

        LocalDate firstDate = firstSession.getSleepStart().toLocalDate();
        LocalTime firstTime = firstSession.getSleepStart().toLocalTime();

        if (firstTime.isAfter(NOON) || firstTime.equals(NOON)) {
            return firstDate.plusDays(1);
        }

        return firstDate;
    }

    private LocalDate getLastPotentialNightDateExclusive(List<SleepingSession> sessions) {
        SleepingSession lastSession = sessions.get(sessions.size() - 1);

        return lastSession.getSleepEnd()
                .toLocalDate()
                .plusDays(1);
    }

    private boolean isSleeplessNight(LocalDate nightDate, List<SleepingSession> sessions) {
        return sessions.stream()
                .noneMatch(session -> session.intersectsNight(nightDate));
    }
}
