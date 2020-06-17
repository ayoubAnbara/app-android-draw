package anbara.ayoub.drawing.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.FrameLayout;


import anbara.ayoub.drawing.R;
import anbara.ayoub.drawing.adapter.AlphabetAdapter;
import anbara.ayoub.drawing.interfaces.AddFrameListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class FrameFragment extends BottomSheetDialogFragment implements AlphabetAdapter.FrameAdapterListener {

    RecyclerView recycler_frame;
    Button btn_add_frame;
    int frame_selected=-1;
    AddFrameListener listener;


    static FrameFragment instance;

    public static void setInstance(FrameFragment instance) {
        FrameFragment.instance = instance;
    }

    public static FrameFragment getInstance() {
        if (instance==null)
            instance=new FrameFragment();
        return instance;
    }

    public void setListener(AddFrameListener listener) {
        this.listener = listener;
    }

    public FrameFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View itemView= inflater.inflate(R.layout.fragment_frame, container, false);

        recycler_frame=itemView.findViewById(R.id.recycler_frame);
        btn_add_frame=itemView.findViewById(R.id.btn_add_letter);
        recycler_frame.setHasFixedSize(true);
        recycler_frame.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        recycler_frame.setAdapter(new AlphabetAdapter(getContext(),this));

        btn_add_frame.setOnClickListener(view -> listener.onAddFrame(frame_selected));
        return itemView;
    }

    @Override
    public void onFrameSelected(int frame) {
        frame_selected=frame;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            BottomSheetDialog dialog = (BottomSheetDialog) getDialog();
            FrameLayout bottomSheet = dialog.findViewById(R.id.design_bottom_sheet);
            BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
            behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            behavior.setPeekHeight(0);
        });
    }

}
