package com.meetme.meetme;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Profile;
import com.meetme.meetme.DataManagers.DataManager;
import com.parse.Parse;
import com.parse.ParseObject;

public class LoginActivity extends AppCompatActivity implements LoginActivityFragment.LoginListener {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    boolean bFirstTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        bFirstTime = true;


        Parse.enableLocalDatastore(this);

        Parse.initialize(this);
  //      toolbar = (Toolbar) findViewById(R.id.app_bar);
  //      setSupportActionBar(toolbar);

      /*  ParseObject testObject = new ParseObject("TestObject");
        testObject.put("foo", "zzzzz");
        testObject.saveInBackground();
*/
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.menu){

        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if(bFirstTime && Profile.getCurrentProfile() != null) {
            startActivity(new Intent(this, mainScreen_with_drawer.class));
            bFirstTime = false;
        } else {
            addButton();
        }
    }

    private void addButton() {

    }

    @Override
    public void SuccessfulLogin(Profile profile) {

        if (DataManager.getInstance(this).GetMyInfo() == null)
            DataManager.getInstance(this).SetMyInfo(profile.getName());

        startActivity(new Intent(this, mainScreen_with_drawer.class));
    }

    /********************************************************************/
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "SECTION 1";
                case 1:
                    return "SECTION 2";
                case 2:
                    return "SECTION 3";
            }
            return null;
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.tabbed_layout, container, false);
            ImageView imageView = (ImageView) rootView.findViewById(R.id.imageView);
            TextView textView = (TextView) rootView.findViewById(R.id.login_text);
           // textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            switch (getArguments().getInt(ARG_SECTION_NUMBER)){
                case 1:
                    imageView.setImageDrawable(getResources().getDrawable(R.drawable.login1));
                    textView.setText(R.string.login1);
                    break;
                case 2:
                    imageView.setImageDrawable(getResources().getDrawable(R.drawable.login2));
                    textView.setText(R.string.login2);
                    break;
                case 3:
                    imageView.setImageDrawable(getResources().getDrawable(R.drawable.login3));
                    textView.setText(R.string.login3);
                    break;
            }

            return rootView;
        }
    }
}
