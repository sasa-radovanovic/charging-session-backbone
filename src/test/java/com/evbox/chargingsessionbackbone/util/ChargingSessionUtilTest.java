package com.evbox.chargingsessionbackbone.util;

import com.evbox.chargingsessionbackbone.entity.ChargingSession;
import com.evbox.chargingsessionbackbone.entity.ChargingSessionStatus;
import com.evbox.chargingsessionbackbone.model.NewChargingSessionDTO;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Created by sasaradovanovic on 4/21/19
 */
@RunWith(SpringRunner.class)
public class ChargingSessionUtilTest {

    @Test
    public void chargingSessionFromNewSessionDTO_ValidStationInRequest_MappedAsExpected() {
        NewChargingSessionDTO newChargingSessionDTO = new NewChargingSessionDTO();
        newChargingSessionDTO.setStationId(UUID.randomUUID());

        ChargingSession chargingSession = ChargingSessionUtil.chargingSessionFromNewSessionDTO(newChargingSessionDTO);

        Assert.assertNotNull(chargingSession);
        Assert.assertNotNull(chargingSession.getStartedAt());
        Assert.assertNull(chargingSession.getFinishedAt());
        Assert.assertEquals(ChargingSessionStatus.IN_PROGRESS, chargingSession.getStatus());
        Assert.assertEquals(newChargingSessionDTO.getStationId(), chargingSession.getStationId());
    }

    @Test
    public void chargingSessionInTimePeriod_ChargingSessionStarted_ReturnsStartedContext() {
        LocalDateTime now = LocalDateTime.now();
        ChargingSession chargingSession = new ChargingSession();
        chargingSession.setStatus(ChargingSessionStatus.IN_PROGRESS);
        chargingSession.setStartedAt(now.minusSeconds(55));

        String context = ChargingSessionUtil.chargingSessionInTimePeriod(chargingSession, now);
        Assert.assertNotNull(context);
        Assert.assertEquals(ChargingSessionUtil.ChargingSessionContext.STARTED, context);
    }


    @Test
    public void chargingSessionInTimePeriod_ChargingSessionStopped_ReturnsStoppedContext() {
        LocalDateTime now = LocalDateTime.now();
        ChargingSession chargingSession = new ChargingSession();
        chargingSession.setStatus(ChargingSessionStatus.STOPPED);
        chargingSession.setStartedAt(now.minusHours(1));
        chargingSession.setFinishedAt(now.minusSeconds(35));

        String context = ChargingSessionUtil.chargingSessionInTimePeriod(chargingSession, now);
        Assert.assertNotNull(context);
        Assert.assertEquals(ChargingSessionUtil.ChargingSessionContext.STOPPED, context);
    }


    @Test
    public void chargingSessionInTimePeriod_ChargingSessionStartedAndStopped_ReturnsStartedStoppedContext() {
        LocalDateTime now = LocalDateTime.now();
        ChargingSession chargingSession = new ChargingSession();
        chargingSession.setStatus(ChargingSessionStatus.STOPPED);
        chargingSession.setStartedAt(now.minusSeconds(57));
        chargingSession.setFinishedAt(now.minusSeconds(25));

        String context = ChargingSessionUtil.chargingSessionInTimePeriod(chargingSession, now);
        Assert.assertNotNull(context);
        Assert.assertEquals(ChargingSessionUtil.ChargingSessionContext.STARTED_AND_STOPPED, context);
    }


    @Test
    public void chargingSessionInTimePeriod_ChargingSessionRunning_ReturnsRunningContext() {
        LocalDateTime now = LocalDateTime.now();
        ChargingSession chargingSession = new ChargingSession();
        chargingSession.setStatus(ChargingSessionStatus.IN_PROGRESS);
        chargingSession.setStartedAt(now.minusMinutes(57));
        chargingSession.setFinishedAt(null);

        String context = ChargingSessionUtil.chargingSessionInTimePeriod(chargingSession, now);
        Assert.assertNotNull(context);
        Assert.assertEquals(ChargingSessionUtil.ChargingSessionContext.RUNNING, context);
    }


    @Test
    public void chargingSessionInTimePeriod_ChargingSessionStopped2HrsAgo_ReturnsNoneContext() {
        LocalDateTime now = LocalDateTime.now();
        ChargingSession chargingSession = new ChargingSession();
        chargingSession.setStatus(ChargingSessionStatus.STOPPED);
        chargingSession.setStartedAt(now.minusHours(5));
        chargingSession.setFinishedAt(now.minusHours(2));

        String context = ChargingSessionUtil.chargingSessionInTimePeriod(chargingSession, now);
        Assert.assertNotNull(context);
        Assert.assertEquals(ChargingSessionUtil.ChargingSessionContext.NONE, context);
    }
}
