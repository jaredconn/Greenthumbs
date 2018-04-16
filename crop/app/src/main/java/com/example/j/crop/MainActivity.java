package com.example.j.crop;

import java.util.Random;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentActivity;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;

/**
 * This activity displays a grid of image squares that represent a game board.
 * The grid can be zoomed in or out and moved. Squares can be touched (regular and long press).
 *
 * <p> This is the class that brings together all the other classes into a working demo.
 * It generates a random grid of of squares. The grid represents a simple game board.
 * Each of the squares shows a while, red, or blue tile.
 * This activity connects a GameBoardView to the game board grid so it can be displayed on the screen.
 * This activity also defines actions for touch actions. When a square is touched, it is highlighted
 * to show that it is selected. When a long touch is done, the square changes to the next color in
 * the sequence (white, red, blue).
 * https://blahti.wordpress.com/2015/11/05/android-zoomable-game-board/
 */

public class MainActivity extends AppCompatActivity
        implements GameBoardTouchListener
{

    static public final int NumSquaresOnGridSide = 4;
    static public final int NumSquaresOnViewSide = 8;
    static public final int NumRedBlueTypes = 3;     // Used with simple squares demo; types: blank, red, blue

    private DrawerLayout mDrawerLayout;

    static private Random mRandomObject = new Random (System.currentTimeMillis ());

/* Property Grid */
    /**
     * This variable holds the value of the Grid property.
     */

    private int [][] pGrid;
    private int x;
    private int y;

    /**
     * Get the value of the Grid property.
     *
     * @return int [][]
     */

    public int [][] getGrid ()
    {
        //if (pGrid == null) {}
        return pGrid;
    } // end getGrid



/* Property GridView */
    /**
     * This variable holds the value of the GridView property.
     */

    private GameBoardView pGridView;

    /**
     * Get the value of the GridView property.
     *
     * @return GameBoardView
     */

    public GameBoardView getGridView ()
    {
        //if (pGridView == null) {}
        return pGridView;
    } // end getGridView

    /**
     * Set the value of the GridView property.
     *
     * @param newValue GameBoardView
     */

    public void setGridView (GameBoardView newValue)
    {
        pGridView = newValue;
    } // end setGridView
/* end Property GridView */

/**
 */
// Methods

    /**
     * onCreate
     */

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupMyGrid(NumSquaresOnGridSide);

        GameBoardView gv = (GameBoardView) findViewById(R.id.boardview);

        if (gv != null) {

            setGridView(gv);

            gv.setNumSquaresAlongCanvas(NumSquaresOnGridSide);
            gv.setNumSquaresAlongSide(NumSquaresOnViewSide);
            gv.updateGrid(getGrid());
            gv.setTouchListener(this);
        }


        //chris added code below

        mDrawerLayout = findViewById(R.id.drawer_layout);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);


        //end chris code add


//handles button clicks inside of side drawer
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        // close drawer when item is tapped
                        mDrawerLayout.closeDrawers();

                        switch (menuItem.getItemId()) {
                            case R.id.notes:

                                Intent intent = new Intent(MainActivity.this, PlantNotes.class);

                                intent.putExtra("x", x);
                                intent.putExtra("y", y);

                                startActivity(intent); break;

                           // case R.id.
                        }
                        // Add code here to update the UI based on the item selected
                        // For example, swap UI fragments here

                        return true;
                    }
                });


    }

    private int getX() {
        return this.x;
    }

    private int getY() {
        return this.y;
    }
    //side drawer buttons
    @Override public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.side_drawer, menu);
        return true;
    }


    @Override public boolean onOptionsItemSelected (MenuItem item){
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }



    /**
     *
     *
     * Get 2-d grid of integers that indicate which bitmap is displayed at that point.
     *
     * @param n int - grid size is N x N squares
     * @return int [] []
     *
     */


    int [] [] randomGridArray (int n) {
        // Set up with red, blue, and gray squares
        int [][] grid = new int [n] [n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++) {
                int index = randomInt (0, NumRedBlueTypes-1);    // index indicates which image to use
                grid [i][j] = index;
            }
        return grid;
    }

    /**
     * Return a random number in the range: minVal to maxVal.
     *
     */

    public int randomInt (int minVal, int maxVal) {
        Random r = mRandomObject;
        int range = maxVal - minVal;
        int offset = (int) Math.round (r.nextFloat () * range);
        return minVal + offset;
    }

    /**
     * Set up a 2d array of values to display in a GameBoardView.
     * The grid is filled in with random values between 0 and maxValue.
     * <p> The grid is saved with setGrid.
     * Use getGrid to access that object.
     *
     * @param n int - grid size is N x N squares
     * @param maxValue
     * @return void
     */

    public void setupMyGrid (int n)
    {
        int [][] grid = randomGridArray (n);
        setGrid (grid);
    }

    /**
     * Set the value of the Grid property.
     *
     * @param newValue int [][]
     */

    public void setGrid (int [][] newValue)
    {
        pGrid = newValue;
    } // end setGrid
/* end Property Grid */

/**
 */
// GameBoardTouchListener methods

    /**
     * This method is called when a touch Down action occurs.
     *
     * <p> Note that the location of the down location is not provided, but it is provided when the touch ends
     * and a call is made to onTouchUp.
     */

    public void onTouchDown () {
    }

    /**
     * This method is called when a touch Up action occurs.
     *
     * <p>
     * Index values are 0 based.
     * Values are between 0 and NumSquaresAlongCanvas-1.
     *
     * @param downX int - x value of the down action square
     * @param downY int - y value of the down action square
     * @param upX int - x value of the up action square
     * @param upY int - y value of the up action square
     * @return void
     */

    public void onTouchUp (int downX, int downY, int upX, int upY) {
        GameBoardView gv = getGridView ();
        if (gv == null) return;

        boolean isSelected = gv.isSelected (upX, upY);
        gv.clearSelections ();
        if (!isSelected) gv.toggleSelection (upX, upY);
        gv.invalidate ();

        setXY(upX, upY);

       // if (AppConfig.DEBUG)
           // Log.e (Constants.LOG_NAME, "onTouchUp x: " + upX + " y: " + upY + " selected: " + isSelected);

    }

    private void setXY(int upX, int upY) {
        this.x = upX;
        this.y = upY;
    }

    /**
     * This method is called when a touch Up action occurs and the time between down and up
     * exceeds the Android long press timeout value.
     *
     * <p>
     * Index values are 0 based.
     * Values are between 0 and NumSquaresAlongCanvas-1.
     *
     * @param downX int - x value of the down action square
     * @param downY int - y value of the down action square
     * @param upX int - x value of the up action square
     * @param upY int - y value of the up action square
     * @return void
     */

    public void onLongTouchUp (int downX, int downY, int upX, int upY) {
        GameBoardView gv = getGridView ();
        if (gv == null) return;

        int oldValue = gv.gridValue (upX, upY);
        int newValue = oldValue + 1;
        if (newValue >= NumRedBlueTypes) newValue = 0;
        gv.setGridValue (upX, upY, newValue);
        gv.invalidate ();

        if (AppConfig.DEBUG)
            Log.d (Constants.LOG_NAME, "onLongTouchUp x: " + upX + " y: " + upY + " old value: " + oldValue);

    }

}// end class