package th.co.siamkubota.kubota.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by sipangka on 22/11/2558.
 */
public class Photo implements Parcelable{
    private String title;
    private String path;
    private Date date;

    public Photo(String title) {
        this.title = title;
        this.date = new Date();
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    /////////////////////////////////////////////////////////////////////////////// implement parcelable


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.path);
        dest.writeLong(date != null ? date.getTime() : -1);
    }

    protected Photo(Parcel in) {
        this.title = in.readString();
        this.path = in.readString();
        long tmpDate = in.readLong();
        this.date = tmpDate == -1 ? null : new Date(tmpDate);
    }

    public static final Creator<Photo> CREATOR = new Creator<Photo>() {
        public Photo createFromParcel(Parcel source) {
            return new Photo(source);
        }

        public Photo[] newArray(int size) {
            return new Photo[size];
        }
    };
}