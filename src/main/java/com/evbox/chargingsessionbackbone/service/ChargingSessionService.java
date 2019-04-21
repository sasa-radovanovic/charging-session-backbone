package com.evbox.chargingsessionbackbone.service;

import com.evbox.chargingsessionbackbone.model.ChargingSessionsStartedSummaryDTO;
import com.evbox.chargingsessionbackbone.model.ChargingSessionsStoppedSummaryDTO;
import com.evbox.chargingsessionbackbone.model.ChargingSessionsSummaryDTO;
import com.evbox.chargingsessionbackbone.model.NewChargingSessionDTO;

import java.util.UUID;

/**
 * Charging session service provides business logic methods for manipulating and monitoring charging sessions
 */
public interface ChargingSessionService {

    /**
     *
     * Add new charging session
     *
     * @param newChargingSessionDTO - {@link NewChargingSessionDTO} object received through REST endpoint
     * @return {@link UUID} - id of the new session
     */
    UUID submitNewChargingSession(NewChargingSessionDTO newChargingSessionDTO);

    /**
     *
     * Stop running charging session.
     *
     * @param chargingSessionId - {@link UUID} of the session to be stopped
     * @return {@link UUID} id of the stopped charging session or null if operation is not possible
     */
    UUID stopChargingSession(UUID chargingSessionId);

    /**
     *
     * Retrieve summary of charging sessions (in the last minute)
     *
     * @return {@link ChargingSessionsSummaryDTO} object containing totalCount, stopCount and startCount
     */
    ChargingSessionsSummaryDTO getChargingSessionsSummary();

    /**
     *
     * Retrieve summary of stopped charging sessions (in the last minute)
     *
     * @return {@link ChargingSessionsStoppedSummaryDTO} object containing the count of stopped sessions
     */
    ChargingSessionsStoppedSummaryDTO getStoppedChargingSessionsSummary();

    /**
     *
     * Retrieve summary of started charging sessions (in the last minute)
     *
     * @return {@link ChargingSessionsStartedSummaryDTO} object containing the count of started sessions
     */
    ChargingSessionsStartedSummaryDTO getStartedChargingSessionsSummary();

    /**
     *
     * Remove all stopped sessions from the system
     *
     * @return simple text notification
     */
    String removeStoppedChargingSessions();

}
