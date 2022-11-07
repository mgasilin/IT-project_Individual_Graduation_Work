package com.example.test.model.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.test.R;
import com.example.test.model.Comment;

import java.util.List;

// Класс, обрабатывающий сущность для вывода на экран. Используется в RecyclerView
public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {
    public static class CommentViewHolder extends RecyclerView.ViewHolder {

        TextView commentAuthor;
        TextView commentText;

        CommentViewHolder(View itemView) {
            super(itemView);
            commentAuthor = itemView.findViewById(R.id.author);
            commentText = itemView.findViewById(R.id.comment_text);
        }
    }

    List<Comment> comments;
    private Context context;

    // Конструктор класса
    public CommentAdapter(List<Comment> comments, Context context) {
        this.comments = comments;
        this.context = context;
    }

    /* Методы, отвечающие за первоначальную настройку RecyclerView */
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public CommentAdapter.CommentViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_comment, viewGroup, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CommentAdapter.CommentViewHolder commentViewHolder, int i) {
        commentViewHolder.commentAuthor.setText(comments.get(i).getAuthorName());
        commentViewHolder.commentText.setText(comments.get(i).getMessage());
    }

    // Возвращает количество элементов в списке
    @Override
    public int getItemCount() {
        return comments.size();
    }

    // Устанавливает новый список комментариев для отображения
    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}
