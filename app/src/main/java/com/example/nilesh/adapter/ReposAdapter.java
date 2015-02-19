package com.example.nilesh.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.nilesh.model.Items;
import com.example.nilesh.redbus.R;
import com.example.nilesh.redbus.WebViewActivity;
import com.example.nilesh.util.CircularImageView;
import com.example.nilesh.util.TextAwesome;
import com.example.nilesh.util.VolleySingleton;

import org.json.JSONArray;

import java.util.List;

/**
 * Created by Nilesh on 2/17/2015.
 * <p/>
 * Custom Adapter to display search repository result
 * <p/>
 * ReposAdapter(Context, List<Items>)
 */
public class ReposAdapter extends BaseAdapter {

    List<Items> reposList;
    Context context;

    // holder for view to make listview scroll smooth
    static class ViewHolder {
        TextAwesome reposName, follower, fork, watcher, userName;
        CircularImageView userPic;
        RelativeLayout container;
    }

    public ReposAdapter(Context context, List<Items> reposList) {
        this.context = context;
        this.reposList = reposList;
    }

    @Override
    public int getCount() {
        return reposList.size();
    }

    @Override
    public Object getItem(int position) {
        return reposList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder viewHolder;

        //getting the viewHolder if the view was inflated before, else creating a new
        // view and assigning viewHolder to it
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater)
                    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.custom_list, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.reposName = (TextAwesome) convertView.findViewById(R.id.reposName);
            viewHolder.follower = (TextAwesome) convertView.findViewById(R.id.follower);
            viewHolder.fork = (TextAwesome) convertView.findViewById(R.id.fork);
            viewHolder.watcher = (TextAwesome) convertView.findViewById(R.id.watcher);
            viewHolder.userName = (TextAwesome) convertView.findViewById(R.id.userName);
            viewHolder.userPic = (CircularImageView) convertView.findViewById(R.id.userPic);
            viewHolder.container = (RelativeLayout) convertView.findViewById(R.id.container);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        //Setting the information of Item data in view.
        final Items item = reposList.get(position);
        viewHolder.userName.setText(item.getOwner().getLogin().toUpperCase());
        viewHolder.fork.setText(String.valueOf(item.getForksCount()));

        //if count is -1, that means follower count is yet to be fetched, else get count from
        // object and set to the view
        if (item.getFollwerCount() == -1) {
            //getting number of followers
            VolleySingleton.getInstance(context).addToRequestQueue(new JsonArrayRequest(
                    item.getOwner().getFollowersUrl(), new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray jsonArray) {
                    item.setFollwerCount(jsonArray.length());
                    viewHolder.follower.setText(String.valueOf(item.getFollwerCount()));
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {

                }
            }));
        } else {
            viewHolder.follower.setText(String.valueOf(item.getFollwerCount()));
        }

        viewHolder.watcher.setText(String.valueOf(item.getWatchersCount()));
        viewHolder.reposName.setText(item.getReposName().toUpperCase());

        //resetting the background of user image
        viewHolder.userPic.setBackgroundResource(0);

        //getting image of the user.
        VolleySingleton.getInstance(context).getImageLoader().get(item.getOwner().getAvatarUrl(),
                new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer imageContainer, boolean b) {
                viewHolder.userPic.setImageBitmap(imageContainer.getBitmap());
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                System.out.println(volleyError);
            }
        });

        //adding click listener to list layout, on click it opens webview activity
        viewHolder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, WebViewActivity.class);
                intent.putExtra("url", item.getGitUrl());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        return convertView;
    }

}
