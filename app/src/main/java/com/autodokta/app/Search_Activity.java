package com.autodokta.app;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;

import com.algolia.instantsearch.core.helpers.Searcher;
import com.algolia.instantsearch.ui.helpers.InstantSearch;
import com.algolia.search.saas.Client;
//import com.algolia.search.saas.Query;
import com.autodokta.app.Adapters.CarParts;
import com.autodokta.app.Adapters.PartsAdapter;
import com.autodokta.app.SearchItems.ResultsListView;
import com.autodokta.app.helpers.Space;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Search_Activity extends AppCompatActivity{ //implements AbsListView.OnScrollListener  {

    //the seaech list view
    private ResultsListView moviesListView;
    // Constants
    private static final int LOAD_MORE_THRESHOLD = 5;
    private static final int HITS_PER_PAGE = 20;

    private Searcher searcher;
    private InstantSearch helper;
    private String imageurl, name, description, price, sellersNumber,product_rating;

    private ArrayList resultParts = new ArrayList<CarParts>();
    private RecyclerView PostRecyclerView;
    private RecyclerView.Adapter mPostAdapter;
    private RecyclerView.LayoutManager mPostLayoutManager;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu,menu);
        MenuItem searchMenuItem = menu.findItem(R.id.actionn_search ); // get my MenuItem with placeholder submenu

//        automatically open search View when activity starts
        searchMenuItem.expandActionView();
//        ends here
        SearchView searchView = (SearchView)searchMenuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                query = query.toLowerCase();
//                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("carparts").child("Offers");
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("allParts");
                Query query1 = reference.orderByKey().startAt(query);//.endAt(query+"\uf8ff");
                query1.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            for(DataSnapshot child : dataSnapshot.getChildren()){
                                FetchParts(child.getKey());
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });


//        try{
//            helper.registerSearchView(this, menu, R.id.actionn_search);
//        }catch (NullPointerException e){
//
//        }
        return super.onCreateOptionsMenu(menu);
    }

    private void FetchParts(final String key) {
//        DatabaseReference postData = FirebaseDatabase.getInstance().getReference().child("carparts").child("Offers").child(key);
        DatabaseReference postData = FirebaseDatabase.getInstance().getReference().child("allParts").child(key);
        postData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot child : dataSnapshot.getChildren()){
                        if(child.getKey().equals("image")){
                            imageurl = child.getValue().toString();
                        }

                        if(child.getKey().equals("name")){
                            name = child.getValue().toString();
                        }

                        if(child.getKey().equals("description")){
                            description = child.getValue().toString();
                        }

                        if(child.getKey().equals("price")){
                            price = child.getValue().toString();
                        }

                        if(child.getKey().equals("buyersNumber")){
                            sellersNumber = child.getValue().toString();
                        }

                        if(child.getKey().equals("rating")){
                            product_rating = child.getValue().toString();
                        }
                        else{
//                            Toast.makeText(getActivity(),"Couldn't fetch posts",Toast.LENGTH_LONG).show();
                        }
                    }

                    String partid = key;
                    boolean isNew = false;
                    CarParts obj = new CarParts(partid,imageurl,name,description,price, isNew, sellersNumber,"",product_rating);
                    resultParts.add(obj);
                    PostRecyclerView.setAdapter(mPostAdapter);
                    mPostAdapter.notifyDataSetChanged();
//                    loading.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Toast.makeText(getActivity(),"Cancelled",Toast.LENGTH_LONG).show();

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        getSupportActionBar().setTitle("Search");

        PostRecyclerView = (RecyclerView) findViewById(R.id.recyclerViewProducts);
        PostRecyclerView.setHasFixedSize(true);

//        mPostLayoutManager = new GridLayoutManager(getActivity(),2, LinearLayoutManager.VERTICAL,false);
        mPostLayoutManager = new LinearLayoutManager(Search_Activity.this);
        PostRecyclerView.setLayoutManager(mPostLayoutManager);


        mPostAdapter = new PartsAdapter(getParts(),Search_Activity.this);

        PostRecyclerView.addItemDecoration(new Space(2,20,true,0));

        PostRecyclerView.setAdapter(mPostAdapter);


        //search items starts
        // Bind UI components.
//        (moviesListView = (ResultsListView) findViewById(R.id.listview_movies)).setOnScrollListener(this);
        //        // Init Algolia.
//        try {
//            Client client = new Client("OYZ8A2I21U", "a48c539217373329bc6f6e774d1058f4");
//            searcher = Searcher.create(client.getIndex("prod_Autodokta"));
//            helper = new InstantSearch(moviesListView, searcher);
//        }catch (IllegalStateException e){
//
//        }
//
        // Pre-build query.
//        try {
//            searcher.setQuery(new Query().setAttributesToRetrieve("*")//"name", "price", "image", "description")
//                    .setAttributesToHighlight("name")
//                    .setHitsPerPage(HITS_PER_PAGE));
//        }catch (NullPointerException e){
//
//        }
//        search items ends
    }

//    @Override
//    public void onScrollStateChanged(AbsListView view, int scrollState) {
//        // Nothing to do.
//    }
//
//    @Override
//    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//        // Abort if list is empty or the end has already been reached.
//        if (totalItemCount == 0 || !searcher.hasMoreHits()) {
//            return;
//        }
//
//        // Load more if we are sufficiently close to the end of the list.
//        int firstInvisibleItem = firstVisibleItem + visibleItemCount;
//        if (firstInvisibleItem + LOAD_MORE_THRESHOLD >= totalItemCount) {
//            searcher.loadMore();
//        }
//    }

    public ArrayList<CarParts> getParts(){
        return  resultParts;
    }

}
