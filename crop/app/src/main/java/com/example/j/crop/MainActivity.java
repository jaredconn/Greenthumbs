package com.example.j.crop;

import java.util.Random;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;

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
    private int selected_x = 0;
    private int selected_y = 0;
    private int first_x = 0;
    private int first_y = 0;
    private int firstValue = 0;

    FloatingActionButton fab_swap;
    FloatingActionButton fab_clear;





    private boolean edit_flag = false;

    static public final int NumSquaresOnGridSide = 10;
    static public final int NumSquaresOnViewSide = 8;
    static public final int NumRedBlueTypes = 3;     // Used with simple squares demo; types: blank, red, blue

    private DrawerLayout mDrawerLayout;

    static private Random mRandomObject = new Random (System.currentTimeMillis ());

/* Property Grid */
    /**
     * This variable holds the value of the Grid property.
     */

    private int [][] pGrid;

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

        //chris work
        int[] dimensions = getIntent().getIntArrayExtra("DIMENSIONS");

        //end chris work


        //setupMyGrid(NumSquaresOnGridSide);
        setupMyGrid(dimensions[0]);

        GameBoardView gv = (GameBoardView) findViewById(R.id.boardview);

        if (gv != null) {

            setGridView(gv);

            //gv.setNumSquaresAlongCanvas(NumSquaresOnGridSide);
            //gv.setNumSquaresAlongSide(NumSquaresOnViewSide);

            gv.setNumSquaresAlongCanvas(dimensions[0]);
            gv.setNumSquaresAlongSide(dimensions[1]);
            gv.updateGrid(getGrid());
            gv.setTouchListener(this);
        }


        //chris added code below

        mDrawerLayout = findViewById(R.id.drawer_layout);

        fab_swap = findViewById(R.id.fab_swap);
        fab_clear = findViewById(R.id.fab_clear);

        fab_swap.setVisibility(View.GONE);
        fab_clear.setVisibility(View.GONE);

        fab_swap.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                GameBoardView gv = getGridView();
                if (gv == null) {
                    return;
                }
        /*
        int oldValue = gv.gridValue (upX, upY);
        int newValue = oldValue + 1;
        if (newValue >= NumRedBlueTypes) newValue = 0;
        gv.setGridValue (upX, upY, newValue);
        gv.invalidate ();
        */

        /*
                int oldValue = gv.gridValue (upX, upY);
                int newValue = gv.gridValue(selected_x, selected_y);
                gv.setGridValue(upX, upY, newValue);
                gv.setGridValue(selected_x, selected_y, oldValue);
                gv.invalidate();
                gv.clearSelections();
                */
                gv.clearSelections();
                if (firstValue == 0)
                {
                    firstValue = gv.gridValue(selected_x, selected_y);
                    first_x = selected_x;
                    first_y = selected_y;

                }
                else
                {
                    int secondValue = gv.gridValue(selected_x, selected_y);
                    gv.setGridValue(first_x, first_y, secondValue);
                    gv.setGridValue(selected_x, selected_y, firstValue);
                    gv.invalidate();
                    selected_x = 0;
                    selected_y = 0;
                    first_x = 0;
                    first_y = 0;
                    firstValue = 0;
                }




            }
        });

        fab_clear.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            }
        });



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
                                startActivity(new Intent(MainActivity.this, PlantNotes.class)); break;

                            case R.id.nav_edit_plot:
                                enterEditMode();
                                break;

                            case R.id.nav_water_mode:
                                break;

                           // case R.id.
                        }
                        // Add code here to update the UI based on the item selected
                        // For example, swap UI fragments here

                        return true;
                    }
                });


    }

    void enterEditMode()
    {
        GameBoardView gv = getGridView ();
        if (gv == null) return;

        if (!edit_flag) {
            edit_flag = true;
            fab_swap.setVisibility(View.VISIBLE);
            fab_clear.setVisibility(View.VISIBLE);
            selected_x = 0;
            selected_y = 0;
            first_x = 0;
            first_y = 0;
            firstValue = 0;
        }
        else
        {
            edit_flag = false;
            gv.clearSelections();
            fab_swap.setVisibility(View.GONE);
            fab_clear.setVisibility(View.GONE);
        }
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
        if (!edit_flag)
        {
            return;
        }
        GameBoardView gv = getGridView ();
        if (gv == null) return;

        boolean isSelected = gv.isSelected (upX, upY);
        gv.clearSelections ();
        if (!isSelected) gv.toggleSelection (upX, upY);
        selected_x = upX;
        selected_y = upY;
        gv.invalidate();

        if (AppConfig.DEBUG)
            Log.d (Constants.LOG_NAME, "onTouchUp x: " + upX + " y: " + upY + " selected: " + isSelected);

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


    }

}// end class