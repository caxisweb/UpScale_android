package sa.upscale.coworking.CustomFont;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

/**
 * Created by DELL on 01-02-2016.
 */
public class MyAutocomplite extends AutoCompleteTextView {

    public MyAutocomplite(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public MyAutocomplite(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyAutocomplite(Context context) {
        super(context);
        init();
    }

    public void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "HelveticaNeueLTArabic-Light_0.ttf");
        setTypeface(tf, 1);

    }

}
