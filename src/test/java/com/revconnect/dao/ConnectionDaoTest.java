package com.revconnect.dao;

import org.junit.Test;

public class ConnectionDaoTest {

 @Test
 public void testConnectionFlow(){
  ConnectionDao dao=new ConnectionDaoImpl();
  dao.sendRequest(1,2);
  dao.acceptRequest(1,2);
  dao.removeConnection(1,2);
 }
}
