package com.example.test.model.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.test.R;
import com.example.test.server.Server;
import com.example.test.model.Event;
import com.example.test.model.adapters.listeners.CustomItemClickListener;

import java.util.List;

// Класс, обрабатывающий сущность для вывода на экран. Используется в RecyclerView
public class UsualEventRvAdapter extends RecyclerView.Adapter<UsualEventRvAdapter.EventViewHolder> {

    public static class EventViewHolder extends RecyclerView.ViewHolder {

        TextView eventName;
        TextView eventPlace;

        EventViewHolder(View itemView) {
            super(itemView);
            eventName = itemView.findViewById(R.id.event_name);
            eventPlace = itemView.findViewById(R.id.event_place);
        }
    }

    List<Event> events;
    CustomItemClickListener listener;

    // Конструктор класса
    public UsualEventRvAdapter(List<Event> events, CustomItemClickListener listener) {
        this.events = events;
        this.listener = listener;
    }

    // Удаляет мероприятие из списка
    public void removeEvent(int position, Context c) {
        Server.removeEvent(events.get(position), c);
        events.remove(position);
    }

    /* Методы, отвечающие за первоначальную настройку RecyclerView */
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_usual_event, viewGroup, false);
        final EventViewHolder eventViewHolder = new EventViewHolder(v);
        v.setOnClickListener(view -> listener.onItemClick(v, eventViewHolder.getPosition()));
        return eventViewHolder;
    }

    @Override
    public void onBindViewHolder(EventViewHolder eventViewHolder, int i) {
        eventViewHolder.eventName.setText(events.get(i).getName());
        eventViewHolder.eventPlace.setText(events.get(i).getPlace());
    }

    // Получение количества элементов в отображаемом списке
    @Override
    public int getItemCount() {
        return events.size();
    }

    // Замена списка отображаемых мероприятий на иной
    public void setEvents(List<Event> events) {
        this.events = events;
    }
}