package com.evbox.chargingsessionbackbone.model;

import lombok.Data;

/**
 * DTO object holding summary counts for the last X minute(s)
 */
@Data
public class ChargingSessionsSummaryDTO {

    private long totalCount;

    private long startedCount;

    private long stoppedCount;

    public ChargingSessionsSummaryDTO() {
        this.totalCount = 0;
        this.startedCount = 0;
        this.stoppedCount = 0;
    }
}
