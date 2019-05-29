package bgapps.rafine.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import bgapps.rafine.Model.Foods;
import bgapps.rafine.R;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder> {
    private Context mContext;
    private OnItemClickListener mListener;
    private ArrayList<Foods> mFoodList;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public RecyclerViewAdapter(Context context, ArrayList<Foods> foodList) {
        mContext = context;
        mFoodList = foodList;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int ViewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.recyclerview_item, parent, false);
        RecyclerViewHolder recyclerViewHolder = new RecyclerViewHolder(v,mListener);
        return recyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        Foods food = mFoodList.get(position);
        String foodName = food.getFoodName();
        String foodCal = food.getFoodCal();

        holder.mFoodName.setText(foodName);
        holder.mFoodCal.setText(foodCal);

    }

    @Override
    public int getItemCount() {
        return mFoodList.size();
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {

        public TextView mFoodName;
        public TextView mFoodCal;

        public RecyclerViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            mFoodCal = itemView.findViewById(R.id.foodCal);
            mFoodName = itemView.findViewById(R.id.foodName);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }


            });
        }

    }
}
