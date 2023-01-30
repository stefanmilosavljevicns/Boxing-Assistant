package com.example.boxing_assistant.Adapters;

import android.annotation.SuppressLint;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.boxing_assistant.DataBaseTimer.Time;
import com.example.boxing_assistant.R;

import java.util.List;

public class RecyclerAdapterForTimer extends RecyclerView.Adapter<RecyclerAdapterForTimer.ViewHolder> {
    private final List<Time> plan;
    private final RecyclerAdapterForTimer.OnNoteListener mOnNoteListener;
    public RecyclerAdapterForTimer(List<Time> plan, RecyclerAdapterForTimer.OnNoteListener onNoteListener) {
        this.plan = plan;
        this.mOnNoteListener = onNoteListener;
    }


    public RecyclerAdapterForTimer.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_timeitem,parent,false);
        return new ViewHolder(view, mOnNoteListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapterForTimer.ViewHolder holder, int position) {
        holder.text.setText(plan.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return plan.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnTouchListener, GestureDetector.OnGestureListener, View.OnClickListener {
        TextView text;
        RelativeLayout parentlayout;
        RecyclerAdapterForTimer.OnNoteListener mOnNoteListener;
        GestureDetector mGestureDetecture;
        ImageButton delete;
        ImageButton rename;
        public ViewHolder(@NonNull View itemView, RecyclerAdapterForTimer.OnNoteListener onNoteListener) {
            super(itemView);
            text = itemView.findViewById(R.id.workout_name);
            delete = itemView.findViewById(R.id.removebtn_timer);
            parentlayout = itemView.findViewById(R.id.parent_timer);
            rename = itemView.findViewById(R.id.renamebtn_timer);
            mOnNoteListener = onNoteListener;
            mGestureDetecture = new GestureDetector(itemView.getContext(),this);
            itemView.setOnTouchListener(this);
            delete.setOnClickListener(this::onClick);
            rename.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnNoteListener.onRenameClick(getBindingAdapterPosition());
                }
            });
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

        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return false;
        }

        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            mGestureDetecture.onTouchEvent(event);
            return true;
        }

        @Override
        public void onClick(View v) {
            mOnNoteListener.onButtonClick(getBindingAdapterPosition());
        }
    }

    public interface OnNoteListener{
        void onNoteClick(int position);
        void onButtonClick(int position);
        void onRenameClick(int position);
    }
}
