package com.evbox.chargingsessionbackbone.entity;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Internal entity of charging session
 */
@Data
public class ChargingSession {

    private LocalDateTime startedAt;

    private LocalDateTime finishedAt;

    private ChargingSessionStatus status;

    private UUID stationId;
}
