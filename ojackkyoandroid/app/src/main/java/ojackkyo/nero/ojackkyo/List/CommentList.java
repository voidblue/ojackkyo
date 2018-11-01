package ojackkyo.nero.ojackkyo.List;

/**
 * Created by wjdal on 2018-11-01.
 */

public class CommentList {
    String contents;
    String nickname;

    public CommentList(String contents) {
        this.contents = contents;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
