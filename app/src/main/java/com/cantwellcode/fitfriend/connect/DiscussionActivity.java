package com.cantwellcode.fitfriend.connect;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.cantwellcode.fitfriend.R;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class DiscussionActivity extends Activity {

    private Post post;

    private TextView content;
    private TextView name;
    private ListView listView;
    private Button postComment;
    private EditText createComment;

    private ParseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discussion);

        user = ParseUser.getCurrentUser();

        content = (TextView) findViewById(R.id.content);
        name = (TextView) findViewById(R.id.name);
        listView = (ListView) findViewById(R.id.comments);
        postComment = (Button) findViewById(R.id.postComment);
        createComment = (EditText) findViewById(R.id.createComment);

        String postID = getIntent().getStringExtra("postID");
        ParseQuery<Post> query = Post.getQuery();
        query.whereEqualTo("objectId", postID);
        try {
            post = query.getFirst();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        content.setText(post.getContent());
        name.setText(post.getUser().getString("name"));

        CommentsAdapter adapter;
        if (post.has("comments")) {
            adapter = new CommentsAdapter(this, android.R.layout.simple_list_item_1, post.getComments());
            listView.setAdapter(adapter);
        }

        postComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Comment c = new Comment();
                c.setUser(user);
                c.setContent(createComment.getText().toString());
                post.addComment(c);
                post.saveInBackground();
                setResult(RESULT_OK);
                finish();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_discussion, menu);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
