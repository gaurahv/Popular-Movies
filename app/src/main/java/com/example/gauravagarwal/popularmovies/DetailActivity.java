package com.example.gauravagarwal.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new DetailFragment())
                    .commit();
        }

    }


    public static class DetailFragment extends Fragment {

        TextView title;
        TextView description;
        TextView releaseDate;
        TextView voteAverage;
        TextView voteCount;
        ImageView posterImage;

        public DetailFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
            title = (TextView) rootView.findViewById(R.id.title_text_view);
            description = (TextView) rootView.findViewById(R.id.description_text_view);
            releaseDate = (TextView) rootView.findViewById(R.id.release_date_text_view);
            voteAverage = (TextView) rootView.findViewById(R.id.vote_average_text_view);
            voteCount = (TextView) rootView.findViewById(R.id.vote_count_text_view);
            posterImage = (ImageView) rootView.findViewById(R.id.poster_image);

            // The detail Activity called via intent.  Inspect the intent for  data.
            Intent intent = getActivity().getIntent();
            if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
                Movie movie  = intent.getParcelableExtra(Intent.EXTRA_TEXT);
                title.setText(movie.title);
                Picasso.with(getContext()).load(movie.posterUrl).into(posterImage);
                releaseDate.setText(movie.releaseDate);
                voteAverage.setText(movie.user_rating + "/10");
                voteCount.setText(movie.num_votes + " votes");
                description.setText(movie.overview);
            }
            return rootView;
        }
    }
}
