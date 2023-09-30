package com.csabapro.core.messaging;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * A Message sent by the client to the server in a JSON format
 */
public enum ClientMessageType {
    /** 
     * Client is asking to create a session 
     * <p>
     * <b>fields:</b>
     * <ul>
     *     <li><span style="color: #0022ee">isPublicSession</span>?: boolean = false</li>
     * </ul>
     *
     */
    CreateSession,
    /** 
     * Client is asking for available (non Private and a match isn't on-going) sessions 
     * <p>
     * <b>fields:</b>
     * <ul>
     * </ul>
     */
    FindSession,
    /** 
     * Client wants to join an available session 
     * <p>
     * <b>fields:</b>
     * <ul>
     *  <li><span style="color: #0022ee">sessionId</span>: string</li>
     * </ul>
     */
    JoinSession,
    
    /** Client sent a torpedo
     * <p>
     * <b>fields:</b>
     * <ul>
     *  <li><span style="color: #0022ee">sessionId</span>: string</li>
     *  <li><span style="color: #0022ee">torpedoPos</span>: {@see Vec2}</li>
     * </ul>
     */
    SendTorpedo,
    /** Client placed a ship
     * <p>
     * <b>fields:</b>
     * <ul>
     *  <li><span style="color: #0022ee">sessionId</span>: string</li>
     *  <li><span style="color: #0022ee">ship</span>: {@see Ship}</li>
     * </ul>
     */
    PlaceShip,
    ;

    private static int ClientMessageTypeCount = 0;
    private int value;

    private int inc() {
        return ClientMessageTypeCount++;
    }

    private ClientMessageType() {
        this.value = inc();
    }

    @JsonValue
    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        if (this == CreateSession) return "CreateSession";
        if (this == FindSession) return "FindSession";
        if (this == JoinSession) return "JoinSession";
        if (this == PlaceShip) return "PlaceShip";
        if (this == SendTorpedo) return "SendTorpedo";
        assert false : "unreachable"; // We should never get here
        return "";
    }
}