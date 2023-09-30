package com.csabapro.core.messaging;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.csabapro.core.game.Ship;
import com.csabapro.core.game.Vec2;
import com.csabapro.core.result.Result;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@JsonInclude(value = Include.NON_NULL)
public class ClientMessage {
    /** Type of the message sent to the server. This field is required */
    public ClientMessageType type;
    // /** Identifier of the connection, a random UUID that. This field is required */
    // public String connectionId;
    /**
     * Identifier of the game session between two players.
     * This field is only required for JoinSession and game related messages.
     */
    public String sessionId = null;
    /** Should the initiated session be a private session */
    public Boolean isPrivateSession = false;

    /**
     * Location of the torpedo to place.
     * This field is required in SendTorpedo
     */
    public Vec2 torpedoPos = null;
    /**
     * Ship to place.
     * This field is required in PlaceShip.
     */
    public Ship ship = null;

    private static String malformedError(ClientMessageType type, String missing) {
        return "Malformed ClientMessage with type: " + type.toString() + " did not have `" + missing
                + "`, but it is required.";
    }

    public static ClientMessage CreateSession() {
        ClientMessage cm = new ClientMessage();
        cm.type = ClientMessageType.CreateSession;
        return cm;
    }

    public static ClientMessage CreateSession(boolean isPrivateSession) {
        ClientMessage cm = new ClientMessage();
        cm.type = ClientMessageType.CreateSession;
        cm.isPrivateSession = isPrivateSession;
        return cm;
    }

    public static ClientMessage FindSession() {
        ClientMessage cm =  new ClientMessage();
        cm.type = ClientMessageType.FindSession;
        return cm;
    }

    public static ClientMessage JoinSession(String sessionId) {
        ClientMessage cm = new ClientMessage();
        cm.type = ClientMessageType.JoinSession;
        cm.sessionId = sessionId;
        return cm;
    }

    public static ClientMessage SendTorpedo(String sessionId, Vec2 torpedoPos) {
        ClientMessage cm = new ClientMessage();
        cm.type = ClientMessageType.SendTorpedo;
        cm.sessionId = sessionId;
        cm.torpedoPos = torpedoPos;
        return cm;
    }

    public static ClientMessage PlaceShip(String sessionId, Ship ship) {
        ClientMessage cm = new ClientMessage();
        cm.type = ClientMessageType.PlaceShip;
        cm.sessionId = sessionId;
        cm.ship = ship;
        return cm;
    }

    public static Result<ClientMessage, ErrorMessage> deserialize(String input) {
        ClientMessage cm = null;
        try {
            cm = new ObjectMapper().readerFor(ClientMessage.class).readValue(input);
        } catch (Exception e) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("type", ClientMessage.class.getSimpleName());
            map.put("deserializerMessage", e.getLocalizedMessage());
            map.put("input", input);
            ErrorMessage em = ErrorMessage.MalformedMessage(ClientMessage.class.getSimpleName(),
                    e.getLocalizedMessage(), map);
            return Result.Err(em);
        }

        List<String> missing = new ArrayList<String>();
        switch (cm.type) {
            case FindSession:
                // if (cm.connectionId == null)
                //     missing.add("connectionId");
                break;

            case CreateSession:
                // if (cm.connectionId == null)
                //     missing.add("connectionId");
                if (cm.isPrivateSession == null)
                    cm.isPrivateSession = false;
                break;

            case JoinSession:
                // if (cm.connectionId == null)
                //     missing.add("connectionId");
                if (cm.sessionId == null)
                    missing.add("sessionId");
                break;

            case SendTorpedo:
                // if (cm.connectionId == null)
                //     missing.add("connectionId");
                if (cm.sessionId == null)
                    missing.add("sessionId");
                if (cm.torpedoPos == null)
                    missing.add("torpedoPos");
                break;

            case PlaceShip:
                // if (cm.connectionId == null)
                //     missing.add("connectionId");
                if (cm.sessionId == null)
                    missing.add("sessionId");
                if (cm.ship == null)
                    missing.add("ship");
                break;

            default:
                assert false : "Unreachable"; // we should never get here
                break;
        }
        if (missing.size() > 0) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("type", ClientMessage.class.getSimpleName());
            String deserializerMessage = malformedError(cm.type, String.join(", ", missing));
            map.put("deserializerMessage", deserializerMessage);
            map.put("input", input);
            ErrorMessage em = ErrorMessage.MalformedMessage(ClientMessage.class.getSimpleName(), deserializerMessage,
                    map);
            return Result.Err(em);
        }

        return Result.Ok(cm);
    }

    public String serialize() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            assert false : "Should never fail to serialize ClientMessage object into variable";
            return "";
        }
    }

    @Override
    public String toString() {
        return serialize();
    }
}
