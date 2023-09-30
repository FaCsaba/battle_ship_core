package com.csabapro.core.messaging;

import java.util.HashMap;
import java.util.List;

import com.csabapro.core.game.Vec2;
import com.csabapro.core.result.Result;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@JsonInclude(value = Include.NON_NULL)
public class ServerMessage {
    public ServerMessageType messageType;
    public String sessionId;
    public ErrorMessage errorMessage;
    public Boolean isYourTurn;
    public Long gameTimeInNs;
    public Boolean isWin;
    public Vec2 torpedoPos;
    public Boolean isHit;
    public Vec2 boardSize;
    public List<Integer> shipSizes;
    public Integer maxAllowedHits; // this is usually the sum of ship sizes

    ServerMessage() {}

    public static ServerMessage Ack() {
        ServerMessage sm = new ServerMessage();
        sm.messageType = ServerMessageType.Ack;
        return sm;
    }

    public static ServerMessage GiveSessionId(String sessionId) {
        ServerMessage sm = new ServerMessage();
        sm.sessionId = sessionId;
        sm.messageType = ServerMessageType.GiveSessionId;
        return sm;
    }

    public static ServerMessage GameStart(String sessionId, boolean isYourTurn, Vec2 boardSize, List<Integer> shipSizes, Integer maxAllowedHits) {
        ServerMessage sm = new ServerMessage();
        sm.messageType = ServerMessageType.GameStart;
        sm.sessionId = sessionId;
        sm.isYourTurn = isYourTurn;
        sm.boardSize = boardSize;
        sm.shipSizes = shipSizes;
        sm.maxAllowedHits = maxAllowedHits;
        return sm;
    }

    public static ServerMessage EnemyDisconnected(String sessionId) {
        ServerMessage sm = new ServerMessage();
        sm.messageType = ServerMessageType.EnemyDisconnected;
        sm.sessionId = sessionId;
        return sm;
    }

    public static ServerMessage GameEnd(long gameTime, boolean isWin) {
        ServerMessage sm = new ServerMessage();
        sm.messageType = ServerMessageType.GameEnd;
        sm.gameTimeInNs = gameTime;
        sm.isWin = isWin;
        return sm;
    }

    public static ServerMessage TurnChange(String sessionId, boolean isYourTurn) {
        ServerMessage sm = new ServerMessage();
        sm.messageType = ServerMessageType.TurnChange;
        sm.sessionId = sessionId;
        sm.isYourTurn = isYourTurn;
        return sm;
    }

    public static ServerMessage ReceiveTorpedo(String sessionId, Vec2 torpedoPos) {
        ServerMessage sm = new ServerMessage();
        sm.messageType = ServerMessageType.ReceiveTorpedo;
        sm.sessionId = sessionId;
        sm.torpedoPos = torpedoPos;
        return sm;
    }

    public static ServerMessage SendTorpedoAck(String sessionId, Vec2 torpedoPos, boolean isHit) {
        ServerMessage sm = new ServerMessage();
        sm.messageType = ServerMessageType.SendTorpedoAck;
        sm.sessionId = sessionId;
        sm.torpedoPos = torpedoPos;
        sm.isHit = isHit;
        return sm;
    }

    public String serialize() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            assert false : "Should never fail to serialize ServerMessage object into variable";
            return "";
        }
    }

    public Result<ServerMessage, ErrorMessage> deserialize(String message) {
        ServerMessage sm = null;
        try {
            sm = new ObjectMapper().readerFor(this.getClass()).readValue(message);
        } catch (Exception e) {
            String deserializerMessage = e.getLocalizedMessage();
            HashMap<String, Object> map = new HashMap<>();
            map.put("type", ServerMessage.class.getSimpleName());
            map.put("deserializerMessage", deserializerMessage);
            map.put("input", message);
            ErrorMessage.MalformedMessage(ServerMessage.class.getSimpleName(), deserializerMessage, map);
        }

        return Result.Ok(sm);
    }
}
