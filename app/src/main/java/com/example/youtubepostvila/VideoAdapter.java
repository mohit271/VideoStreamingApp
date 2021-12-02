package com.example.youtubepostvila;

import android.app.Application;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.youtubepostvila.Model.VideoMember;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;

public class VideoAdapter extends FirebaseRecyclerAdapter<VideoMember,VideoAdapter.personsViewholder> {


    private Context context;
    SimpleExoPlayer exoPlayer;

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public VideoAdapter(@NonNull FirebaseRecyclerOptions<VideoMember> options,Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull personsViewholder holder, int position, @NonNull VideoMember model) {
        holder.firstname.setText(model.getName());
//        BandwidthMeter bandwidthMeter= new DefaultBandwidthMeter.Builder(application).build();
//        TrackSelector trackSelector= new DefaultTrackSelector(new AdaptiveTrackSelection.Factory());
//        holder.exoPlayer=ExoPlayerF
        exoPlayer=new SimpleExoPlayer.Builder(context).build();
        holder.playerView.setPlayer(exoPlayer);
        MediaItem mediaItem = MediaItem.fromUri(model.getVideoUrl());
        exoPlayer.addMediaItem(mediaItem);
        exoPlayer.prepare();
        exoPlayer.setPlayWhenReady(false);


    }

    @NonNull
    @Override
    public personsViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view
                = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rv_item, parent, false);
        return new VideoAdapter.personsViewholder(view);

    }

    class personsViewholder
            extends RecyclerView.ViewHolder {
        TextView firstname, lastname, age;

        PlayerView playerView;

        public personsViewholder(@NonNull View itemView)
        {
            super(itemView);
            firstname=itemView.findViewById(R.id.vvItemName);
            playerView=itemView.findViewById(R.id.vvItemPlayer);





        }
    }
}
