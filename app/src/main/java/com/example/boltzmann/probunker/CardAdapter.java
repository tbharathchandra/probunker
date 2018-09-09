package com.example.boltzmann.probunker;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder> {
    private Context mContext;
    private Cursor mCursor;
    private butttonsAdapetrListener mlistener;
    public CardAdapter(Context context, Cursor cursor ,butttonsAdapetrListener listener){
        mContext=context;
        mCursor=cursor;
        mlistener=listener;
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
        int color = mCursor.getInt(mCursor.getColumnIndex("COLOR"));
        int id= mCursor.getInt(mCursor.getColumnIndex("_id"));
        holder.nameText.setText(name);
        holder.totalText.setText(Integer.toString(total));
        holder.bunkedText.setText(Integer.toString(bunked));
        holder.colorText.setBackgroundColor(color);
        holder.delete.setTag(id);
        holder.plusTotal.setTag(id);
        holder.plusBunk.setTag(id);
        holder.minusTotal.setTag(id);
        holder.minusBunk.setTag(id);
    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    public class CardViewHolder extends RecyclerView.ViewHolder{
        public TextView nameText;
        public TextView totalText;
        public TextView bunkedText;
        public TextView colorText;
        public Button delete;
        public Button plusTotal;
        public Button minusTotal;
        public Button plusBunk;
        public Button minusBunk;
        public CardViewHolder(@NonNull View itemView) {
            super(itemView);
            nameText= itemView.findViewById(R.id.name);
            totalText = itemView.findViewById(R.id.total_num);
            bunkedText = itemView.findViewById(R.id.bunked_num);
            colorText= itemView.findViewById(R.id.color);
            delete= itemView.findViewById(R.id.delete);
            plusTotal= itemView.findViewById(R.id.plus_total);
            minusTotal=itemView.findViewById(R.id.minus_total);
            plusBunk = itemView.findViewById(R.id.plus_bunk);
            minusBunk = itemView.findViewById(R.id.minus_bunk);
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int id=(int) view.getTag();
                    mlistener.deleteOnClick(id,getAdapterPosition());
                }
            });
            plusTotal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int id=(int) view.getTag();
                    mlistener.plusTotalOnClick(id,getAdapterPosition());
                }
            });
            minusTotal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int id = (int) view.getTag();
                    mlistener.minusTotalOnClick(id,getAdapterPosition());
                }
            });
            plusBunk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int id= (int) view.getTag();
                    mlistener.plusBunkOnClick(id,getAdapterPosition());
                }
            });
            minusBunk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int id= (int) view.getTag();
                    mlistener.minusBunkOnClick(id,getAdapterPosition());
                }
            });
        }
    }
    public interface butttonsAdapetrListener{
        void deleteOnClick(int id,long adapterpos);
        void plusTotalOnClick(int id,long adapterpos);
        void minusTotalOnClick(int id,long adapterpos);
        void plusBunkOnClick(int id,long adapterpos);
        void minusBunkOnClick(int id,long adapterpos);
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
