package com.example.firebasedatabaseexample;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;


public class MainActivity extends AppCompatActivity {
    private FloatingActionButton fab;
    ScaleAnimation shrinkAnim;
    private RecyclerView mRecyclerView;
    private StaggeredGridLayoutManager mLayoutManager;
    private FirebaseRecyclerAdapter<Movie, MovieViewHolder> adapter;
    private TextView tvNoMovies;

    //Getting reference to Firebase Database
    private DatabaseReference MoviesRef;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    private static final String userId = "53";
    public static  String idMovies = "idmovies";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initializing our Recyclerview
        mRecyclerView = (RecyclerView)findViewById(R.id.my_recycler_view);
        tvNoMovies = (TextView)findViewById(R.id.tv_no_movies);

        //Scale animation to shrink floating actionbar
        shrinkAnim = new ScaleAnimation(
                1.15f, 0f, 1.15f, 0f,
                Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);

        if (mRecyclerView != null) {
            //To enable optimization of recyclerview
            mRecyclerView.setHasFixedSize(true);
        }

        //Using staggered grid pattern in recyclerview
        mLayoutManager = new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL);

        mRecyclerView.setLayoutManager(mLayoutManager);

        MoviesRef = FirebaseDatabase.getInstance().getReference()
                .child("users").child(userId).child("movies");

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        FirebaseRecyclerOptions<Movie> options = new FirebaseRecyclerOptions.Builder<Movie>()
                        .setQuery(MoviesRef, Movie.class)
                        .build();

        FirebaseRecyclerAdapter<Movie, MovieViewHolder> adapter =
                new FirebaseRecyclerAdapter<Movie, MovieViewHolder>(options) {
                    @NonNull
                    @Override
                    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        return null;
                    }

                    @Override
                    protected void onBindViewHolder(@NonNull MovieViewHolder holder, int position, @NonNull final Movie model) {
                        if (tvNoMovies.getVisibility() == View.VISIBLE) {
                            tvNoMovies.setVisibility(View.GONE);
                        }

                        holder.tvMovieName.setText(model.getMovieName());
                        holder.ratingBar.setVisibility(View.GONE);

                        Picasso.get().load(model.getMoviePoster()).centerCrop().into(holder.ivMoviePoster);

                        holder.Eliminar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                AlertDialog.Builder confirmacion= new AlertDialog.Builder(MainActivity.this);
                                confirmacion.setMessage("¿Seguro que quiere eliminar esta categoría?")
                                        .setCancelable(false)
                                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                databaseReference.child("users")
                                                        .child(userId)
                                                        .child("movies")
                                                        .child(model.getId()).removeValue();
                                                Toast toast1 = Toast.makeText(getApplicationContext(),
                                                        "Categoría Borrada", Toast.LENGTH_SHORT);
                                                toast1.show();
                                            }
                                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.cancel();
                                            }
                                        });

                                        AlertDialog alert = confirmacion.create();
                                alert.setTitle("Eliminar Categoría");
                                alert.show();

                            }
                        });

        }

    }

    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        TextView tvMovieName;
        RatingBar ratingBar;
        ImageView ivMoviePoster;
        ImageButton eliminar;
        ImageButton modificar;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);

            tvMovieName=(TextView)itemView.findViewById(R.id.tv_name);
            ratingBar=(RatingBar)itemView.findViewById(R.id.rating_bar);
            ivMoviePoster=(ImageView)itemView.findViewById(R.id.movie_poster);
        }
    }
}