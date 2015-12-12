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
    private String description;
    private int id;
    private boolean complete;

    public Photo(int id,String title) {
        this.id = id;
        this.title = title;
        this.date = new Date();
    }

    public Photo(int id, String title, String description) {
        this.id = id;
        this.title = title;
        this.date = new Date();
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }


/////////////////////////////////////////////////////////////////////////////// implement parcelable


   /* @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.title);
        dest.writeString(this.path);
        dest.writeString(this.description);
        dest.writeLong(date != null ? date.getTime() : -1);
    }

    protected Photo(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.path = in.readString();
        this.description = in.readString();
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
    };*/


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.path);
        dest.writeLong(date != null ? date.getTime() : -1);
        dest.writeString(this.description);
        dest.writeInt(this.id);
        dest.writeByte(complete ? (byte) 1 : (byte) 0);
    }

    protected Photo(Parcel in) {
        this.title = in.readString();
        this.path = in.readString();
        long tmpDate = in.readLong();
        this.date = tmpDate == -1 ? null : new Date(tmpDate);
        this.description = in.readString();
        this.id = in.readInt();
        this.complete = in.readByte() != 0;
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
