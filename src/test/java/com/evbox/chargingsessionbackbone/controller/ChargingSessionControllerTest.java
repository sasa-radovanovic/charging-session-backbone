package com.evbox.chargingsessionbackbone.controller;

import com.evbox.chargingsessionbackbone.entity.ChargingSessionStatus;
import com.evbox.chargingsessionbackbone.model.NewChargingSessionDTO;
import com.evbox.chargingsessionbackbone.service.impl.ChargingSessionServiceImpl;
import com.evbox.chargingsessionbackbone.util.DataHolder;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.IntStream;


/**
 * Created by sasaradovanovic on 4/21/19
 */
@RunWith(SpringRunner.class)
@Slf4j
public class ChargingSessionControllerTest {

    private ChargingSessionController chargingSessionController;

    @Before
    public void setUp() {
        DataHolder.chargingSessions = new ConcurrentHashMap<>();
        this.chargingSessionController = new ChargingSessionController(new ChargingSessionServiceImpl());
    }

    @Test
    public void newChargingSession_500ParallelRequests_SavedSessionsAsExpected() throws Exception {

        List<Integer> nums = Arrays.asList(1, 2, 3, 4, 5);

        IntStream.rangeClosed(1, 500).parallel().forEach(num -> {
            NewChargingSessionDTO newChargingSessionDTO = new NewChargingSessionDTO();
            newChargingSessionDTO.setStationId(UUID.randomUUID());
            this.chargingSessionController.newChargingSession(newChargingSessionDTO);
        });

        Assert.assertEquals(500, DataHolder.chargingSessions.values().size());
    }


    @Test
    public void newChargingSession_500ParallelRequestsAnd250Stopped_SavedSessionsAsExpected() throws Exception {

        List<Integer> nums = Arrays.asList(1, 2, 3, 4, 5);

        IntStream.rangeClosed(1, 500).parallel().forEach(num -> {
            NewChargingSessionDTO newChargingSessionDTO = new NewChargingSessionDTO();
            newChargingSessionDTO.setStationId(UUID.randomUUID());
            UUID sessionId = this.chargingSessionController.newChargingSession(newChargingSessionDTO);

            if (num % 2 == 0) {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                this.chargingSessionController.stopChargingSession(sessionId);
            }
        });

        Assert.assertEquals(500, DataHolder.chargingSessions.values().size());
        Assert.assertEquals(250, DataHolder.chargingSessions.values().stream().filter(session -> {
            return session.getStatus().equals(ChargingSessionStatus.STOPPED);
        }).count());
    }



    @Test
    public void newChargingSession_500ParallelRequestsAnd250StoppedAndRemoved_SavedSessionsAsExpected() throws Exception {

        List<Integer> nums = Arrays.asList(1, 2, 3, 4, 5);

        IntStream.rangeClosed(1, 500).parallel().forEach(num -> {
            NewChargingSessionDTO newChargingSessionDTO = new NewChargingSessionDTO();
            newChargingSessionDTO.setStationId(UUID.randomUUID());
            UUID sessionId = this.chargingSessionController.newChargingSession(newChargingSessionDTO);

            if (num % 2 == 0) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                this.chargingSessionController.stopChargingSession(sessionId);
            }
        });

        Assert.assertEquals(500, DataHolder.chargingSessions.values().size());
        Assert.assertEquals(250, DataHolder.chargingSessions.values().stream().filter(session -> {
            return session.getStatus().equals(ChargingSessionStatus.STOPPED);
        }).count());

        this.chargingSessionController.removeStoppedChargingSessions();

        Assert.assertEquals(250, DataHolder.chargingSessions.values().size());
    }

}
