package com.cantwellcode.athletejournal;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

/**
 * Created by Daniel on 5/8/2014.
 *
 * DATA:
 *
 * leader - user who created the group
 * members - users who are part of the group, including the leader
 * name - name of the group
 */
@ParseClassName("Group")
public class Group extends ParseObject {

    public ParseUser getLeader() { return getParseUser("leader"); }

    public void setLeader(ParseUser leader) { put("leader", leader); }

    public String getName() { return getString("name"); }

    public void setName(String name) { put("name", name); }

    public List<ParseUser> getMembers() { return getList("members"); }

    public static ParseQuery<Group> getQuery() {
        return ParseQuery.getQuery(Group.class);
    }
}
