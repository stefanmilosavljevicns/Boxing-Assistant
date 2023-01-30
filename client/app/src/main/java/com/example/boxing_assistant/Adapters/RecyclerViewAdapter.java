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
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.boxing_assistant.Models.Model;
import com.example.boxing_assistant.R;

import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> implements ItemTouchHelperAdapter {
    private static final String TAG = "RecyclerViewAdapter";
    private long mLastClickTime = System.currentTimeMillis();
    private static final long CLICK_TIME_INTERVAL = 300;
    private ArrayList<Model> mNotes = new ArrayList<>();
    private OnNoteListener mOnNoteListener;
    private ItemTouchHelper mTouchHelper;


    public RecyclerViewAdapter(ArrayList<Model> mNotes,  OnNoteListener onNoteListener) {
        this.mNotes = mNotes;
        this.mOnNoteListener = onNoteListener;
    }

    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem,parent,false);
        ViewHolder holder = new ViewHolder(view,mOnNoteListener);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.text.setText(mNotes.get(position).getTitle());
        String clock = String.format(Locale.ENGLISH, "%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(mNotes.get(position).getTime()),TimeUnit.MILLISECONDS.toSeconds(mNotes.get(position).getTime()) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(mNotes.get(position).getTime())));
        holder.numb_text.setText(clock);
    }

    @Override
    public int getItemCount() {
        return mNotes.size();
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        Model fromNote = mNotes.get(fromPosition);
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
        TextView text,numb_text;
        OnNoteListener mOnNoteListener;
        RelativeLayout parentLayout;
        ImageButton btn;
        GestureDetector mGestureDetecture;
        public ViewHolder(@NonNull View itemView,OnNoteListener onNoteListener) {
            super(itemView);
            numb_text = itemView.findViewById(R.id.combo_time);
            text = itemView.findViewById(R.id.combo_name);
            btn = itemView.findViewById(R.id.removebtn_plan);
            parentLayout = itemView.findViewById((R.id.parent_layout));
            mOnNoteListener = onNoteListener;
            mGestureDetecture = new GestureDetector(itemView.getContext(),this);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    long now = System.currentTimeMillis();
                    if (now - mLastClickTime < CLICK_TIME_INTERVAL) {
                        return;
                    }
                    mLastClickTime = now;
                    mNotes.remove(getBindingAdapterPosition());
                    notifyItemRemoved(getBindingAdapterPosition());
                }

            });
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
            mOnNoteListener.onNoteClick(getBindingAdapterPosition());
            return true;
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
