package com.cantwellcode.fitfriend.app.plan;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.cantwellcode.fitfriend.app.R;
import com.cantwellcode.fitfriend.app.connect.Group;
import com.cantwellcode.fitfriend.app.connect.SocialEvent;
import com.cantwellcode.fitfriend.app.utils.Statics;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Daniel on 6/1/2014.
 */
public class Plan extends Fragment {

    private static Fragment instance = null;

    public static Fragment newInstance() {
        if(instance == null)
            instance = new Plan();
        return instance;
    }

    private CalendarView mCalendarView;
    private ListView mListView;
    private Calendar mCalendar;

    private final static int MAX_EVENT_SEARCH_RESULTS = 20;

    private ParseUser user;
    private ParseQueryAdapter<Event> mEvents;
    private ParseQueryAdapter.QueryFactory<Event> factory;
    private List<String> mGroups;
    private String mCurrentGroup;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_plan, null);
        user = ParseUser.getCurrentUser();

        mCalendarView = (CalendarView) root.findViewById(R.id.calendarView);
        mListView = (ListView) root.findViewById(R.id.listView);

        mCalendar = Calendar.getInstance();

        mCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                mCalendar.set(Calendar.YEAR, year);
                mCalendar.set(Calendar.MONTH, month);
                mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

//                if (mCurrentGroup.equals("Friends")) {
                    setupFriendsEvents();
//                } else {
//                    setupGroupPosts(mCurrentGroup);
//                }
            }
        });

        mGroups = new ArrayList<String>();
        mGroups.add("Friends");
        for (Group group : getGroups()) {
            mGroups.add(group.getName());
        }
        mCurrentGroup = "Friends";

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, mGroups);

        setupFriendsEvents();

        setHasOptionsMenu(true);

        return root;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        restoreActionBar();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_new:
                Intent intent = new Intent(getActivity(), AddEvent.class);
                startActivityForResult(intent, Statics.INTENT_REQUEST_EVENT);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /* Load Friends' Events into the list below the calendar */
    private void setupFriendsEvents() {
        ParseQuery<ParseObject> friendships = SocialEvent.getCurrentFriendshipsQuery();
        final List<ParseUser> friends = new ArrayList<ParseUser>();
        friends.add(user);

        /* Query for current friendships */
        friendships.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {

                /* Add all friends to the list */
                for (ParseObject object : parseObjects) {
                    ParseUser from = object.getParseUser("from");

                    ParseUser friend;
                    /* Check if friend is the 'from' or 'to' user in the friendship */
                    if (from.hasSameId(ParseUser.getCurrentUser())) {
                        friend = object.getParseUser("to");
                    } else {
                        friend = from;
                    }

                    friends.add(friend);
                }

                /* Set up factory for events by friends */
                factory = new ParseQueryAdapter.QueryFactory<Event>() {
                    public ParseQuery<Event> create() {

                        DateFormat df = new SimpleDateFormat("d MMMM yyyy");
                        String date = df.format(mCalendar.getTime());

                        /* Create a query for forum posts */
                        ParseQuery<Event> query = Event.getQuery();
                        // Forum posts must be created by someone in the list of friends
                        query.whereContainedIn("user", friends);
                        query.whereEqualTo("date", date);
                        query.include("user");
                        query.orderByAscending("title");
                        query.setLimit(MAX_EVENT_SEARCH_RESULTS);

                        return query;
                    }
                };

                /* Set up list adapter using the factory of friends */
                mEvents = new ParseQueryAdapter<Event>(getActivity(), factory) {
                    @Override
                    public View getItemView(final Event event, View view, ViewGroup parent) {

//                        mOnClickListener = new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                switch (v.getId()) {
//                                    /* Click listener for high five button */
//                                    case R.id.highFive:
//                                        post.addHighFive();
//                                        post.saveInBackground(new SaveCallback() {
//                                            @Override
//                                            public void done(ParseException e) {
//                                                posts.notifyDataSetChanged();
//                                            }
//                                        });
//                                        break;
//                                    /* Click listener for discussion button */
//                                    case R.id.discusson:
//                                        FragmentManager fm = getFragmentManager();
//                                        DiscussionDialog discussionDialog = new DiscussionDialog(post);
//                                        discussionDialog.show(fm, "DiscussionDialog");
//                                }
//                            }
//                        };

                        if (view == null) {
                            view = view.inflate(getActivity(), R.layout.event_item, null);
                        }

                        TextView title = (TextView) view.findViewById(R.id.title);
                        TextView time = (TextView) view.findViewById(R.id.time);
                        TextView creator = (TextView) view.findViewById(R.id.creator);
                        ImageView icon = (ImageView) view.findViewById(R.id.icon);

                        String type = event.getType();
                        if (type.equals("Swim")) {
                            icon.setImageResource(R.drawable.swim_icon);
                        } else if (type.equals("Bike")) {
                            icon.setImageResource(R.drawable.bike_icon);
                        } else if (type.equals("Run")) {
                            icon.setImageResource(R.drawable.run_icon);
                        } else if (type.equals("Gym")) {
                            icon.setImageResource(R.drawable.gym_icon);
                        } // else icon is food icon

                        title.setText(event.getTitle());
                        time.setText(event.getTime());
                        creator.setText(event.getUser().getString("name"));

                        view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getActivity(), EventDetails.class);
//                                intent.putExtra("id", event.getObjectId());
//                                intent.putExtra("attendees", event.getAttendees().toArray());
                                intent.putExtra("title", event.getTitle());
                                intent.putExtra("date", event.getDate());
                                intent.putExtra("time", event.getTime());
                                intent.putExtra("creator", event.getUser().getString("name"));
                                intent.putExtra("location", event.getLocation());
                                intent.putExtra("description", event.getDescription());
                                intent.putExtra("type", event.getType());
                                startActivityForResult(intent, Statics.INTENT_REQUEST_EVENT_DETAILS);
                            }
                        });

                        return view;
                    }
                };

                mListView.setAdapter(mEvents);
            }
        });
    }

    public static List<Group> getGroups() {
        ParseQuery<Group> query = Group.getQuery();
        query.whereEqualTo("members", ParseUser.getCurrentUser());
        try {
            return query.find();
        } catch (ParseException e) {
            return null;
        }
    }

    private void restoreActionBar() {
        ActionBar actionBar = getActivity().getActionBar();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == getActivity().RESULT_OK && requestCode == Statics.INTENT_REQUEST_EVENT) {
            setupFriendsEvents();
        }
    }
}
