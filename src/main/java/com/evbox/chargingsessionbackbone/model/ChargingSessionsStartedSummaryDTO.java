package com.evbox.chargingsessionbackbone.model;

import lombok.Data;

/**
 * DTO object holding count of started charging sessions in the last X minute(s)
 */
@Data
public class ChargingSessionsStartedSummaryDTO {

    private long startedCount;

    public ChargingSessionsStartedSummaryDTO() {
        this.startedCount = 0;
    }
}
