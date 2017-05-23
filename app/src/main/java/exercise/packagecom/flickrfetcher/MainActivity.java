package exercise.packagecom.flickrfetcher;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;
import java.util.List;

import exercise.packagecom.flickrfetcher.Model.FlickrResponse;
import exercise.packagecom.flickrfetcher.Model.Item;
import exercise.packagecom.flickrfetcher.newtwork.flickr.ApiClient;

public class MainActivity extends AppCompatActivity {

    public EditText mInputEditText;
    public Button mSearchButton;
    public ProgressBar mProgressBar;
    public RecyclerView mRecyclerView;
    public FloatingActionButton mFab;
    public TextView mNoContentMessage;
    private RecyclerViewAdapter mAdapter;
    private List<String> mDataList =  new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mInputEditText = (EditText) findViewById(R.id.edit_text_input);
        mSearchButton = (Button) findViewById(R.id.button_search);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        mNoContentMessage = (TextView) findViewById(R.id.message);
        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mRecyclerView = (RecyclerView) findViewById(R.id.id_photos_recycler_view);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this,2));
        mAdapter=  new RecyclerViewAdapter();
        mRecyclerView.setAdapter(mAdapter);

        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchContent(mInputEditText.getText().toString());
            }
        });


        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(MainActivity.this,"Fab Clicked",Toast.LENGTH_SHORT).show();



            }
        });
    }


    public void searchContent(String text)
    {
        if (text.isEmpty())
        {
            Toast.makeText(this,"Please enter something to search.",Toast.LENGTH_SHORT).show();
            return;
        }

        mRecyclerView.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);
        mNoContentMessage.setVisibility(View.GONE);
        mDataList.removeAll(mDataList);
        mAdapter.notifyDataSetChanged();
        ApiClient.getClient().getData(text,500,"json",1).enqueue(new ApiClient.Callback<FlickrResponse>() {
            @Override
            public void sucess(FlickrResponse response) {
                for (Item item:response.getItems()) {
                    mDataList.add(item.getMedia().getM().toString());
                }
                mAdapter.notifyDataSetChanged();
                if (mDataList.isEmpty())
                {
                    mRecyclerView.setVisibility(View.GONE);
                    mProgressBar.setVisibility(View.GONE);
                    mNoContentMessage.setText("No Data Found");
                    mNoContentMessage.setVisibility(View.VISIBLE);
                }
                else{
                    mRecyclerView.setVisibility(View.VISIBLE);
                    mProgressBar.setVisibility(View.GONE);
                    mNoContentMessage.setVisibility(View.GONE);
                }
            }

            @Override
            public void failure(String error) {
                mRecyclerView.setVisibility(View.GONE);
                mProgressBar.setVisibility(View.GONE);
                mNoContentMessage.setText("Something Went wrong.Please try again");
                mNoContentMessage.setVisibility(View.VISIBLE);
            }

            @Override
            public void sucessError(String reason) {
                mRecyclerView.setVisibility(View.GONE);
                mProgressBar.setVisibility(View.GONE);
                mNoContentMessage.setText("Something Went wrong.Please try again");
                mNoContentMessage.setVisibility(View.VISIBLE);
            }
        });

    }







    class RecyclerViewAdapter extends RecyclerView.Adapter<Holder>
    {



        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recycler_view_layout, parent, false);

            return new Holder(itemView);
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            holder.bindData(mDataList.get(position));
        }

        @Override
        public int getItemCount() {
            return mDataList.size();
        }
    }

    private class Holder extends RecyclerView.ViewHolder {
        ImageView mImageView;
        public Holder(View itemView) {
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.image_view);
        }

        public void bindData(final String url)
        {

            Picasso.with(MainActivity.this)
                    .load(url)
                    .networkPolicy(NetworkPolicy.OFFLINE)
                    .placeholder(R.drawable.image_placeholder)
                    .fit().centerCrop()
                    .into(mImageView, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {
                            //Try again online if cache failed
                            Picasso.with(MainActivity.this)
                                    .load(url)
                                    .placeholder(R.drawable.image_placeholder)
                                    .fit().centerCrop()
                                    .into(mImageView, new Callback() {
                                        @Override
                                        public void onSuccess() {

                                        }

                                        @Override
                                        public void onError() {

                                        }
                                    });
                        }
                    });

        }
    }
}
