package th.co.siamkubota.kubota.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by sipangka on 22/11/2558.
 */
public class Question implements Parcelable {
    private String title;
    private String detail;
    private  boolean answer;
    private boolean complete;

    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Question(String title) {
        this.title = title;
    }
    public Question(int id, String title) {
        this.id = id;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public boolean isAnswer() {
        return answer;
    }

    public void setAnswer(boolean answer) {
        this.answer = answer;
    }

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }


///////////////////////////////////////////////////////////// implement parcelable


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.title);
        dest.writeString(this.detail);
        dest.writeByte(answer ? (byte) 1 : (byte) 0);
        dest.writeByte(complete ? (byte) 1 : (byte) 0);

    }

    protected Question(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.detail = in.readString();
        this.answer = in.readByte() != 0;
        this.complete = in.readByte() != 0;
    }

    public static final Creator<Question> CREATOR = new Creator<Question>() {
        public Question createFromParcel(Parcel source) {
            return new Question(source);
        }

        public Question[] newArray(int size) {
            return new Question[size];
        }
    };
}
