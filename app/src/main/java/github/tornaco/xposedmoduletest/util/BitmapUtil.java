package github.tornaco.xposedmoduletest.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.AdaptiveIconDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.content.ContextCompat;

import github.tornaco.xposedmoduletest.R;

/**
 * Created by guohao4 on 2017/10/24.
 * Email: Tornaco@163.com
 */

public abstract class BitmapUtil {

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    public static Bitmap getBitmap(VectorDrawable vectorDrawable) {
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),
                vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        vectorDrawable.draw(canvas);
        return bitmap;
    }

    public static Bitmap getBitmap(VectorDrawableCompat vectorDrawable) {
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),
                vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        vectorDrawable.draw(canvas);
        return bitmap;
    }

    public static Bitmap getBitmap(AdaptiveIconDrawable adaptiveIconDrawable) {
        Bitmap bitmap = Bitmap.createBitmap(adaptiveIconDrawable.getIntrinsicWidth(),
                adaptiveIconDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        adaptiveIconDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        adaptiveIconDrawable.draw(canvas);
        return bitmap;
    }

    @Nullable
    public static Bitmap getBitmap(Context context, @DrawableRes int drawableResId) {
        Drawable drawable = ContextCompat.getDrawable(context, drawableResId);
        return getBitmap(context, drawable);
    }

    @Nullable
    public static Bitmap getBitmap(Context context, Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        } else if (drawable instanceof VectorDrawableCompat) {
            return getBitmap((VectorDrawableCompat) drawable);
        } else if (drawable instanceof VectorDrawable) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                return getBitmap((VectorDrawable) drawable);
            }
            return null;
        } else if (drawable instanceof AdaptiveIconDrawable) {
            AdaptiveIconDrawable ad = (AdaptiveIconDrawable) drawable;
            return getBitmap(ad);

        } else {
            throw new IllegalArgumentException("Unsupported drawable type:" + drawable);
        }
    }

    public static Bitmap createDisabledAppLauncherIcon(Context context, Bitmap original) {
        Bitmap res = Bitmap.createBitmap(original.getWidth(), original.getHeight(), original.getConfig());
        Canvas canvas = new Canvas(res);
        canvas.drawBitmap(original, new Matrix(), null);
        Bitmap our = getBitmap(context, R.mipmap.ic_launcher_round);
        if (our == null) return original;
        Matrix matrix = new Matrix();
        matrix.postScale(0.2f, 0.2f);
        float padding = 3;
        Bitmap scaled = Bitmap.createBitmap(our, 0, 0, our.getWidth(), our.getHeight(),
                matrix, true);
        canvas.drawBitmap(scaled, original.getWidth() - scaled.getWidth()
                - padding, original.getHeight() - scaled.getHeight() - padding, null);
        return res;
    }
}
