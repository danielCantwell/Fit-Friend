package com.cantwellcode.athletejournal;

import android.os.Bundle;

import java.util.List;

/**
 * Created by Daniel on 4/27/2014.
 */
public class ForumPost extends Post {

    public ForumPost(User author, String group, String dateTime, String content, Bundle options) {
        super(author, group, dateTime, content, options);
    }

    public ForumPost(User author, String group, String dateTime, String content, List<User> highFives, List<Comment> discussion, Bundle options) {
        super(author, group, dateTime, content, highFives, discussion, options);
    }
}
