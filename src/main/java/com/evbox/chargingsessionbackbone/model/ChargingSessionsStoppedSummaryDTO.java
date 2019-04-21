package com.evbox.chargingsessionbackbone.model;

import lombok.Data;

/**
 * DTO object holding count of stopped charging sessions in the last X minute(s)
 */
@Data
public class ChargingSessionsStoppedSummaryDTO {

    private long stoppedCount;

    public ChargingSessionsStoppedSummaryDTO() {
        this.stoppedCount = 0;
    }
}
