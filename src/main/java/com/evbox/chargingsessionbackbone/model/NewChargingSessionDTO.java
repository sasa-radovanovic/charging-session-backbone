package com.evbox.chargingsessionbackbone.model;

import lombok.Data;

import java.util.UUID;

/**
 * DTO object for creating new charging session
 */
@Data
public class NewChargingSessionDTO {

    private UUID stationId;
}
