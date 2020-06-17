package anbara.ayoub.drawing.fragments;






import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.FrameLayout;
import anbara.ayoub.drawing.R;
import anbara.ayoub.drawing.adapter.NumbersAdapter;
import anbara.ayoub.drawing.interfaces.AddNumberListener;


public class NumberFragment extends BottomSheetDialogFragment implements NumbersAdapter.NumberAdapterListener {

        RecyclerView recycler_numbers;
        Button btn_add_number;
        int frame_selected=-1;
        AddNumberListener listener;


        static anbara.ayoub.drawing.fragments.NumberFragment instance;

        public static void setInstance(anbara.ayoub.drawing.fragments.FrameFragment instance) {
            anbara.ayoub.drawing.fragments.FrameFragment.instance = instance;
        }

        public static anbara.ayoub.drawing.fragments.NumberFragment getInstance() {
            if (instance==null)
                instance=new anbara.ayoub.drawing.fragments.NumberFragment();
            return instance;
        }

        public void setListener(AddNumberListener listener) {
            this.listener = listener;
        }

        public NumberFragment() {
            // Required empty public constructor
        }


        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            // Inflate the layout for this fragment
            View itemView= inflater.inflate(R.layout.fragment_number, container, false);

            recycler_numbers=itemView.findViewById(R.id.recycler_numbers);
            btn_add_number=itemView.findViewById(R.id.btn_add_number);
            recycler_numbers.setHasFixedSize(true);
            recycler_numbers.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
            recycler_numbers.setAdapter(new NumbersAdapter(getContext(),this));

            btn_add_number.setOnClickListener(view -> listener.onAddNumber(frame_selected));
            return itemView;
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

    @Override
    public void onNumberSelected(int number) {
        frame_selected=number;
    }
}
