package edu.gannon.gannonknights;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class MenuFragment extends Fragment {
    private ProgressDialog pDialog;// Progress Dialog
    ListView newsList;
    String my_url;
    LayoutInflater inflater;
    ArrayList<HashMap<String, String>> postList; //Declare Array
    private static String url = "http://wangeltmg.com/GKN_ADMIN/GET_POSTS/";
    GetNews.CustomAdapter CA;

    // JSON Node names
    private static final String TAG_ID = "id";
    private static final String POST_ALLPOSTS = "posts";
    private static final String POST_ID = "ID";
    private static final String POST_TITLE = "post_title";
    private static final String POST_CONTENT = "post_content";
    private static final String POST_DATE = "post_date";
    private static final String POST_AUTHOR = "post_author";
    private static final String USER_EMAIL = "user_email";
    private static final String GUID = "guid";

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view =  inflater.inflate(R.layout.home, container, false);
        newsList = (ListView) view.findViewById(R.id.homeListView);
        TextView topic = (TextView) view.findViewById(R.id.topic);
        postList = new ArrayList<HashMap<String, String>>();
        //Get arguments
        Bundle args = getArguments();
        String mytopic = null;
        if(args !=null) {
            mytopic = args.getString("Topic");
        }
        newsList.setOnItemClickListener(new newsListClick());

        //Log.d("Activit", "Check");
        String title = getResources().getString(R.string.app_name);
        getActivity().getActionBar().setTitle(title);
        getActivity().getActionBar().setDisplayHomeAsUpEnabled(false);   //disable back button
        getActivity().getActionBar().setHomeButtonEnabled(false);
        //setHasOptionsMenu(true); //has something to do with OnOptionsitemclick

        //Execute getContacts
        new GetNews().execute();

        return view;
    }

    public class newsListClick implements ListView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Log.d("News List", "Clicked " + id);
            String text = postList.get(position).get(GUID).toString();
            String post_title = postList.get(position).get(POST_TITLE).toString();
            String post_desc = postList.get(position).get(POST_CONTENT).toString();
            String post_date = postList.get(position).get(POST_DATE).toString();
            String post_author = postList.get(position).get(POST_AUTHOR).toString();
            String user_email = postList.get(position).get(USER_EMAIL).toString();

            android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
            android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            Bundle data = new Bundle();
            data.putString("url", text);
            data.putString(POST_TITLE, post_title);
            data.putString(POST_CONTENT, post_desc);
            data.putString(POST_DATE,post_date);
            data.putString(POST_AUTHOR,post_author);
            data.putString(USER_EMAIL,user_email);

            SinglePost singleFrag = new SinglePost();
            singleFrag.setArguments(data);
            fragmentTransaction.replace(R.id.content_frame, singleFrag);
            fragmentTransaction.commit();
        }
    }

    //Async Task
    private class GetNews extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);
            Log.d("Strings", "Checking Json");
            //Log.d("Response: ", "> " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    // contacts JSONArray
                    JSONArray posts = null;
                    // Getting JSON Array node
                    posts = jsonObj.getJSONArray(POST_ALLPOSTS);
                    Log.d("No of posts", String.valueOf(posts.length()));
                    // looping through All Contacts
                    for (int i = 0; i < posts.length(); i++) {

                        JSONObject c = posts.getJSONObject(i);
                        //Log.d("Post->",posts.getJSONObject(i).toString());

                        String id = c.getString(POST_ID);
                        String post_title = c.getString(POST_TITLE);
                        String post_content = c.getString(POST_CONTENT);
                        String post_date = c.getString(POST_DATE);
                        String post_author = c.getString(POST_AUTHOR);
                        String user_email = c.getString(USER_EMAIL);
                        String guid = c.getString(GUID);
                        Log.d("Post->ID", id);
                        //Log.d("GUID->", guid);
                        //Log.d("Post Date >", post_date);
                        //Log.d("Post Author >", post_author);

                        // tmp hashmap for single post
                        HashMap<String, String> post = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        // adding each child node to HashMap key => value
                        post.put(POST_ID, id);
                        post.put(POST_TITLE, post_title);
                        post.put(POST_CONTENT, post_content);
                        post.put(GUID, guid);
                        post.put(POST_DATE, post_date);
                        post.put(POST_AUTHOR, post_author);
                        post.put(USER_EMAIL, user_email);
                        post.put("ListCount", String.valueOf(i));

                        // adding contact to contact list
                        postList.add(post);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();

            // Updating parsed JSON data into ListView
            CA = new CustomAdapter( getActivity(), R.layout.list_item, postList);
            newsList.setAdapter(CA);
        }


        public class CustomAdapter extends ArrayAdapter<HashMap<String, String>> {

            private final ArrayList<HashMap<String, String>> objects;

            public CustomAdapter(Context context, int resource, ArrayList<HashMap<String, String>> objects) {
                //something is wrong with super
                super(context, resource, objects);
                inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                this.objects = objects;
            }

            @Override
            public int getItemViewType(int position) {
                return position;
            }

            public View getView(int position, View convertView, ViewGroup Parent){
                //convertView = new ImageView();
                //System.out.println("getView " + position + " " + convertView + " type = " + type);
                if(convertView == null){
                    convertView = inflater.inflate(R.layout.list_item,null);
                }

                android.widget.ImageView postImage = (android.widget.ImageView) convertView.findViewById(R.id.img);
                int getListPos = newsList.getFirstVisiblePosition();
                //i set the count starting 0 and saved in the hashmap array
                //to compare the first result with the first position of listview
                int count = Integer.parseInt(objects.get(position).get("ListCount"));
                my_url = objects.get(position).get(GUID);
                if(getListPos == count) {
                    //LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    convertView = inflater.inflate(R.layout.list_item_header,null);
                    TextView HeaderText = (TextView) convertView.findViewById(R.id.headertext);
                    TextView HeaderContent = (TextView) convertView.findViewById(R.id.headercontent);
                    TextView HeaderDate = (TextView) convertView.findViewById(R.id.header_date);
                    HeaderText.setText(objects.get(position).get(POST_TITLE).toUpperCase());
                    DateTimeConverter dtm = new DateTimeConverter();
                    HeaderDate.setText(dtm.DateTimeConverter(objects.get(position).get(POST_DATE)));
                    HeaderContent.setText(objects.get(position).get(POST_CONTENT));
                    if(objects.get(position).get(GUID).equals("NULL")) {
                        postImage.setImageResource(R.drawable.default_bg);
                    }else{
                        new DownloadImageTask((ImageView) convertView.findViewById(R.id.img)).execute(my_url);
                    }
                }
                else{
                    //CHoose list item
                    //LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    convertView = inflater.inflate(R.layout.list_item,null);
                    TextView thisview = (TextView) convertView.findViewById(R.id.email);
                    TextView postContent = (TextView) convertView.findViewById(R.id.mobile);
                    thisview.setText(objects.get(position).get(POST_TITLE).toUpperCase());
                    postContent.setText(objects.get(position).get(POST_CONTENT));

                    if(objects.get(position).get(GUID).equals("NULL")) {
                        postImage.setImageResource(R.drawable.default_bg);
                    }else{
                        new DownloadImageTask((ImageView) convertView.findViewById(R.id.img)).execute(my_url);
                    }
                }

                return convertView;
            }


        }//Custom Adapter

    }//END getnews Async Task

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            //pDialog.show();

        }

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            //super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            bmImage.setImageBitmap(result);
        }
    }//

}