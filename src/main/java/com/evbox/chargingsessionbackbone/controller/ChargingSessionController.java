package com.evbox.chargingsessionbackbone.controller;

import com.evbox.chargingsessionbackbone.model.ChargingSessionsStartedSummaryDTO;
import com.evbox.chargingsessionbackbone.model.ChargingSessionsStoppedSummaryDTO;
import com.evbox.chargingsessionbackbone.model.ChargingSessionsSummaryDTO;
import com.evbox.chargingsessionbackbone.model.NewChargingSessionDTO;
import com.evbox.chargingsessionbackbone.service.ChargingSessionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * REST endpoint for charging session manipulation and auditing
 */
@RestController
@Slf4j
@RequestMapping("/")
public class ChargingSessionController {


    private final ChargingSessionService chargingSessionService;

    @Autowired
    public ChargingSessionController(ChargingSessionService chargingSessionService) {
        this.chargingSessionService = chargingSessionService;
    }

    /**
     *
     * Endpoint for creating new charging session
     *
     * @param newChargingSessionDTO - {@link NewChargingSessionDTO} object containing all relevant information for creating new
     *                              charging session
     * @return {@link UUID} id of new session
     */
    @PostMapping(value = "/chargingSession")
    public UUID newChargingSession(@RequestBody NewChargingSessionDTO newChargingSessionDTO) {
        return this.chargingSessionService.submitNewChargingSession(newChargingSessionDTO);
    }

    /**
     *
     * Endpoint for stopping running session
     *
     * @param chargingSessionId - ID of the session to be stopped
     * @return {@link UUID} ID of stopped session
     */
    @PutMapping(value = "/chargingSession/{sessionId}")
    public UUID stopChargingSession(@PathVariable("sessionId") UUID chargingSessionId) {
        return this.chargingSessionService.stopChargingSession(chargingSessionId);
    }

    /**
     * Endpoint for retrieving full summary of the last X minute(s)
     *
     * @return {@link ChargingSessionsSummaryDTO} object containing counts (total, started and stopped)
     */
    @GetMapping(value = "/chargingSessions")
    public ChargingSessionsSummaryDTO getChargingSessionsSummary() {
        return this.chargingSessionService.getChargingSessionsSummary();
    }


    /**
     * Endpoint for retrieving the count of all stopped charging sessions in the last X minute(s)
     *
     * @return {@link ChargingSessionsStoppedSummaryDTO} object containing the count
     */
    @GetMapping(value = "/chargingSessions/stopped")
    public ChargingSessionsStoppedSummaryDTO getStoppedChargingSessionsSummary() {
        return this.chargingSessionService.getStoppedChargingSessionsSummary();
    }


    /**
     * Endpoint for retrieving the count of all started charging sessions in the last X minute(s)
     *
     * @return {@link ChargingSessionsStartedSummaryDTO} object containing the count
     */
    @GetMapping(value = "/chargingSessions/started")
    public ChargingSessionsStartedSummaryDTO getStartedChargingSessionsSummary() {
        return this.chargingSessionService.getStartedChargingSessionsSummary();
    }


    /**
     * Endpoint for removing all stopped charging sessions
     *
     * @return
     */
    @DeleteMapping(value = "/chargingSessions")
    public String removeStoppedChargingSessions() {
        return this.chargingSessionService.removeStoppedChargingSessions();
    }

}
