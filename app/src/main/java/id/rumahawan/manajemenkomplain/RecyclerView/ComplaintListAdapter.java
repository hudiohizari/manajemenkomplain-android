package id.rumahawan.manajemenkomplain.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.rumahawan.manajemenkomplain.R;

public class ComplaintListAdapter extends RecyclerView.Adapter<ComplaintListAdapter.ComplaintListViewHolder> {
    private Context context;
    private ArrayList<ComplaintList> dataList;

    public ComplaintListAdapter(Context context, ArrayList<ComplaintList> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public ComplaintListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.complaint_list_item, parent, false);
        return new ComplaintListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ComplaintListViewHolder holder, int position) {
        holder.tvName.setText(dataList.get(position).getNama());
        if (position % 2 == 1){
            holder.vBackground.setBackgroundColor(context.getResources().getColor(R.color.colorTranslucentBlue));
        }
        else{
            holder.vBackground.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
        }
        holder.ivStatus.setImageDrawable(context.getResources().getDrawable(dataList.get(position).getDrawableStatus()));
    }

    @Override
    public int getItemCount() {
        return (dataList != null) ? dataList.size() : 0;
    }

    class ComplaintListViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.vBackground)
        ConstraintLayout vBackground;
        @BindView(R.id.tvName)
        TextView tvName;
        @BindView(R.id.ivStatus)
        ImageView ivStatus;

        ComplaintListViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}
