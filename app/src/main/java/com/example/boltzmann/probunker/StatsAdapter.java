package com.example.boltzmann.probunker;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

public class StatsAdapter extends RecyclerView.Adapter<StatsAdapter.StatsViewHolder> {
    private Context mContext;
    private Cursor mCursor;
    private float mMinPercent;

    public StatsAdapter(Context context, Cursor cursor , float minPercent){
        mContext=context;
        mCursor = cursor;
        mMinPercent = minPercent;
    }

    @NonNull
    @Override
    public StatsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.stats_card, parent, false);
        return new StatsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StatsViewHolder holder, int position) {
        if (!mCursor.moveToPosition(position)){
            return;
        }

        String subjectName = mCursor.getString(mCursor.getColumnIndex("NAME"));
        int total = mCursor.getInt(mCursor.getColumnIndex("TOTAL"));
        int bunked = mCursor.getInt(mCursor.getColumnIndex("BUNK"));

        holder.nameText.setText(subjectName);
        int available = forN(total,bunked,mMinPercent);
        if(available<1){
            available=0;
        }
        holder.bunkText.setText(String.valueOf(available));

    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    public class StatsViewHolder extends RecyclerView.ViewHolder{
        public TextView nameText;
        public TextView bunkText;

        public StatsViewHolder(@NonNull View itemView) {
            super(itemView);

            nameText = itemView.findViewById(R.id.subject_name);
            bunkText = itemView.findViewById(R.id.subject_available_bunks);
        }
    }
    private int forN(int total,int bunked,float N){
        int attended = total-bunked;
        return (int) ((attended/N)-total-1);
    }

}
