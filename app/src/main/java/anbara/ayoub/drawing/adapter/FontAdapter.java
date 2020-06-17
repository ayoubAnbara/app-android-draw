package anbara.ayoub.drawing.adapter;

import android.content.Context;
import android.graphics.Typeface;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;



import java.util.ArrayList;
import java.util.List;

import anbara.ayoub.drawing.R;

public class FontAdapter extends RecyclerView.Adapter<FontAdapter.FontViewHolder> {
    Context context;
    FontAdapterClickListener listener;
    List<String> fontList;
    int row_selected = -1;


    public FontAdapter(Context context, FontAdapterClickListener listener) {
        this.context = context;
        this.listener = listener;
        fontList = LoadFontList();
    }

    private List<String> LoadFontList() {
        List<String> result = new ArrayList<>();
        //لائحة الخطوط
        result.add("Italianno.ttf");
        result.add("RobotoSlab-Bold.ttf");
        result.add("Notable-Regular.ttf");

        return result;
    }

    @NonNull
    @Override
    public FontViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.font_item, parent, false);


        return new FontViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FontViewHolder holder, int position) {
        if (row_selected == position)
            holder.img_check.setVisibility(View.VISIBLE);
        else
            holder.img_check.setVisibility(View.INVISIBLE);

        Typeface typeface = Typeface.createFromAsset(context.getAssets(), new StringBuilder("fonts/")
                .append(fontList.get(position)).toString());
        holder.txt_font_name.setText(fontList.get(position));
        holder.txt_font_demo.setTypeface(typeface);
    }

    @Override
    public int getItemCount() {
        return fontList.size();
    }

    public class FontViewHolder extends RecyclerView.ViewHolder {
        TextView txt_font_name, txt_font_demo;
        ImageView img_check;

        public FontViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_font_name = itemView.findViewById(R.id.txt_font_name);
            txt_font_demo = itemView.findViewById(R.id.txt_font_demo);
            img_check = itemView.findViewById(R.id.img_check);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onFontSelected(fontList.get(getAdapterPosition()));
                    row_selected = getAdapterPosition();
                    notifyDataSetChanged();

                }
            });
        }
    }

    public interface FontAdapterClickListener {
        void onFontSelected(String fontName);
    }
}
