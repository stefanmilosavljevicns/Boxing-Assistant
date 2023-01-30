package com.example.boxing_assistant.Adapters;

import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.boxing_assistant.MusicDataBase.MusicEntity;
import com.example.boxing_assistant.R;

import java.util.List;

public class RecyclerViewForMusic extends RecyclerView.Adapter<RecyclerViewForMusic.ViewHolder> implements ItemTouchHelperAdapter {
    private static final long CLICK_TIME_INTERVAL = 300;
    private final List<MusicEntity> plan;
    private final RecyclerViewForMusic.OnNoteListener mOnNoteListener;
    private long mLastClickTime = System.currentTimeMillis();
    private ItemTouchHelper mTouchHeleper;

    public RecyclerViewForMusic(List<MusicEntity> plan, RecyclerViewForMusic.OnNoteListener onNoteListener) {
        this.plan = plan;
        this.mOnNoteListener = onNoteListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listmusicitem, parent, false);
        return new ViewHolder(view, mOnNoteListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.text.setText(plan.get(position).getMusicname());
    }

    @Override
    public int getItemCount() {
        return plan.size();
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        MusicEntity fromME = plan.get(fromPosition);
        plan.remove(fromME);
        plan.add(toPosition, fromME);
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onItemSwiped(int position) {

    }

    public void setmTouchHelper(ItemTouchHelper touchHelper) {
        this.mTouchHeleper = touchHelper;
    }

    public interface OnNoteListener {
        void onNoteClick(int position);

        void onButtonClick(int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnTouchListener, GestureDetector.OnGestureListener, View.OnClickListener {
        TextView text;
        LinearLayout parentlayout;
        RecyclerViewForMusic.OnNoteListener mOnNoteListener;
        GestureDetector mGestureDetector;
        ImageButton delete;

        public ViewHolder(@NonNull View itemView, RecyclerViewForMusic.OnNoteListener onNoteListener) {
            super(itemView);

            text = itemView.findViewById(R.id.music_name);
            text.setSelected(true);
            delete = itemView.findViewById(R.id.removebtn_music);
            parentlayout = itemView.findViewById(R.id.parent_music);
            mOnNoteListener = onNoteListener;
            mGestureDetector = new GestureDetector(itemView.getContext(), this);
            itemView.setOnTouchListener(this);
            delete.setOnClickListener(this::onClick);

        }

        @Override
        public boolean onDown(MotionEvent e) {
            return false;
        }

        @Override
        public void onShowPress(MotionEvent e) {

        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            mOnNoteListener.onNoteClick(getBindingAdapterPosition());
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            mTouchHeleper.startDrag(this);
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return false;
        }

        @Override
        public void onClick(View v) {
            long now = System.currentTimeMillis();
            if (now - mLastClickTime < CLICK_TIME_INTERVAL) {
                return;
            }
            mLastClickTime = now;
            mOnNoteListener.onButtonClick(getBindingAdapterPosition());
            text.setSelected(true);

        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            mGestureDetector.onTouchEvent(event);

            return true;
        }
    }
}
