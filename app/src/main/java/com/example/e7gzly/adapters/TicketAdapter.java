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
        holder.code.setText(model.getCode());

    }

    @NonNull
    @Override
    public TicketHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ticket_item , parent , false);
        return new TicketHolder(view);
    }

    class TicketHolder extends RecyclerView.ViewHolder {

        private TextView line;
        private TextView from;
        private TextView to;
        private TextView leave;
        private TextView arrive;
        private TextView class_ticket;
        private TextView date;
        private TextView code;

        public TicketHolder(@NonNull View itemView) {
            super(itemView);

            line = itemView.findViewById(R.id.line_ticket);
            from = itemView.findViewById(R.id.from_ticket);
            to = itemView.findViewById(R.id.to_ticket);
            leave = itemView.findViewById(R.id.leave_ticket);
            arrive = itemView.findViewById(R.id.arrive_ticket);
            class_ticket = itemView.findViewById(R.id.class_ticket);
            date = itemView.findViewById(R.id.date_ticket);
            code = itemView.findViewById(R.id.fawry_code);

        }
    }

}
