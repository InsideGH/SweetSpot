package com.sweetlab.sweetspot.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sweetlab.sweetspot.R;

/**
 * Multi photo fragment with a photo pager fragment in main pane and
 * a photo carousel in the bottom pane.
 */
public class MultiPhotoFragment extends Fragment {
    /**
     * Create a multi photo fragment.
     *
     * @return A multi photo fragment.
     */
    public static MultiPhotoFragment createInstance() {
        return new MultiPhotoFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.multi_photo_view, container, false);

        FragmentManager fm = getChildFragmentManager();

        Fragment bottomFragment = fm.findFragmentById(R.id.multi_photo_bottom_pane);
        if (bottomFragment == null) {
            addBottomPane(fm);
        }

        Fragment mainFragment = fm.findFragmentById(R.id.multi_photo_main_pane);
        if (mainFragment == null) {
            addMainPane(fm);
        }
        return root;
    }

    /**
     * Toggle the carousel visibility.
     * TODO, use eps? Seems to work fine as is though.
     */
    public void toggleCarousel() {
        View bottomContainer = getActivity().findViewById(R.id.multi_photo_bottom_pane);
        int duration = getActivity().getResources().getInteger(R.integer.carousel_toggle_duration);
        if (bottomContainer.getAlpha() == 1) {
            bottomContainer.animate().alpha(0).setDuration(duration);
        } else {
            bottomContainer.animate().alpha(1).setDuration(duration);
        }
    }

    /**
     * Set the position of the carousel.
     *
     * @param position The adapter position.
     */
    public void setCarouselPosition(int position) {
        Fragment fragment = getChildFragmentManager().findFragmentByTag(FragmentTags.CAROUSEL_TAG);
        if (fragment != null) {
            CarouselFragment carouselFragment = (CarouselFragment) fragment;
            carouselFragment.setPosition(position);
        }
    }

    /**
     * Add the viewpager fragment to main pane.
     *
     * @param fm Support fragment manager.
     */
    private void addMainPane(FragmentManager fm) {
        Fragment mainFragment;
        FragmentTransaction transaction = fm.beginTransaction();
        mainFragment = ViewPagerFragment.createInstance();
        transaction.add(R.id.multi_photo_main_pane, mainFragment, FragmentTags.VIEWPAGER_TAG);
        transaction.commit();
    }

    /**
     * Add the carousel fragment to bottom pane.
     *
     * @param fm Support fragment manager.
     */
    private void addBottomPane(FragmentManager fm) {
        Fragment bottomFragment;
        FragmentTransaction transaction = fm.beginTransaction();
        bottomFragment = new CarouselFragment();
        transaction.add(R.id.multi_photo_bottom_pane, bottomFragment, FragmentTags.CAROUSEL_TAG);
        transaction.commit();
    }
}
