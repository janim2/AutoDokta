package com.autodokta.app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;

import com.algolia.instantsearch.core.helpers.Searcher;
import com.algolia.instantsearch.ui.helpers.InstantSearch;
import com.algolia.search.saas.Client;
import com.algolia.search.saas.Query;
import com.autodokta.app.SearchItems.ResultsListView;

public class Search_Activity extends AppCompatActivity implements AbsListView.OnScrollListener  {

    //the seaech list view
    private ResultsListView moviesListView;
    // Constants
    private static final int LOAD_MORE_THRESHOLD = 5;
    private static final int HITS_PER_PAGE = 20;

    private Searcher searcher;
    private InstantSearch helper;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu,menu);
        MenuItem searchMenuItem = menu.findItem( R.id.actionn_search ); // get my MenuItem with placeholder submenu
        searchMenuItem.expandActionView();

        try{
            helper.registerSearchView(this, menu, R.id.actionn_search);
        }catch (NullPointerException e){

        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        getSupportActionBar().setTitle("Search");

        //search items starts
        // Bind UI components.
        (moviesListView = (ResultsListView) findViewById(R.id.listview_movies)).setOnScrollListener(this);
        //        // Init Algolia.
        try {
            Client client = new Client("OYZ8A2I21U", "a48c539217373329bc6f6e774d1058f4");
            searcher = Searcher.create(client.getIndex("prod_Autodokta"));
            helper = new InstantSearch(moviesListView, searcher);
        }catch (IllegalStateException e){

        }
//
        // Pre-build query.
        try {
            searcher.setQuery(new Query().setAttributesToRetrieve("*")//"name", "price", "image", "description")
                    .setAttributesToHighlight("name")
                    .setHitsPerPage(HITS_PER_PAGE));
        }catch (NullPointerException e){

        }
//        search items ends
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        // Nothing to do.
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        // Abort if list is empty or the end has already been reached.
        if (totalItemCount == 0 || !searcher.hasMoreHits()) {
            return;
        }

        // Load more if we are sufficiently close to the end of the list.
        int firstInvisibleItem = firstVisibleItem + visibleItemCount;
        if (firstInvisibleItem + LOAD_MORE_THRESHOLD >= totalItemCount) {
            searcher.loadMore();
        }
    }
}
