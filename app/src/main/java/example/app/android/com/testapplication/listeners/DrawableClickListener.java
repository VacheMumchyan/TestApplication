package example.app.android.com.testapplication.listeners;

public interface DrawableClickListener {
    enum DrawablePosition { TOP, BOTTOM, LEFT, RIGHT };
    void onClick(DrawablePosition target);
    }