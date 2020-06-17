package anbara.ayoub.drawing.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import anbara.ayoub.drawing.R;

import java.util.ArrayList;
import java.util.List;

public class AlphabetAdapter extends RecyclerView.Adapter<AlphabetAdapter.FrameViewHolder> {
    Context context;
    List<Integer> frameList;
    FrameAdapterListener listener;
    int row_selected=-1;


    public AlphabetAdapter(Context context, FrameAdapterListener listener) {
        this.context = context;
        this.frameList = getFrameList();
        this.listener = listener;
    }

    private List<Integer> getFrameList() {
        List<Integer> result=new ArrayList<>();
// لائحة صور الحروف

        result.add(R.drawable.a);
        result.add(R.drawable.b);
        result.add(R.drawable.c);
        result.add(R.drawable.d);
        result.add(R.drawable.e);
       result.add(R.drawable.f);
        result.add(R.drawable.g);
        result.add(R.drawable.h);
        result.add(R.drawable.i);
        result.add(R.drawable.g);
        result.add(R.drawable.k);
        result.add(R.drawable.l);
        result.add(R.drawable.m);
        result.add(R.drawable.n);
        result.add(R.drawable.o);
        result.add(R.drawable.p);
        result.add(R.drawable.q);
        result.add(R.drawable.r);
        result.add(R.drawable.s);
        result.add(R.drawable.t);
        result.add(R.drawable.u);
        result.add(R.drawable.v);
        result.add(R.drawable.w);
        result.add(R.drawable.x);
        result.add(R.drawable.y);
        result.add(R.drawable.z);
        return result;
    }

    @NonNull
    @Override
    public FrameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        View itemView= LayoutInflater.from(context).inflate(R.layout.frame_item,parent,false);


        return new FrameViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FrameViewHolder holder, int position) {

        if (row_selected==position)
            holder.img_check.setVisibility(View.VISIBLE);
        else holder.img_check.setVisibility(View.INVISIBLE);
        holder.img_frame.setImageResource(frameList.get(position));
       // Glide.with(context).load(frameList.get(position)).asBitmap().override(1080, 600).into(mImageView);

    }

    @Override
    public int getItemCount() {
        return frameList.size();
    }

    public class FrameViewHolder extends RecyclerView.ViewHolder{
        ImageView img_check,img_frame;

        public FrameViewHolder(@NonNull View itemView) {
            super(itemView);
            img_check=itemView.findViewById(R.id.img_check);
            img_frame=itemView.findViewById(R.id.img_frame);
            itemView.setOnClickListener(view -> {
                listener.onFrameSelected(frameList.get(getAdapterPosition()));
                row_selected=getAdapterPosition();
                notifyDataSetChanged();


            });


        }
    }
    public interface FrameAdapterListener{
        void onFrameSelected(int frame);
    }
}

