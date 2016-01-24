package com.meetme.meetme;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInstaller;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.meetme.meetme.DataManagers.DataBase.EventInfo;
import com.meetme.meetme.DataManagers.DataManager;
import com.meetme.meetme.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FriendList extends AppCompatActivity {

    ImageView imageView;
    private StaggeredGridLayoutManager gaggeredGridLayoutManager;
    SolventRecyclerViewAdapter rcAdapter;
    Integer mCurrItemPosition;
    List<FriendsInfo> friendsList;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view2);
        recyclerView.setHasFixedSize(true);
        gaggeredGridLayoutManager = new StaggeredGridLayoutManager(2, 1);
        recyclerView.setLayoutManager(gaggeredGridLayoutManager);

        AccessToken accessToken = AccessToken.getCurrentAccessToken();

        GraphRequest request = GraphRequest.newMyFriendsRequest(
                accessToken,
                new GraphRequest.GraphJSONArrayCallback() {
                    @Override
                    public void onCompleted(JSONArray array, GraphResponse response) {
                        Log.d("Friends", response.toString());
                        ShowFriends(array);
                    }
                });


        Bundle parameters = new Bundle();
        parameters.putString("fields", "picture, name");
        request.setParameters(parameters);
        request.executeAsync();




    }
/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        return true;
    }*/

    void ShowFriends(JSONArray strJson) {
        List<FriendsInfo> listViewItems = new ArrayList<FriendsInfo>();
        try {

            JSONArray jsonArray = strJson;

            //Iterate the jsonArray and print the info of JSONObjects
            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonObject = jsonArray.getJSONObject(i);

                String id = jsonObject.optString("id").toString();
                String name = jsonObject.optString("name").toString();
                JSONObject jsonObject1 = jsonObject.getJSONObject("picture");
                jsonObject1 = jsonObject1.getJSONObject("data");
                String pictureURL = jsonObject1.optString("url");

                String userPictureLarge = "https://graph.facebook.com/"  + id + "/picture?type=large";

                /*show picture*/
                DownloadImageTask downloadImageTask = (DownloadImageTask) new DownloadImageTask(i).execute(userPictureLarge);
                Bitmap bitmap = downloadImageTask.getBmImage();

                listViewItems.add(new FriendsInfo(id, name, null));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        rcAdapter = new SolventRecyclerViewAdapter(FriendList.this, listViewItems);
        recyclerView.setAdapter(rcAdapter);
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        int mPosition;
        Bitmap mIcon11;

        public DownloadImageTask(int position) {
            mPosition = position;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            mIcon11 = null;
            try {
                InputStream in = new URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            ((SolventViewHolders)recyclerView.findViewHolderForAdapterPosition(mPosition)).
                    UserPicture.setImageBitmap(result);
        }

        public Bitmap getBmImage(){
            return mIcon11;
        }
    }
    /*******************************************************/
/*    public class SearchResultsActivity extends Activity {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            handleIntent(getIntent());
        }

        @Override
        protected void onNewIntent(Intent intent) {
            super.onNewIntent(intent);

            handleIntent(intent);
        }

        private void handleIntent(Intent intent) {

            if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
                String query = intent.getStringExtra(SearchManager.QUERY);

                showResults(query);
            }
        }

        private void showResults(String query) {
            // Query your data set and show results
            // ...
        }
    }*/
    public class SolventRecyclerViewAdapter  extends RecyclerView.Adapter<SolventViewHolders> {

        private List<FriendsInfo> itemList;
        private Context context;

        public SolventRecyclerViewAdapter(Context context, List<FriendsInfo> itemList) {
            this.itemList = itemList;
            this.context = context;


        }

        public FriendsInfo getItemAtPosition(int position) {
            return itemList.get(position);
        }
        @Override
        public SolventViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {

            View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_friend_list, null);
            SolventViewHolders rcv = new SolventViewHolders(layoutView);
            return rcv;
        }

        @Override
        public void onBindViewHolder(SolventViewHolders holder, int position) {
            holder.name.setText(itemList.get(position).getName());

            if (itemList.get(position).getPhoto() != null)
                holder.UserPicture.setImageBitmap(itemList.get(position).getPhoto());
        }

        @Override
        public int getItemCount() {
            return this.itemList.size();
        }
    }

    public class SolventViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener {


        public TextView id;
        public TextView name;
        public ImageView UserPicture;

        public SolventViewHolders(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            name = (TextView) itemView.findViewById(R.id.userName1);
            UserPicture = (ImageView) itemView.findViewById(R.id.userPicture);
        }

        @Override
        public void onClick(View view) {
            mCurrItemPosition = getPosition();
            FriendsInfo friend = ((SolventRecyclerViewAdapter)
                    ((RecyclerView) view.getParent()).getAdapter()).getItemAtPosition(mCurrItemPosition);
            Toast.makeText(view.getContext(), "Friend ID = " + friend.id, Toast.LENGTH_SHORT).show();
        }
    }

}



