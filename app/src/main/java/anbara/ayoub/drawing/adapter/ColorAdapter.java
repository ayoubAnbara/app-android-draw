package anbara.ayoub.drawing.adapter;

import android.content.Context;
import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



import java.util.ArrayList;
import java.util.List;

import anbara.ayoub.drawing.R;

public class ColorAdapter extends RecyclerView.Adapter<ColorAdapter.ColorViewHolder> {
    Context context;
    List<Integer> colorList;
    ColorAdapterListener listener;

    public ColorAdapter(Context context, ColorAdapterListener listener) {
        this.context = context;
        this.colorList = genColorList();
        this.listener = listener;
    }

    @NonNull
    @Override
    public ColorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.color_item, parent, false);


        return new ColorViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ColorViewHolder holder, int position) {
        holder.color_section.setCardBackgroundColor(colorList.get(position));


    }

    @Override
    public int getItemCount() {
        return colorList.size();
    }

    public class ColorViewHolder extends RecyclerView.ViewHolder {

        public CardView color_section;


        public ColorViewHolder(@NonNull View itemView) {
            super(itemView);
            color_section = itemView.findViewById(R.id.color_section);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onColorselected(colorList.get(getAdapterPosition()));

                }
            });
        }
    }

    private List<Integer> genColorList() {
        List<Integer> colorList = new ArrayList<>();
        // لائحة الالوان مشتركة بين ايقونات تغيير الخلفية
        //و الرسم والكتابة
        colorList.add(Color.parseColor("#000000"));
        colorList.add(Color.parseColor("#FFFFFF"));
        colorList.add(Color.parseColor("#40e0d0"));
        colorList.add(Color.parseColor("#4c14ba"));
        colorList.add(Color.parseColor("#a014ba"));
        colorList.add(Color.parseColor("#f6546a"));
        colorList.add(Color.parseColor("#0066cc"));
        colorList.add(Color.parseColor("#fa8072"));
        colorList.add(Color.parseColor("#cccccc"));
        colorList.add(Color.parseColor("#f5e44c"));
        colorList.add(Color.parseColor("#bcdceb"));
        colorList.add(Color.parseColor("#600707"));
        colorList.add(Color.parseColor("#fff68f"));
        colorList.add(Color.parseColor("#bc3e1a"));
        colorList.add(Color.parseColor("#2e1e25"));
        colorList.add(Color.parseColor("#b96554"));
        colorList.add(Color.parseColor("#ffc125"));
        colorList.add(Color.parseColor("#d3ffce"));
        colorList.add(Color.parseColor("#0066cc"));
        colorList.add(Color.parseColor("#d87c1e"));
        colorList.add(Color.parseColor("#d825ee"));
        colorList.add(Color.parseColor("#bab414"));
        colorList.add(Color.parseColor("#ff2424"));
        colorList.add(Color.parseColor("#17edff"));


        return colorList;
    }

    public interface ColorAdapterListener {
        void onColorselected(int color);

    }
}
