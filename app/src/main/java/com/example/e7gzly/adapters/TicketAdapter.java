package com.example.e7gzly.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e7gzly.R;
import com.example.e7gzly.model.PassengerInfo;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class TicketAdapter extends FirebaseRecyclerAdapter<PassengerInfo, TicketAdapter.TicketHolder> {


    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public TicketAdapter(@NonNull FirebaseRecyclerOptions<PassengerInfo> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull TicketHolder holder, int position, @NonNull PassengerInfo model) {

        holder.line.setText(model.getTrain_line());
        holder.from.setText(model.getFrom());
        holder.to.setText(model.getTo());
        holder.leave.setText(model.getLeave());
        holder.arrive.setText(model.getArrive());
        holder.class_ticket.setText(model.getTrain_class());
        holder.date.setText(model.getDate());

    }

    @NonNull
    @Override
    public TicketHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ticket_item , parent , false);
        return new TicketHolder(view);
    }

    class TicketHolder extends RecyclerView.ViewHolder {

        TextView direction;
        TextView line;
        TextView tv_from;
        TextView from;
        TextView tv_to;
        TextView to;
        TextView tv_leave;
        TextView leave;
        TextView tv_arrive;
        TextView arrive;
        TextView tv_class;
        TextView class_ticket;
        TextView tv_date;
        TextView date;

        public TicketHolder(@NonNull View itemView) {
            super(itemView);

            direction = itemView.findViewById(R.id.tv_direction);
            line = itemView.findViewById(R.id.line_ticket);
            tv_from = itemView.findViewById(R.id.tv_from);
            from = itemView.findViewById(R.id.from_ticket);
            tv_to = itemView.findViewById(R.id.tv_to);
            to = itemView.findViewById(R.id.to_ticket);
            tv_leave = itemView.findViewById(R.id.tv_leave);
            leave = itemView.findViewById(R.id.leave_ticket);
            tv_arrive = itemView.findViewById(R.id.tv_arrive);
            arrive = itemView.findViewById(R.id.arrive_ticket);
            tv_class = itemView.findViewById(R.id.tv_class);
            class_ticket = itemView.findViewById(R.id.class_ticket);
            tv_date = itemView.findViewById(R.id.tv_date);
            date = itemView.findViewById(R.id.date_ticket);

        }
    }

}
