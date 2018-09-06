package com.example.boltzmann.probunker;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder> {
    private Context mContext;
    private Cursor mCursor;
    public CardAdapter(Context context, Cursor cursor){
        mContext=context;
        mCursor=cursor;
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.mycard,parent,false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        if(!mCursor.moveToPosition(position)){
            return;
        }
        String name = mCursor.getString(mCursor.getColumnIndex("NAME"));
        int total = mCursor.getInt(mCursor.getColumnIndex("TOTAL"));
        int bunked = mCursor.getInt(mCursor.getColumnIndex("BUNK"));
        holder.nameText.setText(name);
        holder.totalText.setText(Integer.toString(total));
        holder.bunkedText.setText(Integer.toString(bunked));
    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    public class CardViewHolder extends RecyclerView.ViewHolder{
        public TextView nameText;
        public TextView totalText;
        public TextView bunkedText;
        public CardViewHolder(@NonNull View itemView) {
            super(itemView);
            nameText= itemView.findViewById(R.id.name);
            totalText = itemView.findViewById(R.id.total_num);
            bunkedText = itemView.findViewById(R.id.bunked_num);
        }
    }
    public void swapCursor(Cursor newCursor){
        if(mCursor!=null){
            mCursor.close();
        }
        mCursor=newCursor;
        if(newCursor!=null){
            notifyDataSetChanged();
        }
    }


}
