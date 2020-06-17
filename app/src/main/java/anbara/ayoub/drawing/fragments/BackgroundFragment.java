package anbara.ayoub.drawing.fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageButton;


import anbara.ayoub.drawing.R;
import anbara.ayoub.drawing.adapter.ColorAdapter;
import anbara.ayoub.drawing.colorPicker.ColorPickerDialog;
import anbara.ayoub.drawing.interfaces.BackgroundFragmentListener;


public class BackgroundFragment extends BottomSheetDialogFragment implements ColorAdapter.ColorAdapterListener {
    BackgroundFragmentListener listener;
    RecyclerView recycler_color;
    ColorAdapter colorAdapter;
    static BackgroundFragment instance;
    private ImageButton chooseColorText;
    private SharedPreferences sharedPreferences;

    public static BackgroundFragment getInstance() {
        if (instance == null)
            instance = new BackgroundFragment();
        return instance;

    }


    public BackgroundFragment() {
        // Required empty public constructor
    }

    public void setListener(BackgroundFragmentListener listener) {
        this.listener = listener;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
      final   View itemView = inflater.inflate(R.layout.fragment_background, container, false);
        itemView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                itemView.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                BottomSheetDialog dialog = (BottomSheetDialog) getDialog();
                FrameLayout bottomSheet =
                        dialog.findViewById(R.id.design_bottom_sheet);
                BottomSheetBehavior behavior ;
                if (bottomSheet != null) {
                    behavior = BottomSheetBehavior.from(bottomSheet);

                    behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    behavior.setPeekHeight(0); // Remove this line to hide a dark background if you manually hide the dialog.
                }}
        });

        chooseColorText=itemView.findViewById(R.id.choose_color);
        recycler_color = itemView.findViewById(R.id.recycler_color);
        recycler_color.setHasFixedSize(true);
        recycler_color.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        colorAdapter = new ColorAdapter(getContext(), this);
        recycler_color.setAdapter(colorAdapter);
        chooseColorText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showColorPickerDialogDemo();
            }
        });

        return itemView;
    }

    @Override
    public void onColorselected(int color) {
        listener.onBackgroundColorChangedListener(color);
        if (getActivity() != null) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("colorText", color);
            editor.apply();

        }
    }
    private void showColorPickerDialogDemo() {

        int initialColor = Color.WHITE;

        ColorPickerDialog colorPickerDialog = new ColorPickerDialog(getActivity(), initialColor, new ColorPickerDialog.OnColorSelectedListener() {

            @Override
            public void onColorSelected(int color) {
                onColorselected(color);
                chooseColorText.setBackgroundColor(color);
            }

        });
        colorPickerDialog.show();
    }
    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() != null) {
            sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);

            int colorBrush = sharedPreferences.getInt("colorText", -1);
            if (colorBrush != -1)
                chooseColorText.setBackgroundColor(colorBrush);
        }
    }
}
