package com.cantwellcode.fitfriend.connect;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.cantwellcode.fitfriend.R;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class CreatePostActivity extends Activity {

    private EditText content;
    private EditText comment;

    private String currentCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        content = (EditText) findViewById(R.id.content);
        comment = (EditText) findViewById(R.id.comment);

        currentCategory = getIntent().getStringExtra("category");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_post, menu);
        getActionBar().setDisplayShowTitleEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setTitle("Post in " + getIntent().getStringExtra("category"));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_post:
                if (isEmpty(content)) {
                    Toast.makeText(this, "Post not created.\nTitle required.", Toast.LENGTH_LONG).show();
                } else {
                        /* Create new post */
                    ParseUser user = ParseUser.getCurrentUser();
                    Post post = new Post();
                    post.setUser(user);
                    post.setContent(content.getText().toString());
                    post.setCategory(currentCategory);
                        /* Add comment if exists */
                    if (!isEmpty(comment)) {
                        Comment c = new Comment();
                        c.setUser(user);
                        c.setContent(comment.getText().toString());
                        post.addComment(c);
                    }
                    post.setHighFives(0);
                        /* Save post */
                    post.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            setResult(RESULT_OK);
                            finish();
                        }
                    });
                }
                break;
            case android.R.id.home:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean isEmpty(EditText etText) {
        if (etText.getText().toString().trim().length() > 0) {
            return false;
        } else {
            return true;
        }
    }
}
