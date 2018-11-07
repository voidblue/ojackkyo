package ojackkyo.nero.ojackkyo.List;

/**
 * Created by wjdal on 2018-11-01.
 */

public class CommentList {
    private String time;
    private String contents;
    private String nickname;

    public CommentList(String[] contents) {
        this.contents = contents[0];
        this.nickname = contents[1];
        this.time = contents[2].substring(0,contents[2].length()-2);
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
