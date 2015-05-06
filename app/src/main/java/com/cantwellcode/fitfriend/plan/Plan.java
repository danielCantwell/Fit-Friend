package com.cantwellcode.fitfriend.plan;

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
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cantwellcode.fitfriend.R;
import com.cantwellcode.fitfriend.utils.SocialEvent;
import com.cantwellcode.fitfriend.utils.Statics;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Daniel on 6/1/2014.
 */
public class Plan extends Fragment {

    private static Fragment instance = null;

    public static Fragment newInstance() {
        if (instance == null)
            instance = new Plan();
        return instance;
    }

    private CalendarView mCalendarView;
    private ListView mListView;
    private TextView mEmptyListText;
    private Calendar mCalendar;

    private final static int MAX_EVENT_SEARCH_RESULTS = 20;

    private ParseUser user;
    private ParseQueryAdapter<Event> mEvents;
    private ParseQueryAdapter.QueryFactory<Event> factory;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_plan, null);
        user = ParseUser.getCurrentUser();

        mCalendarView = (CalendarView) root.findViewById(R.id.calendarView);
        mListView = (ListView) root.findViewById(R.id.listView);
        mEmptyListText = (TextView) root.findViewById(android.R.id.empty);
        mListView.setEmptyView(mEmptyListText);
        mCalendar = Calendar.getInstance();

        mCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                mCalendar.set(Calendar.YEAR, year);
                mCalendar.set(Calendar.MONTH, month);
                mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                setupFriendsEvents();
            }
        });

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
                int year = mCalendar.get(Calendar.YEAR);
                int month = mCalendar.get(Calendar.MONTH);
                int day = mCalendar.get(Calendar.DAY_OF_MONTH);
                Intent intent = new Intent(getActivity(), AddEvent.class);
                Bundle bundle = new Bundle();
                bundle.putInt("year", year);
                bundle.putInt("month", month);
                bundle.putInt("day", day);
                intent.putExtra("args", bundle);
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
                    if (from.hasSameId(user)) {
                        friend = object.getParseUser("to");
                    } else {
                        friend = from;
                    }

                    friends.add(friend);
                }

                /* Set up factory for events by friends */
                factory = new ParseQueryAdapter.QueryFactory<Event>() {
                    public ParseQuery<Event> create() {

                        int year = mCalendar.get(Calendar.YEAR);
                        int month = mCalendar.get(Calendar.MONTH);
                        int day = mCalendar.get(Calendar.DAY_OF_MONTH);

                        Calendar midnight = Calendar.getInstance();
                        midnight.set(Calendar.YEAR, year);
                        midnight.set(Calendar.MONTH, month);
                        midnight.set(Calendar.DAY_OF_MONTH, day);
                        midnight.set(Calendar.HOUR_OF_DAY, 0);
                        midnight.set(Calendar.MINUTE, 0);
                        midnight.set(Calendar.SECOND, 0);

                        Calendar elevenFiftyNine = Calendar.getInstance();
                        elevenFiftyNine.set(Calendar.YEAR, year);
                        elevenFiftyNine.set(Calendar.MONTH, month);
                        elevenFiftyNine.set(Calendar.DAY_OF_MONTH, day);
                        elevenFiftyNine.set(Calendar.HOUR_OF_DAY, 23);
                        elevenFiftyNine.set(Calendar.MINUTE, 59);
                        elevenFiftyNine.set(Calendar.SECOND, 59);

                        Date dayStart = new Date(midnight.getTimeInMillis());
                        Date dayEnd = new Date(elevenFiftyNine.getTimeInMillis());

                        /* Create a query for events */
                        ParseQuery<Event> query = Event.getQuery();
                        // Events must be created by someone in the list of friends
                        query.whereContainedIn("user", friends);
                        query.whereGreaterThan("dateTime", dayStart);
                        query.whereLessThan("dateTime", dayEnd);
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

                        if (view == null) {
                            view = view.inflate(getActivity(), R.layout.plan_event_list_item, null);
                        }

                        TextView title = (TextView) view.findViewById(R.id.title);
                        TextView time = (TextView) view.findViewById(R.id.time);
                        TextView creator = (TextView) view.findViewById(R.id.creator);

                        SimpleDateFormat timeFormat = new SimpleDateFormat("K:mm aa", Locale.US);

                        title.setText(event.getTitle());
                        time.setText(timeFormat.format(event.getDateTime()));
                        creator.setText(event.getUser().getString("name"));

                        view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (user.getBoolean("athlete")) {
                                    event.pinInBackground(Statics.PIN_EVENT_DETAILS, new SaveCallback() {
                                        @Override
                                        public void done(ParseException e) {
                                            Intent intent = new Intent(getActivity(), EventDetails.class);
                                            startActivityForResult(intent, Statics.INTENT_REQUEST_EVENT_DETAILS);
                                        }
                                    });
                                } else {
                                    Toast.makeText(getActivity(), "Upgrade to 'Athlete' to view event details", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                        view.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View v) {
                                if (event.getUser().getObjectId().equals(user.getObjectId())) {

                                    HashMap<String, String> params = new HashMap<String, String>();
                                    params.put(Statics.PUSH_ACTION_UNSUBSCRIBE, Statics.PUSH_CHANNEL_ID + event.getObjectId());

                                    String json = getJSONString(params);
                                    JSONObject j = null;
                                    try {
                                        j = new JSONObject(json);
                                    } catch (JSONException e1) {
                                        e1.printStackTrace();
                                    }

                                    ParseQuery query = ParseInstallation.getQuery();
                                    query.whereEqualTo("channels", event.getObjectId());

                                    ParsePush push = new ParsePush();
                                    push.setData(j);
                                    push.setQuery(query);
                                    push.sendInBackground();

                                    Toast.makeText(getActivity(), "Deleting Event", Toast.LENGTH_SHORT).show();
                                    event.deleteInBackground(new DeleteCallback() {
                                        @Override
                                        public void done(ParseException e) {
                                            mEvents.loadObjects();
                                        }
                                    });
                                    return true;
                                } else {
                                    return false;
                                }
                            }
                        });

                        return view;
                    }
                };

                mListView.setAdapter(mEvents);
            }
        });
    }

    private void restoreActionBar() {
        ActionBar actionBar = getActivity().getActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle("Plan");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == getActivity().RESULT_OK && requestCode == Statics.INTENT_REQUEST_EVENT) {
            setupFriendsEvents();
        }
    }

    private String getJSONString(HashMap<String, String> keyValues) {
        String json = "{\"";

        int count = 1;
        for (Map.Entry<String, String> entry : keyValues.entrySet()) {

            String key = entry.getKey();
            String value = entry.getValue();

            json += key + "\":\"" + value + "\"";

            if (count != keyValues.entrySet().size()) {
                json += ",";
            }

            count ++;
        }

        json += "}";

        return json;
    }
}
