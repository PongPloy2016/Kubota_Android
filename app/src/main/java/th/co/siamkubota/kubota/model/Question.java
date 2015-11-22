package th.co.siamkubota.kubota.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by sipangka on 22/11/2558.
 */
public class Question implements Parcelable {
    private String title;
    private String detail;

    public Question(String title) {
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

    ///////////////////////////////////////////////////////////// implement parcelable


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.detail);
    }

    protected Question(Parcel in) {
        this.title = in.readString();
        this.detail = in.readString();
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
