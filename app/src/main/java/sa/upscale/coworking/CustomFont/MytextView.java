package sa.upscale.coworking.CustomFont;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by DELL on 01-02-2016.
 */
public class MytextView extends TextView {

    public MytextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public MytextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MytextView(Context context) {
        super(context);
        init();
    }

    public void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "OpenSans-Regular.ttf");
        setTypeface(tf, 1);

    }

}
