package com.example.sportnews.ui.main;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sportnews.PostLoader;
import com.example.sportnews.R;
import com.example.sportnews.SectionLoader;
import com.example.sportnews.logic.Logic;
import com.example.sportnews.pojo.Post;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    private static final int POSTS_LOADER_ID = 1;
    private static final int Section_LOADER_ID = 2;

    private static URL url;
    @BindView(R.id.searchTxt)
    EditText searchTxt;
    @BindView(R.id.CountryRecycle)
    RecyclerView CountryRecycle;
    @BindView(R.id.mainRecycle)
    RecyclerView mainRecycle;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.empty_view)
    TextView emptyView;
    private PostLoader postLoader;
    private PostAdapter postAdapter;
    private SectionAdapter sectionAdapter;

    int limit = 100;
    String searchKey = "";
    String orderBy = "newest";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        emptyView.setText(R.string.no_posts_found);

        if (Logic.isNetworkAvailable(this.getApplication())) {
            initCouponRecycleView();
            initSectionRecycleView();
            url = getUrl();
            LoaderManager.getInstance(this).initLoader(POSTS_LOADER_ID, null, postsLoader());
            LoaderManager.getInstance(this).initLoader(Section_LOADER_ID, null, sectionLoader());
            searchTxt.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    final int DRAWABLE_RIGHT = 2;
                    searchTxt.performClick();
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        if (event.getRawX() >= (searchTxt.getRight() - searchTxt.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                            search(searchTxt.getText().toString());
                            return true;
                        }
                    }
                    return false;
                }
            });
            sectionAdapter.setOnAdapterClickListener(new SectionAdapter.OnAdapterClickListener() {
                @Override
                public void onSectionClick(String sectionName) {
                    search(sectionName);
                }
            });

            postAdapter.setOnAdapterClickListener(new PostAdapter.OnAdapterClickListener() {
                @Override
                public void onFavClick(Post post) { }

                @Override
                public void onShareClick(Post post) {
                    String shared = "NEWS App \n"
                            + "This " + post.getType() + " "
                            + "is about " + post.getSectionName()
                            + " with title " + post.getTitle() + "\n"
                            + post.getUrl() + "\n"
                            + "Published At " + post.getPublishedAt().split("-")[0]
                            + " in " + post.getPublishedAt().split("-")[1] + "\n";
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, shared);
                    sendIntent.setType("text/plain");
                    startActivity(sendIntent);
                }

                @Override
                public void onLinkClick(String url) {
                    if(url!=null) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(url));
                        startActivity(intent);
                    }
                }

                @Override
                public void onAuthorNameClick(String url) {
                    if(url!=null) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(url));
                        startActivity(intent);
                    }
                }
            });
        } else {
            progressBar.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
            emptyView.setText(R.string.no_internet_connection);
            //generate the snackbar
            progressBar.setVisibility(View.GONE);
            Snackbar sb = Snackbar.make(findViewById(android.R.id.content), R.string.no_internet_connection, Snackbar.LENGTH_INDEFINITE);
            sb.setActionTextColor(getResources().getColor(R.color.colorWhite));
            View sbView = sb.getView();
            sbView.setBackgroundColor(getResources().getColor(R.color.color3));
            sb.setAction(R.string.exit, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            sb.show();
        }
    }

    private void search(String s) {
        searchKey = s;
        url = getUrl();
        progressBar.setVisibility(View.VISIBLE);
        LoaderManager.getInstance(MainActivity.this)
                .restartLoader(POSTS_LOADER_ID, null, postsLoader());
    }

    @OnClick(R.id.orderBy)
    public void onViewClicked() {
        createDialog().show();
    }

    private AlertDialog createDialog() {

        final View view = getLayoutInflater().inflate(R.layout.filterdialog, null);
        final EditText limitDialog;

        limitDialog = view.findViewById(R.id.limit);
        limitDialog.setText(limit + "");
        final Spinner order = view.findViewById(R.id.order);

        return new AlertDialog.Builder(MainActivity.this)
                .setView(view).setPositiveButton(R.string.filter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        limit = Integer.parseInt(limitDialog.getText().toString());
                        searchKey = "";
                        orderBy = order.getSelectedItem().toString();
                        url = getUrl();

                        progressBar.setVisibility(View.VISIBLE);
                        LoaderManager.getInstance(MainActivity.this)
                                .restartLoader(POSTS_LOADER_ID, null, postsLoader());
                    }
                }).setNegativeButton(R.string.back, null).create();

    }


    private LoaderManager.LoaderCallbacks<List<String>> sectionLoader() {
        return new LoaderManager.LoaderCallbacks<List<String>>() {
            @NonNull
            @Override
            public Loader<List<String>> onCreateLoader(int id, @Nullable Bundle args) {
                return new SectionLoader(MainActivity.this, getSectionUrl());
            }

            @Override
            public void onLoadFinished(@NonNull Loader<List<String>> loader, List<String> data) {
                sectionAdapter.setSectionList(data);
            }

            @Override
            public void onLoaderReset(@NonNull Loader<List<String>> loader) {

            }
        };
    }

    private LoaderManager.LoaderCallbacks<List<Post>> postsLoader() {
        return new LoaderManager.LoaderCallbacks<List<Post>>() {
            @NonNull
            @Override
            public Loader<List<Post>> onCreateLoader(int id, @Nullable Bundle args) {
                postLoader = new PostLoader(MainActivity.this, url);
                return postLoader;
            }

            @Override
            public void onLoadFinished(@NonNull Loader<List<Post>> loader, List<Post> data) {
                postAdapter.setPostList(data);
                progressBar.setVisibility(View.GONE);
                if (data == null || data.isEmpty()) {
                    emptyView.setText(R.string.no_posts_found);
                    if(!Logic.isNetworkAvailable(MainActivity.this.getApplication())){
                        emptyView.setText(R.string.no_internet_connection);
                    }
                    emptyView.setVisibility(View.VISIBLE);
                } else emptyView.setVisibility(View.GONE);
            }

            @Override
            public void onLoaderReset(@NonNull Loader<List<Post>> loader) {

            }
        };
    }


    private URL getSectionUrl() {
        String urlSection = "https://content.guardianapis.com/sections?&api-key=02bc0235-459a-465e-bf49-e40dcbc15b4c";
        URL url = null;
        try {
            url = new URL(urlSection);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    private URL getUrl() {
        String urlNews = "https://content.guardianapis.com/search?";
        Uri builtUri = Uri.parse(urlNews)
                .buildUpon()
                .appendQueryParameter("page-size", String.valueOf(limit))
                .appendQueryParameter("q", searchKey)
                .appendQueryParameter("show-tags", "contributor")
                .appendQueryParameter("order-by", orderBy)
                .appendQueryParameter("api-key", "02bc0235-459a-465e-bf49-e40dcbc15b4c")
                .build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }


    private void initCouponRecycleView() {
        postAdapter = new PostAdapter(this);
        mainRecycle.setLayoutManager(new LinearLayoutManager(this));
        mainRecycle.setHasFixedSize(true);
        mainRecycle.setAdapter(postAdapter);
    }

    private void initSectionRecycleView() {
        sectionAdapter = new SectionAdapter(this);
        CountryRecycle.setLayoutManager(new LinearLayoutManager(this));
        CountryRecycle.setHasFixedSize(true);
        CountryRecycle.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true));
        CountryRecycle.setAdapter(sectionAdapter);
    }


}