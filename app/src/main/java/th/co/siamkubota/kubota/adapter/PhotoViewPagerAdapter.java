package th.co.siamkubota.kubota.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import java.util.ArrayList;

import th.co.siamkubota.kubota.fragment.PhotoPageFragment;
import th.co.siamkubota.kubota.model.Photo;

/**
 * Created by sipangka on 10/12/2558.
 */
public class PhotoViewPagerAdapter extends FragmentPagerAdapter {

    //private final Resources resources;

    private ArrayList<PhotoPageFragment> photoPageFragments;
    private ArrayList<Photo> datalist;

    private Context context;
    private Fragment listener;

    SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();

    /**
     * Create pager adapter
     *
     * @param resources
     * @param fm
     */
    public PhotoViewPagerAdapter(final Resources resources, FragmentManager fm) {
        super(fm);
        //this.resources = resources;
    }

    public PhotoViewPagerAdapter(Context context, FragmentManager fm, ArrayList<Photo> photos, Fragment listener) {
        super(fm);

        this.context = context;
        this.datalist = photos;
        this.listener = listener;


     /*   photoPageFragments = new ArrayList<PhotoPageFragment>();

        for(Photo photo : datalist){
            PhotoPageFragment fragment = PhotoPageFragment.newInstance(photo);
            fragment.setmListener((PhotoPageFragment.OnFragmentInteractionListener) listener);
            photoPageFragments.add(fragment);
        }
*/
    }

    @Override
    public Fragment getItem(int position) {
        final Fragment result;
       /* switch (position) {
            case 0:
                // First Fragment of First Tab
                result = new A1Fragment();
                break;
            case 1:
                // First Fragment of Second Tab
                result = new B1Fragment();
                break;
            case 2:
                // First Fragment of Third Tab
                result = new C1Fragment();
                break;
            default:
                result = null;
                break;
        }*/

        PhotoPageFragment fragment = PhotoPageFragment.newInstance(datalist.get(position), true);
        fragment.setmListener((PhotoPageFragment.OnFragmentInteractionListener) listener);
        result = (Fragment)fragment;


        return result;
    }

    @Override
    public int getCount() {
        return datalist.size();
    }

   /* @Override
    public CharSequence getPageTitle(final int position) {
        switch (position) {
            case 0:
                return resources.getString(R.string.page_1);
            case 1:
                return resources.getString(R.string.page_2);
            case 2:
                return resources.getString(R.string.page_3);
            default:
                return null;
        }
    }*/

    /**
     * On each Fragment instantiation we are saving the reference of that Fragment in a Map
     * It will help us to retrieve the Fragment by position
     *
     * @param container
     * @param position
     * @return
     */
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        registeredFragments.put(position, fragment);
        return fragment;
    }

    /**
     * Remove the saved reference from our Map on the Fragment destroy
     *
     * @param container
     * @param position
     * @param object
     */
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        registeredFragments.remove(position);
        super.destroyItem(container, position, object);
    }


    /**
     * Get the Fragment by position
     *
     * @param position tab position of the fragment
     * @return
     */
    public Fragment getRegisteredFragment(int position) {
        return registeredFragments.get(position);
    }
}