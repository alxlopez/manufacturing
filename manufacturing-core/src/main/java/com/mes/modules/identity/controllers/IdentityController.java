package com.mes.modules.identity.controllers;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.mes.dom.identity.Group;
import com.mes.dom.identity.User;
import com.mes.dom.identity.UserList;
import com.mes.modules.identity.services.IdentityService;


@RestController
public class IdentityController {
	@Autowired
	private IdentityService identitiService;

 	@RequestMapping(value = "/mes/identity/users", method = RequestMethod.GET)
    public @ResponseBody ResponseEntity<ArrayList<User>> users() {
    	try {
			ArrayList<User> users = identitiService.getAllUsers();
			return ResponseEntity.ok(users);
    	} catch (Exception e) {
    		e.printStackTrace();
    		return null;
    	}
    }

 	@RequestMapping(value = "/mes/identity/userList", method = RequestMethod.GET)
    public @ResponseBody ResponseEntity<UserList> userList() {
    	try {
    		UserList userList = new UserList();
			ArrayList<User> users = identitiService.getAllUsers();
			userList.setData(users);
			return ResponseEntity.ok(userList);
    	} catch (Exception e){
    		e.printStackTrace();
    		return null;
    	}
    }

 	@RequestMapping(value = "/mes/identity/groups", method = RequestMethod.GET)
    public @ResponseBody ResponseEntity<ArrayList<Group>> groups() {
    	try {
			ArrayList<Group> groups = identitiService.getAllGroups();
			return ResponseEntity.ok(groups);
    	} catch (Exception e){
    		e.printStackTrace();
    		return null;
    	}
    }
    

 	// Get user properties from Tenant by user id
 	@RequestMapping(value = "/mes/identity/user/{id}", method = RequestMethod.GET)
    public @ResponseBody ResponseEntity<User> user(
    		@PathVariable(value="id") String objectId
    ) {
 		try {
			User user = identitiService.getUserByObjectId(objectId);
			return ResponseEntity.ok(user);
    	} catch (Exception e){
    		e.printStackTrace();
    		return null;
    	}
    }

 	@RequestMapping(value = "/mes/identity/group/{id}", method = RequestMethod.GET)
    public @ResponseBody ResponseEntity<Group> group(
    		@PathVariable(value="id") String objectId
    ) {
    	try {
			Group group = identitiService.getGroupByObjectId(objectId);
			return ResponseEntity.ok(group);
    	} catch (Exception e){
    		e.printStackTrace();
    		return null;
    	}
    }

 	@RequestMapping(value = "/mes/identity/group/{id}/members", method = RequestMethod.GET)
    public @ResponseBody ResponseEntity<ArrayList<User>> groupMembers(
    		@PathVariable(value="id") String objectId
    ) {
 		try {
			ArrayList<User> users = identitiService.getGroupMembersByObjectId(objectId);
			return ResponseEntity.ok(users);
    	} catch (Exception e){
    		e.printStackTrace();
    		return null;
    	}
    }

  	@RequestMapping(value = "/mes/identity/user/{id}/memberOf", method = RequestMethod.GET)
    public @ResponseBody ResponseEntity<ArrayList<Group>> userMemberOf(
    		@PathVariable(value="id") String objectId
    ) {
    	try {
			ArrayList<Group> groups = identitiService.getMemberOfByObjectId(objectId);
			return ResponseEntity.ok(groups);
    	} catch (Exception e){
    		e.printStackTrace();
    		return null;
    	}
    }
  	
 	// Get user properties from Tenant by user id
 	@RequestMapping(value = "/mes/identity/user", method = RequestMethod.GET)
    public @ResponseBody ResponseEntity<User> userByCode(
    		@RequestParam(value = "code", required = true) String code
	) {
 		try {
    		//ArrayList<User> users = identitiService.filterUsersByFieldValue("extension_81673501429c4f69a046bc11e9c5fee2_code",code);
 			ArrayList<User> users = identitiService.filterUsersByFieldValue("extension_0f54a822292d42ccaa368a3c62b939de_code",code);
    		User user = users.get(0);
			return ResponseEntity.ok(user);
    	} catch (Exception e) {
    		e.printStackTrace();
    		return null;
    	}
    }
}

