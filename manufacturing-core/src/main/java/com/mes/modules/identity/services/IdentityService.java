package com.mes.modules.identity.services;

import java.util.ArrayList;

import com.mes.dom.identity.Group;
import com.mes.dom.identity.User;

public interface IdentityService {
	public ArrayList<User> getAllUsers() throws Exception;
	public User getUserByObjectId(String objectId) throws Exception;
	public ArrayList<Group> getMemberOfByObjectId(String objectId) throws Exception;
	public ArrayList<Group> getAllGroups() throws Exception;
	public Group getGroupByObjectId(String objectId) throws Exception;
	public ArrayList<User> getGroupMembersByObjectId(String objectId) throws Exception;
	public ArrayList<User> filterUsersByFieldValue(String field, String value) throws Exception;
}
