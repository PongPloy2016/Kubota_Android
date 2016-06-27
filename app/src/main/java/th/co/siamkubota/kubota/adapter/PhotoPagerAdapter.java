package th.co.siamkubota.kubota.adapter;

/**
 * Created by atthapok on 10/06/2558.
 */

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

import th.co.siamkubota.kubota.fragment.PhotoPageFragment;
import th.co.siamkubota.kubota.fragment.Step1CustomerDetailFragment;
import th.co.siamkubota.kubota.fragment.Step2PhotoFragment;
import th.co.siamkubota.kubota.fragment.Step3SignFragment;
import th.co.siamkubota.kubota.fragment.Step4ConfirmFragment;
import th.co.siamkubota.kubota.model.Photo;

public class PhotoPagerAdapter extends FragmentStatePagerAdapter {

    private ArrayList<PhotoPageFragment> photoPageFragments;
    private ArrayList<Photo> datalist;

    private Context context;
    private Fragment listener;
    private boolean editabled;

    public PhotoPagerAdapter(Context context, FragmentManager fm, ArrayList<Photo> photos, Fragment listener, boolean editabled) {
        super(fm);

        this.context = context;
        this.datalist = photos;
        this.listener = listener;
        this.editabled = editabled;


        photoPageFragments = new ArrayList<PhotoPageFragment>();

        for(Photo photo : datalist){
            PhotoPageFragment fragment = PhotoPageFragment.newInstance(photo, this.editabled);
            fragment.setmListener((PhotoPageFragment.OnFragmentInteractionListener) listener);
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