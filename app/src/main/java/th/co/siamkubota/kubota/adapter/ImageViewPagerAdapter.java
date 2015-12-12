package th.co.siamkubota.kubota.adapter;

/**
 * Created by atthapok on 10/06/2558.
 */

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

import th.co.siamkubota.kubota.fragment.ImageViewPageFragment;
import th.co.siamkubota.kubota.fragment.PhotoPageFragment;
import th.co.siamkubota.kubota.model.Photo;

public class ImageViewPagerAdapter extends FragmentStatePagerAdapter {

    private ArrayList<ImageViewPageFragment> photoPageFragments;
    private ArrayList<Photo> datalist;

    private Context context;
    private Fragment listener;

    public ImageViewPagerAdapter(Context context, FragmentManager fm, ArrayList<Photo> photos, Fragment listener) {
        super(fm);

        this.context = context;
        this.datalist = photos;
        this.listener = listener;


        photoPageFragments = new ArrayList<ImageViewPageFragment>();

        for(Photo photo : datalist){
            ImageViewPageFragment fragment = ImageViewPageFragment.newInstance(photo);
            //fragment.setmListener((PhotoPageFragment.OnFragmentInteractionListener) listener);
            photoPageFragments.add(fragment);
        }

    }

    public ArrayList<Photo> getDatalist() {
        return datalist;
    }

    public void setDatalist(ArrayList<Photo> datalist) {
        this.datalist = datalist;
        notifyDataSetChanged();
    }

    // This method return the fragment for the every position in the View Pager
    @Override
    public Fragment getItem(int position) {

        return photoPageFragments.get(position);

    }

    // This method return the titles for the Tabs in the Tab Strip

    @Override
    public CharSequence getPageTitle(int position) {
        return datalist.get(position).getTitle();
    }

    // This method return the Number of tabs for the tabs Strip

    @Override
    public int getCount() {
        return photoPageFragments.size();
    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }
}