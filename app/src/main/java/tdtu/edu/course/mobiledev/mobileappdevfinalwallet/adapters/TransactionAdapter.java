package tdtu.edu.course.mobiledev.mobileappdevfinalwallet.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.LinkedList;

import tdtu.edu.course.mobiledev.mobileappdevfinalwallet.R;
import tdtu.edu.course.mobiledev.mobileappdevfinalwallet.pojos.Transaction;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.MyViewHolder> implements Filterable {

    private LinkedList<Transaction> transactions;
    private LinkedList<Transaction> fullItemList;
    private int searchPosition;


    public TransactionAdapter(LinkedList<Transaction> transactions, int searchPosition) {
        this.transactions = transactions;
        this.fullItemList = new LinkedList<>(transactions);
        this.searchPosition = searchPosition;
    }

    @NonNull
    @Override
    public TransactionAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transaction_recycler_view, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionAdapter.MyViewHolder holder, int position) {

        String currentDate = transactions.get(position).getDate();
        holder.txtDate.setText(currentDate);

        String currentAmount = transactions.get(position).getAmount();
        holder.txtAmount.setText(currentAmount + " VND");

        Boolean currentSign = transactions.get(position).isIncome();
        if (currentSign) {
            holder.txtSign.setText("+");
            holder.txtSign.setTextColor(Color.parseColor("#7CFF82"));
            holder.txtAmount.setTextColor(Color.parseColor("#7CFF82"));
        } else {
            holder.txtSign.setText("-");
            holder.txtSign.setTextColor(Color.parseColor("#FF6B6B"));
            holder.txtAmount.setTextColor(Color.parseColor("#FF6B6B"));
        }

        String currentCategory = transactions.get(position).getCategory();
        holder.txtCategory.setText(currentCategory);

        String currentAcc = transactions.get(position).getAccount();
        holder.txtAcc.setText(currentAcc);

        String currentNote = transactions.get(position).getNote();
        holder.txtNote.setText(currentNote);
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtId;
        public TextView txtDate;
        public TextView txtAmount;
        public TextView txtSign;
        public TextView txtCategory;
        public TextView txtAcc;
        public TextView txtNote;

        public MyViewHolder(View itemView) {
            super(itemView);
            txtDate = itemView.findViewById(R.id.txtDate);
            txtAmount = itemView.findViewById(R.id.txtAmount);
            txtSign = itemView.findViewById(R.id.txtSign);
            txtCategory = itemView.findViewById(R.id.txtCategory);
            txtAcc = itemView.findViewById(R.id.txtAcc);
            txtNote = itemView.findViewById(R.id.txtNote);
        }
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    private Filter filter= new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            LinkedList<Transaction> filteredList = new LinkedList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(fullItemList);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();


                for (Transaction item: fullItemList) {
                    String currentSearchValue;

                    if (searchPosition == 1) {
                        currentSearchValue = item.getAmount();
                    } else if (searchPosition == 0) {
                        currentSearchValue = item.getCategory();
                    } else {
                        currentSearchValue = item.getDate();
                    }


                    if(currentSearchValue.toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            transactions.clear();
            transactions.addAll((LinkedList<Transaction>) results.values);
            notifyDataSetChanged();
        }
    };
}