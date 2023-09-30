package com.csabapro;


import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.csabapro.core.game.Orientation;
import com.csabapro.core.game.Ship;
import com.csabapro.core.game.Vec2;
import com.csabapro.core.messaging.ClientMessage;
import com.csabapro.core.messaging.ErrorMessage;
import com.csabapro.core.result.Result;

public class MessageTests {
    @Test
    public void testClientMessageDeserializesCorrectly() {
        ClientMessage cm = ClientMessage.CreateSession(true);
        String createSession = cm.serialize();
        ClientMessage cm2 = ClientMessage.deserialize(createSession).unwrap();
        assertEquals(cm.type, cm2.type);
        assertEquals(cm.isPrivateSession, cm2.isPrivateSession);

        cm = ClientMessage.CreateSession();
        String createSession2 = cm.serialize();
        cm2 = ClientMessage.deserialize(createSession2).unwrap();
        assertEquals(cm.type, cm2.type);

        cm = ClientMessage.FindSession();
        String findSession = cm.serialize();
        cm2 = ClientMessage.deserialize(findSession).unwrap();
        assertEquals(cm.type, cm2.type);

        cm = ClientMessage.JoinSession("abc123");
        String joinSession = cm.serialize();
        cm2 = ClientMessage.deserialize(joinSession).unwrap();
        assertEquals(cm.type, cm2.type);
        assertEquals(cm.sessionId, cm2.sessionId);
        
        cm = ClientMessage.SendTorpedo("abc123", new Vec2(1, 2));
        String sendTorpedo = cm.serialize();
        cm2 = ClientMessage.deserialize(sendTorpedo).unwrap();
        assertEquals(cm.type, cm2.type);
        assertEquals(cm.sessionId, cm2.sessionId);
        assertEquals(cm.torpedoPos, cm2.torpedoPos);

        cm = ClientMessage.PlaceShip("abc123", new Ship(new Vec2(1, 2), Orientation.Vertical, 2));
        String placeShip = cm.serialize();
        Result<ClientMessage, ErrorMessage> a = ClientMessage.deserialize(placeShip);
        cm2 = a.unwrap();
        assertEquals(cm.type, cm2.type);
        assertEquals(cm.ship.getStartPos(), cm2.ship.getStartPos());
        assertEquals(cm.ship.getOrientation(), cm2.ship.getOrientation());
        assertEquals(cm.ship.getSize(), cm2.ship.getSize());
    }
}
