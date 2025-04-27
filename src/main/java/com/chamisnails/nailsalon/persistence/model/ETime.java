package com.chamisnails.nailsalon.persistence.model;

import java.time.LocalTime;

public enum ETime {
    HORA_09_00(LocalTime.of(9, 0)),
    HORA_11_00(LocalTime.of(11, 0)),
    HORA_14_00(LocalTime.of(14, 0)),
    HORA_16_00(LocalTime.of(16, 0));

    private final LocalTime hora;

    ETime(LocalTime hora) {
        this.hora = hora;
    }

    public LocalTime getHour() {
        return hora;
    }
}
