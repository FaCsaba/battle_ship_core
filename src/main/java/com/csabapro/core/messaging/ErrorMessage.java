package com.csabapro.core.messaging;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.csabapro.core.game.Action;
import com.csabapro.core.game.Ship;
import com.csabapro.core.game.Vec2;
import com.csabapro.core.result.Result;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

/**
 * An error message in the following format:
 * <pre>{@code {"The Errors Code":
 *  {
 *      "reason": "text explaining the reason",
 *      "vars": Map<variable_used, its_value>
 *  }
 * }
 * </pre>
 */
@JsonSerialize(using = ErrorSerializer.class)
@JsonDeserialize(using = ErrorDeserializer.class)
@JsonInclude(value = Include.NON_NULL)
public enum ErrorMessage {
    MalformedMessage,
    IncorrectConnectionId,
    TooManyPlayers,
    SessionNotFound,
    PlayerOutOfTurn,
    ActionOutOfTurn,
    ShipOutOfBounds,
    TorpedoOutOfBounds,
    ShipOverlap,
    TorpedoOverlap,
    IncorrectShipSize;

    public String reason;
    public Map<String, Object> vars = new HashMap<>();

    private ErrorMessage() { }

    @JsonValue
    public String getValue() {
        return "MalformedMessage";
    }

    public static ErrorMessage MalformedMessage(String type, String reason, Map<String, Object> vars) {
        ErrorMessage m = MalformedMessage;
        m.reason = "Malformed message with type: `" + type + "`. " + reason;
        m.vars = vars;
        return m;
    }

    public static ErrorMessage IncorrectConnectionId(String connectionId) {
        ErrorMessage m = IncorrectConnectionId;
        m.reason = "Incorrect connectionId: `" + connectionId + "`.";
        m.vars.put("connectionId", connectionId);
        return m;
    }

    public static ErrorMessage TooManyPlayers(String sessionId) {
        ErrorMessage m = TooManyPlayers;
        m.reason = "Attempted to join session with id: `" + sessionId + "`. But game already has enough players.";
        m.vars.put("sessionId", sessionId);
        return m;
    }

    public static ErrorMessage SessionNotFound(String sessionId) {
        ErrorMessage m = SessionNotFound;
        m.reason = "Session with id: `" + sessionId + "` does not exist.";
        m.vars.put("sessionId", sessionId);
        return m;
    }

    public static ErrorMessage PlayerOutOfTurn(String sessionId) {
        ErrorMessage m = PlayerOutOfTurn;
        m.reason = "Attempted to take action in session with id: `" + sessionId + "`. But it isn't your turn.";
        m.vars.put("sessionId", sessionId);
        return m;
    }

    public static ErrorMessage ActionOutOfTurn(String sessionId, Action action) {
        ErrorMessage m = ActionOutOfTurn;
        m.reason = "Attempted to take action in session with id: `" + sessionId + "`. But current action is `" + action + "`.";
        m.vars.put("sessionId", sessionId);
        m.vars.put("action", action);
        return m;
    }

    public static ErrorMessage ShipOutOfBounds(String sessionId, Ship ship, List<Vec2> positions) {
        ErrorMessage m = ShipOutOfBounds;
        ObjectMapper oMapper = new ObjectMapper();
        String shipString = "";
        String posString = "";
        try {
            shipString = oMapper.writeValueAsString(ship);
            posString = oMapper.writeValueAsString(positions);
        } catch (Exception e) {
            assert false : "Should be able to stringify ship and position";
        } 
        m.reason = "In session with id: `" + sessionId + "`. Attempted to place ship: `" + shipString + "`. But positions: `" + posString + "` are out of bounds.";  
        m.vars.put("sessionId", sessionId);
        m.vars.put("ship", ship);
        m.vars.put("positions", positions);
        return m;
    }

    public static ErrorMessage TorpedoOutOfBounds(String sessionId, Vec2 torpedo) {
        ErrorMessage m = TorpedoOutOfBounds;
        ObjectMapper oMapper = new ObjectMapper();
        String torpedoPos = "";
        try {
            torpedoPos = oMapper.writeValueAsString(torpedo);
        } catch (Exception e) {
            assert false : "Should be able to stringify torpedo position";
        } 
        m.reason = "In session with id: `" + sessionId + "`. Attempted to place torpedo. But position: `" + torpedoPos + "` is out of bounds";  
        m.vars.put("sessionId", sessionId);
        m.vars.put("position", torpedoPos);
        return m;
    }

    public static ErrorMessage ShipOverlap(String sessionId, Ship ship, List<Vec2> positions) {
        ErrorMessage m = ShipOverlap;
        ObjectMapper oMapper = new ObjectMapper();
        String shipString = "";
        String posString = "";
        try {
            shipString = oMapper.writeValueAsString(ship);
            posString = oMapper.writeValueAsString(positions);
        } catch (Exception e) {
            assert false : "Should be able to stringify ship and position";
        } 
        m.reason = "In session with id: `" + sessionId + "`. Attempted to place ship: `" + shipString + "`. But positions: " + posString + "` are out of bounds";
        m.vars.put("sessionId", sessionId);
        m.vars.put("ship", ship);
        m.vars.put("positions", positions);
        return m;
    }

    public static ErrorMessage TorpedoOverlap(String sessionId, Vec2 position) {
        ErrorMessage m = TorpedoOverlap;
        ObjectMapper oMapper = new ObjectMapper();
        String posString = "";
        try {
            posString = oMapper.writeValueAsString(position);
        } catch (Exception e) {
            assert false : "Should be able to stringify ship and position";
        } 
        m.reason = "In session with id: `" + sessionId + "`. Attempted to place torpedo. But position: `" + posString + "` is out of bounds";
        m.vars.put("sessionId", sessionId);
        m.vars.put("position", position);
        return m;
    }

    public static ErrorMessage IncorrectShipSize(String sessionId, Ship ship) {
        ErrorMessage m = IncorrectShipSize;
        m.reason = "In session with id: `" + sessionId + "`. Attempted to place ship with incorrect size. Size: `" + ship.getSize() + "`.";
        m.vars.put("sessionId", sessionId);
        m.vars.put("ship", ship);
        return m;
    }

    public String serialize() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            assert false;
            return "";
        }
    }

    public static Result<ErrorMessage, ErrorMessage> deserialize(String input) {
        try {
            ErrorMessage ok = new ObjectMapper().readerFor(ErrorMessage.class).readValue(input);
            return Result.Ok(ok);
        } catch (Exception e) {
            String reason = "Error occured while trying to deserialize ErrorMessage: " + e.getLocalizedMessage();
            HashMap<String, Object> vars = new HashMap<>();
            vars.put("type", "ErrorMessage");
            vars.put("deserializerMessage", e.getLocalizedMessage());
            vars.put("input", input);
            ErrorMessage err = ErrorMessage.MalformedMessage("ErrorMessage", reason, vars);
            return Result.Err(err);
        }
    }
}

class ErrorSerializer extends StdSerializer<ErrorMessage> {

    public ErrorSerializer() {
        super(ErrorMessage.class);
    }

    public ErrorSerializer(Class<ErrorMessage> t) {
        super(t);
    }

    @Override
    public void serialize(
            ErrorMessage error, JsonGenerator generator, SerializerProvider provider)
            throws IOException, JsonProcessingException {
        generator.writeStartObject();
        generator.writeFieldName(error.name());
        generator.writeStartObject();
        generator.writeFieldName("reason");
        generator.writeString(error.reason);
        generator.writeFieldName("vars");
        generator.writeObject(error.vars);
        generator.writeEndObject();
        generator.writeEndObject();
    }
}

class ErrorDeserializer extends StdDeserializer<ErrorMessage> {

    public ErrorDeserializer() {
        super(ErrorMessage.class);
    }

    protected ErrorDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public ErrorMessage deserialize(JsonParser jsonParser, DeserializationContext ctxt)
            throws IOException {
        ObjectMapper mapper = (ObjectMapper) jsonParser.getCodec();
        ObjectNode node = mapper.readTree(jsonParser);

        String errTypeString = node.fieldNames().next();
        for (ErrorMessage errType : ErrorMessage.values()) {
            if (!errType.name().equals(errTypeString))
                continue;
            errType.reason = node.get(errTypeString).get("reason").asText();
            errType.vars = mapper.readerFor(errType.vars.getClass())
                    .readValue(node.get(errTypeString).get("vars").toString());
            return errType;
        }

        ctxt.handleWeirdKey(String.class, errTypeString, "%s is not in ServerError", errTypeString);
        return null;
    }
}
