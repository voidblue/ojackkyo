package ojackkyo.nero.ojackkyo.List;

/**
 * Created by wjdal on 2018-07-05.
 */

import android.graphics.drawable.Drawable;

public class ListVO {
    private Drawable img;
    private String Title;
    private String context;
    private String contents;
    private String nickname;

    public Drawable getImg() {
        return img;
    }

    public void setImg(Drawable img) {
        this.img = img;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }
}
