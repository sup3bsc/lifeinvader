package com.lifeinvader.tracker.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.lifeinvader.tracker.R;
import com.lifeinvader.tracker.activities.GroupDetailActivity;
import com.lifeinvader.tracker.activities.GroupListActivity;
import com.lifeinvader.tracker.fragments.GroupDetailFragment;
import com.lifeinvader.tracker.models.Group;

import java.util.ArrayList;

public class GroupsAdapter extends FirebaseRecyclerAdapter<GroupsAdapter.ViewHolder, Group> {
    private final GroupListActivity activity;

    public GroupsAdapter(GroupListActivity activity, DatabaseReference query, Class<Group> itemClass,
                         @Nullable ArrayList<Group> items, @Nullable ArrayList<String> keys) {
        super(query, itemClass, items, keys);
        this.activity = activity;
    }

    @Override
    public GroupsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.group_list_content, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final GroupsAdapter.ViewHolder holder, int position) {
        Group item = getItem(position);
        holder.mItem = item;
        holder.textViewName.setText(item.name);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activity.twoPane) {
                    Bundle arguments = new Bundle();
                    arguments.putString(GroupDetailFragment.ARG_ITEM, holder.mItem.key);
                    GroupDetailFragment fragment = new GroupDetailFragment();

                    fragment.setArguments(arguments);
                    activity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.group_detail_container, fragment)
                        .commit();
                } else {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, GroupDetailActivity.class);
                    intent.putExtra(GroupDetailFragment.ARG_ITEM, holder.mItem.key);

                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    protected void itemAdded(Group item, String key, int position) {
        item.key = key;
    }

    @Override
    protected void itemChanged(Group oldItem, Group newItem, String key, int position) {
        newItem.key = key;
    }

    @Override
    protected void itemRemoved(Group item, String key, int position) {
        // NOOP
    }

    @Override
    protected void itemMoved(Group item, String key, int oldPosition, int newPosition) {
        // NOOP
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public final View mView;
        public final TextView textViewName;
        public Group mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            textViewName = (TextView) view.findViewById(R.id.content);
        }
    }
}
