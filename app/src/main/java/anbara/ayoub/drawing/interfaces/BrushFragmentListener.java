package anbara.ayoub.drawing.interfaces;

public interface BrushFragmentListener {
    void onBrushSizeChangedListener(float size);

    void onBrushOpacityChangedListener(int opacity);

    void onBrushColorChangedListener(int color);
    void onBrushClickListener();
    void onEraserClickListener();
}
