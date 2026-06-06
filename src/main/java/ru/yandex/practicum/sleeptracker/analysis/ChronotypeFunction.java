package ru.yandex.practicum.sleeptracker.analysis;

import ru.yandex.practicum.sleeptracker.Chronotype;
import ru.yandex.practicum.sleeptracker.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.SleepingSession;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ChronotypeFunction implements Function<List<SleepingSession>, SleepAnalysisResult<Chronotype>> {

    private static final String DESCRIPTION = "Хронотип пользователя";

    private static final LocalTime OWL_SLEEP_TIME = LocalTime.of(23, 0);
    private static final LocalTime OWL_WAKE_UP_TIME = LocalTime.of(9, 0);

    private static final LocalTime LARK_SLEEP_TIME = LocalTime.of(22, 0);
    private static final LocalTime LARK_WAKE_UP_TIME = LocalTime.of(7, 0);

    private static final LocalTime EARLY_MORNING_BORDER = LocalTime.of(6, 0);

    @Override
    public SleepAnalysisResult<Chronotype> apply(List<SleepingSession> sessions) {
        if (sessions == null) {
            throw new IllegalArgumentException("Список сессий сна не может быть null.");
        }

        Chronotype chronotype = defineChronotype(sessions);

        return new SleepAnalysisResult<>(DESCRIPTION, chronotype);
    }

    private Chronotype defineChronotype(List<SleepingSession> sessions) {
        Map<Chronotype, Long> chronotypeCounts = sessions.stream()
                .filter(this::isNightSleep)
                .map(this::classifySession)
                .collect(Collectors.groupingBy(
                        Function.identity(),
                        Collectors.counting()
                ));

        long owlCount = chronotypeCounts.getOrDefault(Chronotype.OWL, 0L);
        long larkCount = chronotypeCounts.getOrDefault(Chronotype.LARK, 0L);
        long pigeonCount = chronotypeCounts.getOrDefault(Chronotype.PIGEON, 0L);

        if (owlCount > larkCount && owlCount > pigeonCount) {
            return Chronotype.OWL;
        }

        if (larkCount > owlCount && larkCount > pigeonCount) {
            return Chronotype.LARK;
        }

        return Chronotype.PIGEON;
    }

    private boolean isNightSleep(SleepingSession session) {
        LocalDate startDate = session.getSleepStart().toLocalDate();
        LocalDate endDate = session.getSleepEnd().toLocalDate();

        return session.intersectsNight(startDate)
                || session.intersectsNight(endDate);
    }

    private Chronotype classifySession(SleepingSession session) {
        LocalTime sleepStartTime = session.getSleepStart().toLocalTime();
        LocalTime wakeUpTime = session.getSleepEnd().toLocalTime();

        if (isLateSleepStart(sleepStartTime) && wakeUpTime.isAfter(OWL_WAKE_UP_TIME)) {
            return Chronotype.OWL;
        }

        if (isEarlySleepStart(sleepStartTime) && wakeUpTime.isBefore(LARK_WAKE_UP_TIME)) {
            return Chronotype.LARK;
        }

        return Chronotype.PIGEON;
    }

    private boolean isLateSleepStart(LocalTime sleepStartTime) {
        return sleepStartTime.isAfter(OWL_SLEEP_TIME)
                || sleepStartTime.isBefore(EARLY_MORNING_BORDER);
    }

    private boolean isEarlySleepStart(LocalTime sleepStartTime) {
        return sleepStartTime.isBefore(LARK_SLEEP_TIME);
    }
}