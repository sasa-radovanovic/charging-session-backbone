package com.evbox.chargingsessionbackbone.util;

import com.evbox.chargingsessionbackbone.entity.ChargingSession;
import com.evbox.chargingsessionbackbone.entity.ChargingSessionStatus;
import com.evbox.chargingsessionbackbone.model.NewChargingSessionDTO;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

/**
 * Utility class for all charging session-related operations
 */
@Slf4j
public final class ChargingSessionUtil {

    private ChargingSessionUtil() {}

    // Static holder for marking the target timeframe
    public static final int MINUTES_IN_PAST = 1;

    // All possible contexts of Charging session in the target timeframe
    public static final class ChargingSessionContext {

        private ChargingSessionContext() {}

        public static final String STARTED = "STARTED";
        public static final String STARTED_AND_STOPPED = "STARTED_STOPPED";
        public static final String STOPPED = "STOPPED";
        public static final String RUNNING = "RUNNING";
        public static final String NONE = "NONE";
    }


    /**
     *
     * Map request for new charging session to new {@link ChargingSession} object
     *
     * @param newChargingSessionDTO - {@link NewChargingSessionDTO} request from REST endpoint
     * @return {@link ChargingSession} endpoint
     */
    public static ChargingSession chargingSessionFromNewSessionDTO(NewChargingSessionDTO newChargingSessionDTO) {
        ChargingSession chargingSession = new ChargingSession();
        chargingSession.setStartedAt(LocalDateTime.now());
        chargingSession.setFinishedAt(null);
        chargingSession.setStationId(newChargingSessionDTO.getStationId());
        chargingSession.setStatus(ChargingSessionStatus.IN_PROGRESS);
        return chargingSession;
    }


    /**
     *
     * Map charging session to it's context in the last X minute(s). Charging session can have following contexts (roles) in the last
     * X minute(s):
     *          STARTED - Meaning that charging session was started in the last X minute(s) and is running
     *          STOPPED - Meaning that charging session was started before the last X minute(s) and was stopped in the last X minute(s)
     *          STARTED_AND_STOPPED - Meaning that charging session was both started and stopped in the last X minute(s)
     *          RUNNING - Charging session was started before the target period and is still running
     *          NONE - None of the above, meaning that charging session is not relevant in this timeframe
     *
     * @param chargingSession - {@link ChargingSession} object
     * @param now - Timestamp of the request (reference timestamp)
     * @return String representation of charging session's context, refer to explanation above
     */
    public static String chargingSessionInTimePeriod(ChargingSession chargingSession, LocalDateTime now) {
        if (chargingSession.getStartedAt().isAfter(now.minusMinutes(MINUTES_IN_PAST))) {
            if (ChargingSessionStatus.STOPPED.equals(chargingSession.getStatus())) {
                return ChargingSessionContext.STARTED_AND_STOPPED;
            } else {
                return ChargingSessionContext.STARTED;
            }
        }
        if (ChargingSessionStatus.STOPPED.equals(chargingSession.getStatus()) &&
                chargingSession.getFinishedAt().isAfter(now.minusMinutes(MINUTES_IN_PAST))) {
            return ChargingSessionContext.STOPPED;
        }
        if (chargingSession.getFinishedAt() == null) {
            return ChargingSessionContext.RUNNING;
        }
        return ChargingSessionContext.NONE;
    }

}
