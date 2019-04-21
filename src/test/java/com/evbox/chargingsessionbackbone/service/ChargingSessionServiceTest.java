package com.evbox.chargingsessionbackbone.service;

import com.evbox.chargingsessionbackbone.entity.ChargingSessionStatus;
import com.evbox.chargingsessionbackbone.model.ChargingSessionsStartedSummaryDTO;
import com.evbox.chargingsessionbackbone.model.ChargingSessionsStoppedSummaryDTO;
import com.evbox.chargingsessionbackbone.model.ChargingSessionsSummaryDTO;
import com.evbox.chargingsessionbackbone.model.NewChargingSessionDTO;
import com.evbox.chargingsessionbackbone.service.impl.ChargingSessionServiceImpl;
import com.evbox.chargingsessionbackbone.util.DataHolder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by sasaradovanovic on 4/21/19
 */
@RunWith(SpringRunner.class)
public class ChargingSessionServiceTest {

    private ChargingSessionService chargingSessionService;

    @Before
    public void setUp() {
        DataHolder.chargingSessions = new ConcurrentHashMap<>();
        chargingSessionService = new ChargingSessionServiceImpl();
    }

    @Test
    public void submitNewChargingSession_NewSessionSubmitted_AddedToTheDataHolder() {
        NewChargingSessionDTO newChargingSessionDTO = new NewChargingSessionDTO();
        newChargingSessionDTO.setStationId(UUID.randomUUID());
        this.chargingSessionService.submitNewChargingSession(newChargingSessionDTO);
        Assert.assertEquals(1, DataHolder.chargingSessions.values().size());
    }


    @Test
    public void stopChargingSession_ValidId_SessionIsStopped() {

        NewChargingSessionDTO newChargingSessionDTO = new NewChargingSessionDTO();
        newChargingSessionDTO.setStationId(UUID.randomUUID());
        UUID sessionId = this.chargingSessionService.submitNewChargingSession(newChargingSessionDTO);
        Assert.assertEquals(1, DataHolder.chargingSessions.values().size());

        this.chargingSessionService.stopChargingSession(sessionId);
        Assert.assertEquals(1, DataHolder.chargingSessions.values().stream().filter(session -> {
            return session.getStatus().equals(ChargingSessionStatus.STOPPED);
        }).count());
    }


    @Test
    public void stopChargingSession_UnknownId_ReturnsNull() {
        Assert.assertNull(this.chargingSessionService.stopChargingSession(UUID.randomUUID()));
    }



    @Test
    public void stopChargingSession_SessionAlreadyStopped_ReturnsNull() {

        NewChargingSessionDTO newChargingSessionDTO = new NewChargingSessionDTO();
        newChargingSessionDTO.setStationId(UUID.randomUUID());
        UUID sessionId = this.chargingSessionService.submitNewChargingSession(newChargingSessionDTO);
        Assert.assertEquals(1, DataHolder.chargingSessions.values().size());

        this.chargingSessionService.stopChargingSession(sessionId);
        Assert.assertEquals(1, DataHolder.chargingSessions.values().stream().filter(session -> {
            return session.getStatus().equals(ChargingSessionStatus.STOPPED);
        }).count());

        UUID result = this.chargingSessionService.stopChargingSession(sessionId);
        Assert.assertNull(result);
    }


    @Test
    public void getChargingSessionsSummary_TwoSessionsOneStopped_ReturnsCorrectCount() {
        NewChargingSessionDTO newChargingSessionDTO = new NewChargingSessionDTO();
        newChargingSessionDTO.setStationId(UUID.randomUUID());
        UUID sessionId = this.chargingSessionService.submitNewChargingSession(newChargingSessionDTO);
        Assert.assertEquals(1, DataHolder.chargingSessions.values().size());

        this.chargingSessionService.stopChargingSession(sessionId);
        Assert.assertEquals(1, DataHolder.chargingSessions.values().stream().filter(session -> {
            return session.getStatus().equals(ChargingSessionStatus.STOPPED);
        }).count());

        NewChargingSessionDTO newChargingSessionDTO2 = new NewChargingSessionDTO();
        newChargingSessionDTO.setStationId(UUID.randomUUID());
        this.chargingSessionService.submitNewChargingSession(newChargingSessionDTO2);
        Assert.assertEquals(2, DataHolder.chargingSessions.values().size());

        ChargingSessionsSummaryDTO sessionsSummaryDTO = this.chargingSessionService.getChargingSessionsSummary();
        Assert.assertNotNull(sessionsSummaryDTO);
        Assert.assertEquals(2, sessionsSummaryDTO.getTotalCount());
        Assert.assertEquals(2, sessionsSummaryDTO.getStartedCount());
        Assert.assertEquals(1, sessionsSummaryDTO.getStoppedCount());
    }


    @Test
    public void getStoppedChargingSessionsSummary_TwoSessionsOneStopped_ReturnsCorrectCount() {
        NewChargingSessionDTO newChargingSessionDTO = new NewChargingSessionDTO();
        newChargingSessionDTO.setStationId(UUID.randomUUID());
        UUID sessionId = this.chargingSessionService.submitNewChargingSession(newChargingSessionDTO);
        Assert.assertEquals(1, DataHolder.chargingSessions.values().size());

        this.chargingSessionService.stopChargingSession(sessionId);
        Assert.assertEquals(1, DataHolder.chargingSessions.values().stream().filter(session -> {
            return session.getStatus().equals(ChargingSessionStatus.STOPPED);
        }).count());

        NewChargingSessionDTO newChargingSessionDTO2 = new NewChargingSessionDTO();
        newChargingSessionDTO.setStationId(UUID.randomUUID());
        this.chargingSessionService.submitNewChargingSession(newChargingSessionDTO2);
        Assert.assertEquals(2, DataHolder.chargingSessions.values().size());

        ChargingSessionsStoppedSummaryDTO sessionsSummaryDTO = this.chargingSessionService.getStoppedChargingSessionsSummary();
        Assert.assertNotNull(sessionsSummaryDTO);
        Assert.assertEquals(1, sessionsSummaryDTO.getStoppedCount());
    }


    @Test
    public void getStartedChargingSessionsSummary_TwoSessionsOneStopped_ReturnsCorrectCount() {
        NewChargingSessionDTO newChargingSessionDTO = new NewChargingSessionDTO();
        newChargingSessionDTO.setStationId(UUID.randomUUID());
        UUID sessionId = this.chargingSessionService.submitNewChargingSession(newChargingSessionDTO);
        Assert.assertEquals(1, DataHolder.chargingSessions.values().size());

        this.chargingSessionService.stopChargingSession(sessionId);
        Assert.assertEquals(1, DataHolder.chargingSessions.values().stream().filter(session -> {
            return session.getStatus().equals(ChargingSessionStatus.STOPPED);
        }).count());

        NewChargingSessionDTO newChargingSessionDTO2 = new NewChargingSessionDTO();
        newChargingSessionDTO.setStationId(UUID.randomUUID());
        this.chargingSessionService.submitNewChargingSession(newChargingSessionDTO2);
        Assert.assertEquals(2, DataHolder.chargingSessions.values().size());

        ChargingSessionsStartedSummaryDTO sessionsSummaryDTO = this.chargingSessionService.getStartedChargingSessionsSummary();
        Assert.assertNotNull(sessionsSummaryDTO);
        Assert.assertEquals(2, sessionsSummaryDTO.getStartedCount());
    }



    @Test
    public void removeStoppedChargingSessions_HalfOfTheSessionsStopped_RemovesAllStoppedSessions() {
        List<UUID> toStop = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            NewChargingSessionDTO newChargingSessionDTO = new NewChargingSessionDTO();
            newChargingSessionDTO.setStationId(UUID.randomUUID());
            UUID sessionId = this.chargingSessionService.submitNewChargingSession(newChargingSessionDTO);
            if (i % 2 == 0) {
                toStop.add(sessionId);
            }
        }
        for (UUID uuid : toStop) {
            this.chargingSessionService.stopChargingSession(uuid);
        }
        Assert.assertEquals(50, DataHolder.chargingSessions.values().size());
        this.chargingSessionService.removeStoppedChargingSessions();
        Assert.assertEquals(25, DataHolder.chargingSessions.values().size());
    }

}
