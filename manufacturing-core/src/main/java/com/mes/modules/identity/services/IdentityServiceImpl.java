package com.mes.modules.identity.services;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mes.config.apigraph.ApiGraphConnection;
import com.mes.config.apigraph.HttpClientHelper;
import com.mes.config.apigraph.JSONHelper;
import com.mes.dom.identity.Group;
import com.mes.dom.identity.User;

@Service("identityService")
public class IdentityServiceImpl implements IdentityService{
	@Autowired
	ApiGraphConnection graphConnect;
	
	private Map<String,String> getGraphParameters() {
		Map<String,String> map = new HashMap<String,String>();
		map.put("accessToken", graphConnect.getAccessToken());
    	map.put("tenant", graphConnect.getTenant());
    	map.put("apiGraphUrl", graphConnect.getApiGraphUrl());
    	map.put("apiVersion", graphConnect.getApiVersion());
		return map;
	}
	
	private JSONObject getQueryGraph(URL url, String accessToken) throws IOException, JSONException {
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        //conn.setRequestProperty("api-version", apiVersion);
		System.out.println("token: "+accessToken);
		conn.setRequestMethod("GET");
        conn.setRequestProperty("Authorization", "Bearer "+accessToken);
        //conn.setRequestProperty("Accept", "application/json;odata=minimalmetadata");
        conn.setRequestProperty("Accept", "application/json");
        String goodRespStr = HttpClientHelper.getResponseStringFromConn(conn, true);
        int responseCode = conn.getResponseCode();
        JSONObject response = HttpClientHelper.processGoodRespStr(responseCode, goodRespStr);
        return response;
	}

    public ArrayList<User> getAllUsers() throws Exception {
    	Map<String,String> map = getGraphParameters();
        //URL url = new URL(String.format("%s/%s/users", map.get("apiGraphUrl"), map.get("tenant")));
    	URL url = new URL(String.format("%s/%s/users", map.get("apiGraphUrl"), map.get("apiVersion")));
        JSONObject response = getQueryGraph(url, map.get("accessToken"));
        JSONArray users = new JSONArray();
        users = JSONHelper.fetchDirectoryObjectJSONArray(response);
        User user;
        ArrayList<User> userList = new ArrayList<User>();
        for (int i = 0; i < users.length(); i++) {
            JSONObject thisUserJSONObject = users.optJSONObject(i);
            user = new User();
            JSONHelper.convertJSONObjectToDirectoryObject(thisUserJSONObject, user);
            userList.add(user);
        }
        return userList;
    }
    
    public User getUserByObjectId(String objectId) throws Exception {
    	Map<String,String> map = getGraphParameters();
        //URL url = new URL(String.format("%s/%s/users/%s", map.get("apiGraphUrl"), map.get("tenant"), objectId));
    	URL url = new URL(String.format("%s/%s/users/%s", map.get("apiGraphUrl"), map.get("apiVersion"), objectId));
        JSONObject response = getQueryGraph(url, map.get("accessToken"));
        JSONObject jsonObject = new JSONObject();
        jsonObject = JSONHelper.fetchDirectoryObjectJSONObject(response);
        User user = new User();
        JSONHelper.convertJSONObjectToDirectoryObject(jsonObject, user);
        return user;
    }
    
    public ArrayList<Group> getMemberOfByObjectId(String objectId) throws Exception {
    	Map<String,String> map = getGraphParameters();
        //URL url = new URL(String.format("%s/%s/users/%s/memberOf", map.get("apiGraphUrl"), map.get("tenant"), objectId));
    	URL url = new URL(String.format("%s/%s/users/%s/memberOf", map.get("apiGraphUrl"), map.get("apiVersion"), objectId));
        JSONObject response = getQueryGraph(url, map.get("accessToken"));
        JSONArray groups = new JSONArray();
        groups = JSONHelper.fetchDirectoryObjectJSONArray(response);
        Group group;
        ArrayList<Group> groupList = new ArrayList<Group>();
        for (int i = 0; i < groups.length(); i++) {
            JSONObject thisUserJSONObject = groups.optJSONObject(i);
            group = new Group();
            JSONHelper.convertJSONObjectToDirectoryObject(thisUserJSONObject, group);
            groupList.add(group);
        }
        return groupList;
    }

    public ArrayList<Group> getAllGroups() throws Exception {
    	Map<String,String> map = getGraphParameters();
        //URL url = new URL(String.format("%s/%s/groups",map.get("apiGraphUrl"), map.get("tenant")));
    	URL url = new URL(String.format("%s/%s/groups",map.get("apiGraphUrl"), map.get("apiVersion")));
        JSONObject response = getQueryGraph(url, map.get("accessToken"));
        JSONArray groups = new JSONArray();
        groups = JSONHelper.fetchDirectoryObjectJSONArray(response);
        Group group;
        ArrayList<Group> groupList = new ArrayList<Group>();
        for (int i = 0; i < groups.length(); i++) {
            JSONObject thisUserJSONObject = groups.optJSONObject(i);
            group = new Group();
            JSONHelper.convertJSONObjectToDirectoryObject(thisUserJSONObject, group);
            groupList.add(group);
        }
        return groupList;
    }
    
    public Group getGroupByObjectId(String objectId) throws Exception {
    	Map<String,String> map = getGraphParameters();
        //URL url = new URL(String.format("%s/%s/groups/%s", map.get("apiGraphUrl"), map.get("tenant"), objectId));
    	URL url = new URL(String.format("%s/%s/groups/%s", map.get("apiGraphUrl"), map.get("apiVersion"), objectId));
        JSONObject response = getQueryGraph(url, map.get("accessToken"));
        JSONObject jsonObject = new JSONObject();
        jsonObject = JSONHelper.fetchDirectoryObjectJSONObject(response);
        Group group = new Group();
        JSONHelper.convertJSONObjectToDirectoryObject(jsonObject, group);
        return group;
    }
    
    public ArrayList<User> getGroupMembersByObjectId(String objectId) throws Exception {
    	Map<String,String> map = getGraphParameters();
    	//URL url = new URL(String.format("%s/%s/groups/%s/members", map.get("apiGraphUrl"), map.get("tenant"),objectId));
    	URL url = new URL(String.format("%s/%s/groups/%s/members", map.get("apiGraphUrl"), map.get("apiVersion"),objectId));
        JSONObject response = getQueryGraph(url, map.get("accessToken"));
        JSONArray users = new JSONArray();
        users = JSONHelper.fetchDirectoryObjectJSONArray(response);
        User user;
        ArrayList<User> userList = new ArrayList<User>();
        for (int i = 0; i < users.length(); i++) {
            JSONObject thisUserJSONObject = users.optJSONObject(i);
            user = new User();
            JSONHelper.convertJSONObjectToDirectoryObject(thisUserJSONObject, user);
            userList.add(user);
        }
        return userList;
    }
	
    
	public ArrayList<User> filterUsersByFieldValue(String field, String value) throws Exception {
		Map<String,String> map = getGraphParameters();
        //URL url = new URL(map.get("apiGraphUrl")+"/"+map.get("tenant")+"/users?$filter="+field+"%20eq%20'"+value+"'");
		
		//URL url = new URL(map.get("apiGraphUrl")+"/"+map.get("apiVersion")+"/users?$filter="+field+" eq '"+value+"'");
		URL url = new URL(String.format("%s/%s/users%s", map.get("apiGraphUrl"), map.get("apiVersion"),"?$filter="+field+"%20eq%20'"+value+"'"));
		
		JSONObject response = getQueryGraph(url, map.get("accessToken"));
        JSONArray users = new JSONArray();
        users = JSONHelper.fetchDirectoryObjectJSONArray(response);
        User user;
        ArrayList<User> userList = new ArrayList<User>();
        for (int i = 0; i < users.length(); i++) {
            JSONObject thisUserJSONObject = users.optJSONObject(i);
            user = new User();
            JSONHelper.convertJSONObjectToDirectoryObject(thisUserJSONObject, user);
            userList.add(user);
        }
        return userList;
	}
}
