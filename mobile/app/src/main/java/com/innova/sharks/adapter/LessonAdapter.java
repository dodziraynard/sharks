package com.innova.sharks.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.innova.sharks.R;
import com.innova.sharks.databinding.ItemLessonBinding;
import com.innova.sharks.models.Lesson;
import com.innova.sharks.ui.quiz.QuizActivity;

import java.util.ArrayList;
import java.util.List;

import static com.innova.sharks.utils.Constants.QUIZ_LESSON;

public class LessonAdapter extends RecyclerView.Adapter<LessonAdapter.ViewHolder> {
    private final String TAG = this.getClass().getSimpleName();
    private final Context mContext;
    private List<Lesson> lessons;

    private OnItemClickListener listener;

    public LessonAdapter(Context context) {
        mContext = context;
        lessons = new ArrayList<>();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setData(List<Lesson> lessons) {
        this.lessons = lessons;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemLessonBinding binding = ItemLessonBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull LessonAdapter.ViewHolder holder, int position) {
        Lesson lesson = lessons.get(position);
        holder.binding.title.setText(lesson.getTitle());
        holder.binding.description.setText(lesson.getDescription());

        GlideUrl url = new GlideUrl(lesson.getImage(), new LazyHeaders.Builder()
                .addHeader("User-Agent", "Sharks-App")
                .build());
        Glide.with(mContext)
                .load(url)
                .centerCrop()
                .placeholder(R.drawable.exo_controls_play)
                .into(holder.binding.imageIcon);

        if (lesson.getQuizScore() != null && !lesson.getQuizScore().isEmpty()) {
            holder.binding.scoreText.setText(lesson.getQuizScore());
            holder.binding.scoreText.setVisibility(View.VISIBLE);
        } else {
            holder.binding.scoreText.setVisibility(View.GONE);
        }
        if (lesson.hasQuizzes()) {
            holder.binding.quizzesButton.setVisibility(View.VISIBLE);
        } else {
            holder.binding.quizzesButton.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return lessons.size();
    }

    public interface OnItemClickListener {
        void setOnLessonCardClickListener(Lesson lesson);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final ItemLessonBinding binding;

        public ViewHolder(@NonNull ItemLessonBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.card.setOnClickListener(view -> {
                if (listener != null)
                    listener.setOnLessonCardClickListener(lessons.get(getLayoutPosition()));
            });
            binding.quizzesButton.setOnClickListener(view -> {
                Intent intent = new Intent(mContext, QuizActivity.class);
                intent.putExtra(QUIZ_LESSON, lessons.get(getLayoutPosition()));
                intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                mContext.startActivity(intent);
            });
        }
    }
}
