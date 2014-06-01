package com.cantwellcode.ipsum.app.connect;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Daniel on 5/8/2014.
 *
 * DATA:
 *
 * leader (ParseUser) - user who created the group
 * members (list of ParseUser) - users who are part of the group, including the leader
 * name (String) - name of the group
 * privacy (String) - open, closed, secret
 */
@ParseClassName("Group")
public class Group extends ParseObject {

    public ParseUser getLeader() { return getParseUser("leader"); }

    public void setLeader(ParseUser leader) {
        put("leader", leader);
        add("members", leader);
    }

    public String getName() { return getString("name"); }

    public void setName(String name) { put("name", name); }

    public String getPrivacy() { return getString("privacy"); }

    public void setOpen() { put("privacy", "open"); }

    public void setClosed() { put("privacy", "closed"); }

    public void setSecret() { put("privacy", "secret"); }

    public List<ParseUser> getMembers() { return getList("members"); }

    public void addMember(ParseUser member) { add("members", member); }

    public void removeMember(ParseUser member) { removeAll("members", Arrays.asList(member));}

    public static ParseQuery<Group> getQuery() {
        return ParseQuery.getQuery(Group.class);
    }
}
