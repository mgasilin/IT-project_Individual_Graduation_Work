package com.example.test.event_showcase;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.test.R;
import com.example.test.server.Server;
import com.example.test.model.Comment;
import com.example.test.model.Event;
import com.example.test.model.adapters.CommentAdapter;

import java.util.ArrayList;
import java.util.List;

public class ShowcaseComments extends Fragment {
    private long user;
    private int size = 0;
    private Event shownEvent;
    private CommentAdapter adapter;
    private EditText comment;
    private TextView noCommentText;

    /* Методы update отвечают за обработку ответа от сервера: записывают информацию в поля класса,
    а также могут отвечать за ее отображение */
    @SuppressLint("NotifyDataSetChanged")
    public void update(List<Comment> comments) {
        if (size > comments.size()) {
            if (shownEvent != null)
                Server.findByEvent(shownEvent, getActivity());
        }
        if (adapter != null) {
            size = comments.size();
            adapter.setComments(comments);
            if (comments.size() <= 0) {
                noCommentText.setVisibility(View.VISIBLE);
            } else {
                noCommentText.setVisibility(View.GONE);
            }
            adapter.notifyDataSetChanged();
        }
    }

    public void update(Event event) {
        shownEvent = event;
    }

    /* Методы onCreate у акивностей, а также методы onCreate,
    onCreateView и newInstance у фрагментов отвечают за создание активности/фрагмента и вывод первоначальной информации */
    public ShowcaseComments() {
    }

    public static ShowcaseComments newInstance(long id, long user) {
        ShowcaseComments fragment = new ShowcaseComments();
        Bundle args = new Bundle();
        args.putLong("id", id);
        args.putLong("user", user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            user = getArguments().getLong("user");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_showcase_comments, container, false);
        comment = view.findViewById(R.id.new_comment);
        noCommentText = view.findViewById(R.id.emptyCommentList);
        AppCompatButton send = view.findViewById(R.id.send_new_comment);
        List<Comment> comments = new ArrayList<>();
        RecyclerView commentRecycler = view.findViewById(R.id.showcase_comments);
        adapter = new CommentAdapter(comments, getActivity());
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        commentRecycler.setLayoutManager(llm);
        commentRecycler.setAdapter(adapter);
        AppCompatButton update = view.findViewById(R.id.update_recycler);
        send.setOnClickListener(view12 -> {
            String text = comment.getText().toString();
            comment.setText("");
            if (text.isEmpty()) {
                Toast.makeText(getActivity(), "Не введен текст комментария", Toast.LENGTH_LONG).show();
            } else {
                Comment comment1 = new Comment(shownEvent.getId(), user, text);
                size++;
                Server.insertComment(comment1, getActivity());
            }
        });

        update.setOnClickListener(view1 -> Server.findByEvent(shownEvent, getActivity()));
        Server.findByEvent(shownEvent, getActivity());
        return view;
    }

    //Пауза автообновления при паузе фрагмента
    @Override
    public void onPause() {
        handler.removeCallbacks(updaterRunnable);
        super.onPause();
    }

    //Возобновление автообновления при продолжении работы фрагмента
    @Override
    public void onResume() {
        super.onResume();
        handler.postDelayed(updaterRunnable, 100);
    }

    //Создание автообновления
    private final Handler handler = new Handler();
    private final Runnable updaterRunnable = new Runnable() {
        public void run() {
            Server.findByEvent(shownEvent,getActivity());
            handler.postDelayed(this, 5000);
        }
    };
}