package com.example.boxing_assistant.Adapters;

import android.content.Context;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.boxing_assistant.R;

import java.util.ArrayList;

public class RecyclerViewAdapterForCombos extends RecyclerView.Adapter<RecyclerViewAdapterForCombos.ViewHolder> implements ItemTouchHelperAdapterForCombo {
    private ArrayList<String> mNotes = new ArrayList<>();
    private  ArrayList<Integer> fiks = new ArrayList<>();
    private OnNoteListener mOnNoteListener;
    private ItemTouchHelper mTouchHelper;
    String text;
    private Context mContext;
    public RecyclerViewAdapterForCombos(ArrayList<String> mNotes, Context mContext) {
        this.mNotes = mNotes;
        this.mContext = mContext;
    }



    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_comboitem,parent,false);
        RecyclerViewAdapterForCombos.ViewHolder holder = new ViewHolder(view,mOnNoteListener);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        text = mNotes.get(position);
        holder.text.setText(text);

    }

    @Override
    public int getItemCount() {
        return mNotes.size();
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        String fromNote = mNotes.get(fromPosition);
        mNotes.remove(fromNote);
        mNotes.add(toPosition,fromNote);
        notifyItemMoved(fromPosition,toPosition);

    }

    @Override
    public void onItemSwiped(int position) {
        mNotes.remove(position);
        notifyItemRemoved(position);
    }


    public void setmTouchHelper(ItemTouchHelper touchHelper){
        this.mTouchHelper = touchHelper;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements   View.OnTouchListener, GestureDetector.OnGestureListener {
        TextView text;
        RecyclerViewAdapterForCombos.OnNoteListener mOnNoteListener;
        RelativeLayout parentLayout;

        GestureDetector mGestureDetecture;
        public ViewHolder(@NonNull View itemView, RecyclerViewAdapterForCombos.OnNoteListener onNoteListener) {
            super(itemView);
            text = itemView.findViewById(R.id.combo_name);
            parentLayout = itemView.findViewById((R.id.parent_layout_combos));
            mGestureDetecture = new GestureDetector(itemView.getContext(),this);
            mOnNoteListener = onNoteListener;
            itemView.setOnTouchListener(this);
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


         return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            mTouchHelper.startDrag(this);
        }


        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return false;
        }


        @Override
        public boolean onTouch(View v, MotionEvent event) {
            mGestureDetecture.onTouchEvent(event);
            return true;
        }
    }
    public interface OnNoteListener{
        void onNoteClick(int position);
    }
}
