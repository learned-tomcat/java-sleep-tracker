package ru.yandex.practicum.sleeptracker;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class SleepingSession {

    private final LocalDateTime sleepStart;
    private final LocalDateTime sleepEnd;
    private final SleepQuality quality;

    public SleepingSession(LocalDateTime sleepStart, LocalDateTime sleepEnd, SleepQuality quality) {
        if (sleepStart == null || sleepEnd == null || quality == null) {
            throw new IllegalArgumentException("Поля сессии сна не могут быть null.");
        }

        if (!sleepEnd.isAfter(sleepStart)) {
            throw new IllegalArgumentException("Время пробуждения должно быть позже времени засыпания.");
        }

        this.sleepStart = sleepStart;
        this.sleepEnd = sleepEnd;
        this.quality = quality;
    }

    public LocalDateTime getSleepStart() {
        return sleepStart;
    }

    public LocalDateTime getSleepEnd() {
        return sleepEnd;
    }

    public SleepQuality getQuality() {
        return quality;
    }

    public long getDurationInMinutes() {
        return Duration.between(sleepStart, sleepEnd).toMinutes();
    }

    public boolean hasBadQuality() {
        return getQuality() == SleepQuality.BAD;
    }

    public boolean intersectsNight(LocalDate nightDate) {
        LocalDateTime nightStart = nightDate.atStartOfDay();
        LocalDateTime nightEnd = nightDate.atTime(6, 0);

        return sleepStart.isBefore(nightEnd) && sleepEnd.isAfter(nightStart);
    }
}
