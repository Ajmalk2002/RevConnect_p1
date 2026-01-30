package com.revconnect.dao;

import java.util.List;

public interface ConnectionDao {

	boolean sendRequest(int fromUser, int toUser);

	boolean acceptRequest(int fromUser, int toUser);

	boolean rejectRequest(int fromUser, int toUser);

	boolean removeConnection(int userId, int otherUser);

	List<String> getPendingRequests(int userId);

	List<Integer> getConnections(int userId);

	List<String> getAllConnections(int userId);
}
