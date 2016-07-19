package edu.gannon.gannonknights;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by wangel on 4/29/15.
 */
public class BreakingNews extends Activity{
    private ProgressDialog pDialog;// Progress Dialog
    ArrayList<HashMap<String, String>> postList; //Declare Array
    private String post_id;
    TextView PostTitle, PostContent, PostDate, PostAuthor, UserEmail;
    private static String url;
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


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_post);

        postList = new ArrayList<HashMap<String, String>>();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            //String data = extras.getString(POST_CONTENT);
            int abc = extras.getInt(POST_ID);

            Log.d("DATA", String.valueOf(abc));
            url = "http://wangeltmg.com/GKN_ADMIN/GET_POSTS/breaking_news.php?post_id="+String.valueOf(abc)+"";
            Log.d("URL", url);

            //Log.d("DATA", data);
            //Log.d("ID", String.valueOf(abc));
        }
        //Execute getContacts
        new GetNews().execute();

    }//

    //Async Task
    private class GetNews extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(BreakingNews.this);
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
                        Log.d("GUID->", guid);
                        Log.d("Post Date >", post_date);
                        Log.d("Post Author >", post_author);

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
            if (pDialog.isShowing()) {
                pDialog.dismiss();
            }

            //SET SOME DATA
            PostTitle = (TextView) findViewById(R.id.post_title);
            PostTitle.setText(postList.get(0).get(POST_TITLE));
            PostContent = (TextView) findViewById(R.id.post_content);
            PostContent.setText(postList.get(0).get(POST_CONTENT));
            PostDate = (TextView) findViewById(R.id.post_date);
            PostDate.setText(postList.get(0).get(POST_DATE));
            PostAuthor = (TextView)findViewById(R.id.post_author);
            PostAuthor.append(postList.get(0).get(POST_AUTHOR));
            PostAuthor.append(", "+postList.get(0).get(USER_EMAIL));

            new DownloadImageTask((ImageView) findViewById(R.id.img)).execute(postList.get(0).get(GUID));

        }//
    }//Get news end
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(BreakingNews.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
            //ImgBar.setVisibility(View.VISIBLE);

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
            bmImage.setImageBitmap(result);
            pDialog.hide();
        }
    }//Download Image

}
