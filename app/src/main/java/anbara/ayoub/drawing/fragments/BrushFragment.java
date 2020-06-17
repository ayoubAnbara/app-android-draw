package anbara.ayoub.drawing.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;


import android.widget.FrameLayout;
import android.widget.ImageButton;



import com.xw.repo.BubbleSeekBar;

import anbara.ayoub.drawing.R;
import anbara.ayoub.drawing.adapter.ColorAdapter;
import anbara.ayoub.drawing.colorPicker.ColorPickerDialog;
import anbara.ayoub.drawing.interfaces.BrushFragmentListener;


public class BrushFragment extends BottomSheetDialogFragment implements ColorAdapter.ColorAdapterListener {

    BubbleSeekBar seekbar_brush_size, seekbar_brush_opacity;
    RecyclerView recycler_color;
    ImageButton btn_brush, btn_eraser;
    private FloatingActionButton chooseColor;
    private SharedPreferences sharedPreferences;
    ColorAdapter colorAdapter;
    BrushFragmentListener listener;

    static BrushFragment instance;

    public static BrushFragment getInstance() {
        if (instance == null)
            instance = new BrushFragment();
        return instance;

    }

    public void setListener(BrushFragmentListener listener) {
        this.listener = listener;
    }

    public BrushFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View itemView = inflater.inflate(R.layout.fragment_brush, container, false);
        seekbar_brush_size = itemView.findViewById(R.id.seekbar_brush_size);
        seekbar_brush_opacity = itemView.findViewById(R.id.seekbar_brush_opacity);
        btn_brush = itemView.findViewById(R.id.floatingBtn_brush);
        btn_eraser = itemView.findViewById(R.id.floatingBtn_eraser);
        chooseColor = itemView.findViewById(R.id.choose_color);

        recycler_color = itemView.findViewById(R.id.recycler_color);
        recycler_color.setHasFixedSize(true);
        recycler_color.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        colorAdapter = new ColorAdapter(getContext(), this);
        recycler_color.setAdapter(colorAdapter);

        //event
        seekbar_brush_opacity.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListener() {
            @Override
            public void onProgressChanged(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat, boolean fromUser) {
                listener.onBrushOpacityChangedListener(progress);
            }

            @Override
            public void getProgressOnActionUp(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {

            }

            @Override
            public void getProgressOnFinally(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat, boolean fromUser) {

            }
        });
//        seekbar_brush_opacity.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                listener.onBrushOpacityChangedListener(progress);
//
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//
//            }
//        });
        seekbar_brush_size.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListener() {
            @Override
            public void onProgressChanged(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat, boolean fromUser) {
                listener.onBrushSizeChangedListener(progress);
            }

            @Override
            public void getProgressOnActionUp(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {

            }

            @Override
            public void getProgressOnFinally(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat, boolean fromUser) {

            }
        });
        btn_brush.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                btn_eraser.setElevation(-10);
            }
            listener.onBrushClickListener();
        });
        btn_eraser.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                btn_brush.setElevation(-10);
            }
            listener.onEraserClickListener();
        });
        chooseColor.setOnClickListener(v -> showColorPickerDialogDemo());

        return itemView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                BottomSheetDialog dialog = (BottomSheetDialog) getDialog();
//                FrameLayout bottomSheet = dialog.findViewById(android.support.design.R.id.design_bottom_sheet);
                FrameLayout bottomSheet = dialog.findViewById(R.id.design_bottom_sheet);
                BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                behavior.setPeekHeight(0);
            }
        });
    }

    @Override
    public void onColorselected(int color) {
        listener.onBrushColorChangedListener(color);
        chooseColor.setBackgroundTintList(ColorStateList.valueOf(color));
    }


    private void showColorPickerDialogDemo() {

        int initialColor = Color.WHITE;

        ColorPickerDialog colorPickerDialog = new ColorPickerDialog(getActivity(), initialColor, new ColorPickerDialog.OnColorSelectedListener() {

            @Override
            public void onColorSelected(int color) {
                listener.onBrushColorChangedListener(color);
                chooseColor.setBackgroundTintList(ColorStateList.valueOf(color));
                if (getActivity() != null) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("colorBrush", color);
                    editor.apply();

                }
            }

        });
        colorPickerDialog.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() != null) {
            sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);

            int colorBrush = sharedPreferences.getInt("colorBrush", -1);
            if (colorBrush != -1)
                chooseColor.setBackgroundTintList(ColorStateList.valueOf(colorBrush));
        }
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
}
