package app.hb.mylocalevents.util;

import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

public class ImageBindingAdapter {

    @BindingAdapter(value = {"android:src", "bind:articlesIndicator"})
    public static void bindArticleImage(ImageView articleImage, String url, final ProgressBar progressWheel) {
        if (url != null) {
            if (!TextUtils.isEmpty(url)) {
                Glide.with(articleImage.getContext())
                        .load(url)
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target,
                                                        boolean isFirstResource) {
                                progressWheel.setVisibility(View.GONE);

                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target,
                                                           DataSource dataSource, boolean isFirstResource) {
                                progressWheel.setVisibility(View.GONE);
                                return false;
                            }
                        })
                        .into(articleImage);
            }
        }
    }
}