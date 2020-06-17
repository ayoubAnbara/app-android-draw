package anbara.ayoub.drawing.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import anbara.ayoub.drawing.R;

public class NumbersAdapter extends RecyclerView.Adapter<NumbersAdapter.NumbersViewHolder> {
    Context context;
    List<Integer> numbersList;
    NumbersAdapter.NumberAdapterListener listener;
    int row_selected = -1;


    public NumbersAdapter(Context context, NumbersAdapter.NumberAdapterListener listener) {
        this.context = context;
        this.numbersList = getNumbersList();
        this.listener = listener;
    }

    private List<Integer> getNumbersList() {
        List<Integer> result = new ArrayList<>();
// لائحة صور الارقام
        result.add(R.drawable.zero);
        result.add(R.drawable.one);
        result.add(R.drawable.two);
        result.add(R.drawable.tree);
        result.add(R.drawable.four);
        result.add(R.drawable.five);
        result.add(R.drawable.six);
        result.add(R.drawable.seven);
        result.add(R.drawable.hight);
        result.add(R.drawable.nine);
        result.add(R.drawable.addition);
        result.add(R.drawable.substraction);
        result.add(R.drawable.multiplication);
        result.add(R.drawable.division);
        result.add(R.drawable.equal);
        return result;
    }

    @NonNull
    @Override
    public NumbersAdapter.NumbersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.number_item, parent, false);


        return new NumbersAdapter.NumbersViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NumbersAdapter.NumbersViewHolder holder, int position) {

        if (row_selected == position)
            holder.img_check.setVisibility(View.VISIBLE);
        else holder.img_check.setVisibility(View.INVISIBLE);
        holder.img_number.setImageResource(numbersList.get(position));

    }

    @Override
    public int getItemCount() {
        return numbersList.size();
    }

    public class NumbersViewHolder extends RecyclerView.ViewHolder{
        ImageView img_check,img_number;

        public NumbersViewHolder(@NonNull View itemView) {
            super(itemView);
            img_check=itemView.findViewById(R.id.img_check);
            img_number=itemView.findViewById(R.id.img_number);
            itemView.setOnClickListener(view -> {
                listener.onNumberSelected(numbersList.get(getAdapterPosition()));
                row_selected=getAdapterPosition();
                notifyDataSetChanged();


            });


        }
    }
    public interface NumberAdapterListener{
        void onNumberSelected(int number);
    }
}


