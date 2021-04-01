package com.innova.sharks.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.innova.sharks.R;
import com.innova.sharks.databinding.ItemBookBinding;
import com.innova.sharks.models.Book;

import java.util.ArrayList;
import java.util.List;


public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {
    private static final String TAG = "BookAdapter";

    private final Context mContext;
    private OnItemClickListener listener;
    private List<Book> mBooks = new ArrayList<>();

    public BookAdapter(Context context) {
        mContext = context;
    }

    public void setData(List<Book> books) {
        mBooks = books;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemBookBinding binding = ItemBookBinding.inflate(layoutInflater, parent, false);
        return new BookViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        Book book = mBooks.get(position);
        if (book != null)
            holder.bindTo(book);

        holder.mBinding.card.setOnClickListener(view -> {
            if (listener != null)
                listener.setOnBookCardClickListener(mBooks.get(position));
        });

    }

    @Override
    public int getItemCount() {
        return mBooks.size();
    }

    public interface OnItemClickListener {
        void setOnBookCardClickListener(Book book);
    }

    public class BookViewHolder extends RecyclerView.ViewHolder {
        ItemBookBinding mBinding;

        public BookViewHolder(ItemBookBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        public void bindTo(Book book) {
            // Update views
            mBinding.title.setText(book.getTitle());
            mBinding.author.setText(book.getAuthor());

            Glide.with(mContext)
                    .load(book.getImage())
                    .centerCrop()
                    .placeholder(R.drawable.ic_baseline_person_24)
                    .into(mBinding.image);
        }
    }
}
