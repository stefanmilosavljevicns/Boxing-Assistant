package com.example.boxing_assistant.Adapters;

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

import com.example.boxing_assistant.R;

import java.util.ArrayList;

public class RecyclerViewForWeb extends RecyclerView.Adapter<RecyclerViewForWeb.ViewHolder> {
    private final ArrayList<String> plan;
    private final RecyclerAdapterForWorkout.OnNoteListener mOnNoteListener;

    public RecyclerViewForWeb(ArrayList<String> plan, RecyclerAdapterForWorkout.OnNoteListener onNoteListener) {
        this.plan = plan;
        this.mOnNoteListener = onNoteListener;
    }

    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listwebitem, parent, false);
        return new ViewHolder(view, mOnNoteListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewForWeb.ViewHolder holder, int position) {
        holder.text.setText(plan.get(position));
    }


    @Override
    public int getItemCount() {
        return plan.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnTouchListener, GestureDetector.OnGestureListener, View.OnClickListener {
        TextView text;
        RelativeLayout parentlayout;
        RecyclerAdapterForWorkout.OnNoteListener mOnNoteListener;
        GestureDetector mGestureDetecture;

        public ViewHolder(@NonNull View itemView, RecyclerAdapterForWorkout.OnNoteListener onNoteListener) {
            super(itemView);
            text = itemView.findViewById(R.id.web_name);
            parentlayout = itemView.findViewById(R.id.webworkoutlayout);
            mOnNoteListener = onNoteListener;
            mGestureDetecture = new GestureDetector(itemView.getContext(), this);
            itemView.setOnTouchListener(this);

        }

        @Override
        public boolean onDown(MotionEvent motionEvent) {
            return false;
        }

        @Override
        public void onShowPress(MotionEvent motionEvent) {

        }

        @Override
        public boolean onSingleTapUp(MotionEvent motionEvent) {
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
            return false;
        }

        @Override
        public void onLongPress(MotionEvent motionEvent) {

        }

        @Override
        public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
            return false;
        }

        @Override
        public void onClick(View view) {
            mOnNoteListener.onButtonClick(getBindingAdapterPosition());
        }

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mOnNoteListener.onButtonClick(getBindingAdapterPosition());
            return true;
        }
    }
    public interface OnNoteListener{
        void onNoteClick(int position);
        void onButtonClick(int position);
        void onShareClick(int position);
        void onRenameClick(int position);
    }

}