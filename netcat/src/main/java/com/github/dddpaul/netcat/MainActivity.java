package com.github.dddpaul.netcat;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity implements ActionBar.TabListener, OnFragmentInteractionListener
{
    private final String CLASS_NAME = ( (Object) this ).getClass().getSimpleName();

    private SectionsPagerAdapter adapter;
    private Menu menu;
    private ViewPager pager;
    private TextView statusView;

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        // Set up the action bar
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode( ActionBar.NAVIGATION_MODE_TABS );

        // Set up the ViewPager with the sections adapter
        adapter = new SectionsPagerAdapter( this, getSupportFragmentManager() );
        pager = (ViewPager) findViewById( R.id.pager );
        pager.setAdapter( adapter );
        pager.setOnPageChangeListener( new ViewPager.SimpleOnPageChangeListener()
        {
            @Override
            public void onPageSelected( int position )
            {
                actionBar.setSelectedNavigationItem( position );
            }
        } );

        // For each of the sections in the app, add a tab to the action bar
        for( int i = 0; i < adapter.getCount(); i++ ) {
            actionBar.addTab(
                    actionBar.newTab()
                            .setText( adapter.getPageTitle( i ) )
                            .setTabListener( this )
            );
        }
    }

    @Override
    public boolean onCreateOptionsMenu( Menu menu )
    {
        getMenuInflater().inflate( R.menu.actions, menu );
        statusView = (TextView) menu.findItem( R.id.action_status ).getActionView();
        this.menu = menu;
        return super.onCreateOptionsMenu( menu );
    }

    @Override
    public boolean onOptionsItemSelected( MenuItem item )
    {
        switch( item.getItemId() ) {
            case R.id.action_settings:
                return true;
            case R.id.action_cancel:
                int position = getResources().getInteger( R.integer.result_fragment_position );
                ResultFragment result = (ResultFragment) adapter.getRegisteredFragment( position );
                result.disconnect( statusView );
        }
        return super.onOptionsItemSelected( item );
    }

    @Override
    public void onTabSelected( ActionBar.Tab tab, FragmentTransaction fragmentTransaction )
    {
        pager.setCurrentItem( tab.getPosition() );
    }

    @Override
    public void onTabUnselected( ActionBar.Tab tab, FragmentTransaction fragmentTransaction )
    {
    }

    @Override
    public void onTabReselected( ActionBar.Tab tab, FragmentTransaction fragmentTransaction )
    {
    }

    @Override
    public void onFragmentInteraction( int position )
    {
        setDisconnectButtonVisibility( true );
        pager.setCurrentItem( position, false );
    }

    @Override
    public void onFragmentInteraction( int position, NetCat.Op op, String data )
    {
        if( position == getResources().getInteger( R.integer.result_fragment_position ) ) {
            ResultFragment result = (ResultFragment) adapter.getRegisteredFragment( position );
            switch( op ) {
                case CONNECT:
                    result.connect( data, statusView );
                    break;
                case LISTEN:
                    result.listen( data, statusView );
                    break;
            }
        }
    }

    public void setDisconnectButtonVisibility( boolean visible )
    {
        menu.findItem( R.id.action_cancel ).setVisible( visible );
        onPrepareOptionsMenu( menu );
    }
}
