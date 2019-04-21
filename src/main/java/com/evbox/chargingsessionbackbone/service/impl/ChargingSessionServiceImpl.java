package com.evbox.chargingsessionbackbone.service.impl;

import com.evbox.chargingsessionbackbone.entity.ChargingSession;
import com.evbox.chargingsessionbackbone.entity.ChargingSessionStatus;
import com.evbox.chargingsessionbackbone.model.ChargingSessionsStartedSummaryDTO;
import com.evbox.chargingsessionbackbone.model.ChargingSessionsStoppedSummaryDTO;
import com.evbox.chargingsessionbackbone.model.ChargingSessionsSummaryDTO;
import com.evbox.chargingsessionbackbone.model.NewChargingSessionDTO;
import com.evbox.chargingsessionbackbone.service.ChargingSessionService;
import com.evbox.chargingsessionbackbone.util.ChargingSessionUtil;
import com.evbox.chargingsessionbackbone.util.DataHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

/**
 * Implementation of {@link ChargingSessionService}
 * {@inheritDoc}
 */
@Service
@Slf4j
public class ChargingSessionServiceImpl implements ChargingSessionService {

    /**
     * {@inheritDoc}
     */
    @Override
    public UUID submitNewChargingSession(NewChargingSessionDTO newChargingSessionDTO) {
        // Generate unique identifier for the session
        UUID sessionId = UUID.randomUUID();
        // Map request to internal entity
        ChargingSession chargingSession = ChargingSessionUtil.chargingSessionFromNewSessionDTO(newChargingSessionDTO);
        // Save it to internal data storage
        DataHolder.chargingSessions.put(sessionId, chargingSession);
        log.info("Added new session {}", sessionId);
        return sessionId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UUID stopChargingSession(UUID chargingSessionId) {
        // Retrieve session from internal data storage
        ChargingSession chargingSession = DataHolder.chargingSessions.get(chargingSessionId);
        // If session does not exist with provided ID or it was already stopped, return null
        // In proper production systems this would be done in a different way
        if (chargingSession == null || ChargingSessionStatus.STOPPED.equals(chargingSession.getStatus())) {
            return null;
        }
        // Set status and finished at timestamp
        chargingSession.setStatus(ChargingSessionStatus.STOPPED);
        chargingSession.setFinishedAt(LocalDateTime.now());
        // Replace object in a map
        DataHolder.chargingSessions.replace(chargingSessionId, chargingSession);
        log.info("Stopped running session {}", chargingSessionId);
        return chargingSessionId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ChargingSessionsSummaryDTO getChargingSessionsSummary() {
        // Take same timestamp for all sessions
        final LocalDateTime now = LocalDateTime.now();
        // Return object holding the counts
        ChargingSessionsSummaryDTO chargingSessionsSummaryDTO = new ChargingSessionsSummaryDTO();
        // Iterate through sessions
        for (ChargingSession chargingSession : DataHolder.chargingSessions.values()) {
            // Map every session accordingly (each session can be either STARTED, STOPPED, STARTED&STOPPED, RUNNING or NONE context of the last minute)
            String chargingSessionInTimePeriod = ChargingSessionUtil.chargingSessionInTimePeriod(chargingSession, now);
            // If STARTED, increase STARTED and TOTAL count
            if (chargingSessionInTimePeriod.equalsIgnoreCase(ChargingSessionUtil.ChargingSessionContext.STARTED)) {
                chargingSessionsSummaryDTO.setStartedCount(chargingSessionsSummaryDTO.getStartedCount() + 1);
                chargingSessionsSummaryDTO.setTotalCount(chargingSessionsSummaryDTO.getTotalCount() + 1);
            } else if (chargingSessionInTimePeriod.equalsIgnoreCase(ChargingSessionUtil.ChargingSessionContext.STOPPED)) {
                // If STOPPED, increase STOPPED and TOTAL count
                chargingSessionsSummaryDTO.setStoppedCount(chargingSessionsSummaryDTO.getStoppedCount() + 1);
                chargingSessionsSummaryDTO.setTotalCount(chargingSessionsSummaryDTO.getTotalCount() + 1);
            } else if (chargingSessionInTimePeriod.equalsIgnoreCase(ChargingSessionUtil.ChargingSessionContext.RUNNING)) {
                // IF RUNNING, increase TOTAL count
                chargingSessionsSummaryDTO.setTotalCount(chargingSessionsSummaryDTO.getTotalCount() + 1);
            } else if (chargingSessionInTimePeriod.equalsIgnoreCase(ChargingSessionUtil.ChargingSessionContext.STARTED_AND_STOPPED)) {
                // IF STARTED&STOPPED increase all 3 counts
                chargingSessionsSummaryDTO.setStartedCount(chargingSessionsSummaryDTO.getStartedCount() + 1);
                chargingSessionsSummaryDTO.setStoppedCount(chargingSessionsSummaryDTO.getStoppedCount() + 1);
                chargingSessionsSummaryDTO.setTotalCount(chargingSessionsSummaryDTO.getTotalCount() + 1);
            }
        }
        return chargingSessionsSummaryDTO;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ChargingSessionsStoppedSummaryDTO getStoppedChargingSessionsSummary() {
        /**
         * Refer to the comments in {@link ChargingSessionServiceImpl#getChargingSessionsSummary()}
         */
        LocalDateTime now = LocalDateTime.now();
        ChargingSessionsStoppedSummaryDTO stoppedSummaryDTO = new ChargingSessionsStoppedSummaryDTO();
        for (ChargingSession chargingSession : DataHolder.chargingSessions.values()) {
            String chargingSessionInTimePeriod = ChargingSessionUtil.chargingSessionInTimePeriod(chargingSession, now);
            if (chargingSessionInTimePeriod.equalsIgnoreCase(ChargingSessionUtil.ChargingSessionContext.STOPPED)
                    || chargingSessionInTimePeriod.equalsIgnoreCase(ChargingSessionUtil.ChargingSessionContext.STARTED_AND_STOPPED)) {
                stoppedSummaryDTO.setStoppedCount(stoppedSummaryDTO.getStoppedCount() + 1);
            }
        }
        return stoppedSummaryDTO;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ChargingSessionsStartedSummaryDTO getStartedChargingSessionsSummary() {
        /**
         * Refer to the comments in {@link ChargingSessionServiceImpl#getChargingSessionsSummary()}
         */
        LocalDateTime now = LocalDateTime.now();
        ChargingSessionsStartedSummaryDTO startedSummaryDTO = new ChargingSessionsStartedSummaryDTO();
        for (ChargingSession chargingSession : DataHolder.chargingSessions.values()) {
            String chargingSessionInTimePeriod = ChargingSessionUtil.chargingSessionInTimePeriod(chargingSession, now);
            if (chargingSessionInTimePeriod.equalsIgnoreCase(ChargingSessionUtil.ChargingSessionContext.STARTED)
                    || chargingSessionInTimePeriod.equalsIgnoreCase(ChargingSessionUtil.ChargingSessionContext.STARTED_AND_STOPPED)) {
                startedSummaryDTO.setStartedCount(startedSummaryDTO.getStartedCount() + 1);
            }
        }
        return startedSummaryDTO;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String removeStoppedChargingSessions() {
        log.info("Removing stopped sessions");
        // Iterate through all data entries and remove ones which are stopped
        for (Map.Entry<UUID,ChargingSession> uuidChargingSessionEntry : DataHolder.chargingSessions.entrySet()) {
            if (ChargingSessionStatus.STOPPED.equals(uuidChargingSessionEntry.getValue().getStatus())) {
                DataHolder.chargingSessions.remove(uuidChargingSessionEntry.getKey());
            }
        }
        log.info("Stoped sessions removed");
        // In production-scale app this is a proper return value with ControllerAdvice implemented as well
        return "Stopped sessions removed";
    }
}
