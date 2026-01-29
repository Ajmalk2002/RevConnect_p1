package com.revconnect.dao;

import static org.junit.Assert.*;

import org.junit.Test;

public class ConnectionDaoTest {

    @Test
    public void testConnectionFlow() {

        ConnectionDao dao = new ConnectionDaoImpl();

        boolean sent = dao.sendRequest(1, 2);
        assertTrue(sent);

        boolean accepted = dao.acceptRequest(1, 2);
        assertTrue(accepted);

        boolean removed = dao.removeConnection(1, 2);
        assertTrue(removed);
    }

    @Test
    public void testSendRequestToSelf() {

        ConnectionDao dao = new ConnectionDaoImpl();

        boolean result = dao.sendRequest(1, 1);
        assertFalse(result);
    }
}
