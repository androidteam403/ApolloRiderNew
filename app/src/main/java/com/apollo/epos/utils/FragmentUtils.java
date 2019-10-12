package com.apollo.epos.utils;

import androidx.annotation.IntDef;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.apollo.epos.R;

public class FragmentUtils {

    public static final int TRANSITION_POP = 0;
    public static final int TRANSITION_FADE_IN_OUT = 1;
    public static final int TRANSITION_SLIDE_LEFT_RIGHT = 2;
    public static final int TRANSITION_SLIDE_LEFT_RIGHT_WITHOUT_EXIT = 3;
    public static final int TRANSITION_NONE = 4;
    public static final int TRANSITION_FROM_LEFT_TO_RIGHT = 5;
    public static final int TRANSITION_FROM_RIGHT_TO_LEFT = 6;

    @IntDef({TRANSITION_POP, TRANSITION_FADE_IN_OUT, TRANSITION_SLIDE_LEFT_RIGHT, TRANSITION_SLIDE_LEFT_RIGHT_WITHOUT_EXIT, TRANSITION_NONE, TRANSITION_FROM_LEFT_TO_RIGHT, TRANSITION_FROM_RIGHT_TO_LEFT})
    @interface FragmentAnimation {
    }

    private FragmentUtils() {
        // Private constructor to hide the implicit one
    }

    public static void replaceFragment(AppCompatActivity activity, Fragment fragment, int id, boolean addToBackStack, @FragmentAnimation int animationType) {

        if (null == activity)
            return;

        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        switch (animationType) {
            case TRANSITION_POP:
                transaction.setCustomAnimations(R.anim.anim_enter, R.anim.anim_exit, R.anim.anim_pop_enter, R.anim.anim_pop_exit);
                break;
            case TRANSITION_FADE_IN_OUT:
                transaction.setCustomAnimations(R.anim.anim_frag_fade_in, R.anim.anim_frag_fade_out);
                break;
            case TRANSITION_SLIDE_LEFT_RIGHT:
                transaction.setCustomAnimations(R.anim.slide_in_from_rigth, R.anim.slide_out_to_left, R.anim.slide_in_from_left, R.anim.slide_out_to_right);
                break;
            case TRANSITION_SLIDE_LEFT_RIGHT_WITHOUT_EXIT:
                transaction.setCustomAnimations(R.anim.slide_in_from_rigth, 0);
                break;
            case TRANSITION_FROM_LEFT_TO_RIGHT:
                transaction.setCustomAnimations(R.anim.slide_from_right, R.anim.slide_to_left);
                break;
            case TRANSITION_FROM_RIGHT_TO_LEFT:
                transaction.setCustomAnimations(R.anim.slide_from_left, R.anim.slide_to_right);
                break;
            case TRANSITION_NONE:
            default:
                transaction.setCustomAnimations(0, 0);
                break;
        }

        if (addToBackStack)
            transaction.addToBackStack(fragment.getClass().getCanonicalName());

        transaction.replace(id, fragment, fragment.getClass().getCanonicalName());
        transaction.commit();
    }
}
