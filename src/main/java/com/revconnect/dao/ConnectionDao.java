package com.revconnect.dao;

import java.util.List;

public interface ConnectionDao {
    void sendRequest(int fromUser, int toUser);
    void acceptRequest(int fromUser, int toUser);
    void rejectRequest(int fromUser, int toUser);
    List<Integer> getPendingRequests(int userId);
    List<Integer> getConnections(int userId);
    void removeConnection(int userId, int otherUser);
}
