package com.csabapro.core.messaging;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * This is the type of the message that the server sends to the client
 */
public enum ServerMessageType {
    /** 
     * Acknowledge that one of the following actions occured:
     * <ul>
     *  <li>The user is now connected via websocket.</li>
     *  <li>The user put a ship down via <code>{@link ClientMessageType}.PlaceShip</code></li>
     * </ul>
     */
    Ack,
    /**
     * The server sends out a sessionId<p>
     */
    GiveSessionId,
    GameStart,
    GameEnd,
    EnemyDisconnected,

    TurnChange,
    SendTorpedoAck,
    ReceiveTorpedo;

    public static int ServerMessageTypeCount = 0; 
    private int value;

    private int inc() {
        return ServerMessageTypeCount++;
    }

    private ServerMessageType() {
        this.value = this.inc();
    }

    @JsonValue
    public int getValue() {
        return value;
    }
}
