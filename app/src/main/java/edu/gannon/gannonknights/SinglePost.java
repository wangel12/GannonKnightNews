package edu.gannon.gannonknights;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;


public class SinglePost extends Fragment {

    // Progress Dialog
    private ProgressDialog pDialog;
    public String get_url;

    //ProgressBar ImgBar = (ProgressBar) findViewById(R.id.progressBar);

    public String my_url;
    TextView PostTitle, PostContent, PostDate, PostAuthor, UserEmail;

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Get arguments
        Bundle args = getArguments();
        get_url = args.getString("url");
        String post_title = null, post_date = null, post_content = null, post_author = null, user_email = null;

        if(args != null){
            post_title = args.getString("post_title");
            Log.d("TITLE", post_title);
            post_content = args.getString("post_content");
            post_date = args.getString("post_date");
            post_author = args.getString("post_author");
            user_email = args.getString("user_email");
            //Log.d("URL", get_url);
            //Log.d("Post Date", post_date);
            my_url = get_url;

        }
        View view = inflater.inflate(R.layout.single_post, container, false);
        if(get_url.equals("NULL")){

        }else{
            new DownloadImageTask((ImageView) view.findViewById(R.id.img)).execute(my_url);
        }
        PostTitle = (TextView) view.findViewById(R.id.post_title);
        PostTitle.setText(post_title);
        PostContent = (TextView) view.findViewById(R.id.post_content);
        PostContent.setText(post_content);
        PostDate = (TextView) view.findViewById(R.id.post_date);
        PostAuthor = (TextView) view.findViewById(R.id.post_author);
        PostAuthor.append(post_author);
        PostAuthor.append(", "+user_email);


        //Get TIME DATE FORMAT
        String date = post_date;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd HH:MM:SS");
        Date testDate = null;
        try {
            testDate = sdf.parse(date);
        }catch(Exception ex){
            ex.printStackTrace();
        }

        SimpleDateFormat formatter = new SimpleDateFormat("hh:mm a,F MMM");
        String newFormat = formatter.format(testDate);
        PostDate.setText(newFormat);
        System.out.println(".....Date..."+newFormat);

        //getActivity().getActionBar().setTitle(mytopic);
        getActivity().getActionBar().setIcon(R.drawable.ic_launcher);
        getActivity().getActionBar().setHomeButtonEnabled(true);
        getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
        setHasOptionsMenu(true); //has something to do with OnOptionsitemclick

        return view;
    }

    //When the ActionBar is Clicked
    //Which option is clicked & Execute
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d("MENU RETURN", "CLICKED");
        switch (item.getItemId()) {
            case android.R.id.home:
                Log.d("MENU RETURN", "CLICKED INSIDE");
                if (getFragmentManager().getBackStackEntryCount() == 0) {
                    getActivity().finish();
                } else {
                    getFragmentManager().popBackStack();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(getActivity());
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
    }


}//class
