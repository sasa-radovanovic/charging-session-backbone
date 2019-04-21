package com.evbox.chargingsessionbackbone.util;

import com.evbox.chargingsessionbackbone.entity.ChargingSession;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In memory data holder for charging sessions
 */
public final class DataHolder {

    public static ConcurrentHashMap<UUID, ChargingSession> chargingSessions = new ConcurrentHashMap<UUID, ChargingSession>();
}
