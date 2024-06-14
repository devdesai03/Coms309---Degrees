package com.example.degrees;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * Adapter class to manage CourseRating objects within a RecyclerView.
 */
public class CourseRatingsAdapter extends RecyclerView.Adapter<CourseRatingsAdapter.ViewHolder> {
    public List<CourseRating> ratings;

    /**
     * Constructs a CourseRatingsAdapter with a list of CourseRating objects.
     */
    public CourseRatingsAdapter() {
        this.ratings = ratings;
    }

    /**
     * Creates a new ViewHolder instance for a CourseRating object.
     * @param parent The parent view group in which the new view will be added
     * @param viewType The type of view
     * @return A ViewHolder instance representing an item view
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View ratingView = inflater.inflate(R.layout.item_course_ratings, parent, false);
        return new ViewHolder(ratingView);
    }

    /**
     * Updates the contents of the ViewHolder to reflect the item at the given position.
     * @param holder The ViewHolder to be updated
     * @param position The position of the item within the adapter's data set
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CourseRating rating = ratings.get(position);
        holder.courseNameTextView.setText(rating.getCourseName());
        holder.instructorNameTextView.setText(rating.getInstructorName());
        holder.ratingValueTextView.setText(String.valueOf(rating.getRating()));
        holder.reviewTextTextView.setText(rating.getReviewText());
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     * @return The total number of items
     */
    @Override
    public int getItemCount() {
        return ratings.size();
    }

    /**
     * ViewHolder class to represent the item view.
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView courseNameTextView;
        public TextView instructorNameTextView;
        public TextView ratingValueTextView;
        public TextView reviewTextTextView;

        /**
         * Constructor for ViewHolder.
         * @param itemView The item view to be held by the ViewHolder
         */
        public ViewHolder(View itemView) {
            super(itemView);
            courseNameTextView = itemView.findViewById(R.id.courseName);
            instructorNameTextView = itemView.findViewById(R.id.instructorName);
            ratingValueTextView = itemView.findViewById(R.id.ratingValue);
            reviewTextTextView = itemView.findViewById(R.id.reviewText);
        }
    }
}
