package anbara.ayoub.drawing.fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import anbara.ayoub.drawing.R;
import anbara.ayoub.drawing.adapter.ColorAdapter;
import anbara.ayoub.drawing.adapter.FontAdapter;
import anbara.ayoub.drawing.colorPicker.ColorPickerDialog;
import anbara.ayoub.drawing.interfaces.AddTextFragmentListener;


public class AddTextFragment extends BottomSheetDialogFragment implements ColorAdapter.ColorAdapterListener, FontAdapter.FontAdapterClickListener {

    int colorSelected = Color.parseColor("#000000");//default
    AddTextFragmentListener listener;
    EditText edit_add_text;
    RecyclerView recycler_color, recycler_font;
    ImageButton btn_done;
    Typeface typefaceSelected = Typeface.DEFAULT;

    static AddTextFragment instance;
    private Context mContext;
    private FloatingActionButton chooseColorText;
    private SharedPreferences sharedPreferences;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }



    @Override
    public void onDetach() {
        super.onDetach();
        mContext = null;
    }
    public static AddTextFragment getInstance() {
        if (instance == null)
            instance = new AddTextFragment();
        return instance;
    }

    public void setListener(AddTextFragmentListener listener) {
        this.listener = listener;
    }

    public AddTextFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View itemView = inflater.inflate(R.layout.fragment_add_text, container, false);
        edit_add_text = itemView.findViewById(R.id.edit_add_text);
        btn_done = itemView.findViewById(R.id.btn_done);
        chooseColorText=itemView.findViewById(R.id.choose_color_text);
        recycler_color = itemView.findViewById(R.id.recycler_color);
        recycler_color.setHasFixedSize(true);
        recycler_color.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recycler_font = itemView.findViewById(R.id.recycler_font);
        recycler_font.setHasFixedSize(true);
        recycler_font.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        ColorAdapter colorAdapter = new ColorAdapter(getContext(), this);
        recycler_color.setAdapter(colorAdapter);

        FontAdapter fontAdapter = new FontAdapter(getContext(), this);
        recycler_font.setAdapter(fontAdapter);

        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onAddTextButtonClick(typefaceSelected, edit_add_text.getText().toString(), colorSelected);
                AddTextFragment.this.dismiss();

            }
        });
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
        //Toast.makeText(mContext, "ckkk", Toast.LENGTH_SHORT).show();
        edit_add_text.setTextColor(color);
        colorSelected = color;// set Color when user select
        if (getActivity() != null) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("colorText", color);
            editor.apply();

        }

    }

    @Override
    public void onFontSelected(String fontName) {
        typefaceSelected = Typeface.createFromAsset(mContext.getAssets()
                , new StringBuilder("fonts/")
                        .append(fontName).toString());
        edit_add_text.setTypeface(typefaceSelected);


    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                BottomSheetDialog dialog = (BottomSheetDialog) getDialog();
                FrameLayout bottomSheet = dialog.findViewById(R.id.design_bottom_sheet);
                BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                behavior.setPeekHeight(0);
            }
        });
    }
    private void showColorPickerDialogDemo() {

        int initialColor = Color.WHITE;

        ColorPickerDialog colorPickerDialog = new ColorPickerDialog(getActivity(), initialColor, new ColorPickerDialog.OnColorSelectedListener() {

            @Override
            public void onColorSelected(int color) {
                onColorselected(color);
               // chooseColorText.setBackgroundColor(color);
                chooseColorText.setBackgroundTintList(ColorStateList.valueOf(color));
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
               // chooseColorText.setBackgroundColor(colorBrush);
                chooseColorText.setBackgroundTintList(ColorStateList.valueOf(colorBrush));
        }
    }

}
