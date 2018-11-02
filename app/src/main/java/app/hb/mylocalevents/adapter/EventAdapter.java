package app.hb.mylocalevents.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import app.hb.mylocalevents.BR;
import app.hb.mylocalevents.R;
import app.hb.mylocalevents.callback.IEventClickListener;
import app.hb.mylocalevents.databinding.ItemEventBinding;
import app.hb.mylocalevents.models.Event;


public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {

    private List<Event> mItems;
    private Context mcontext;
    private IEventClickListener iEventClickListener;

    public EventAdapter(Context context, List<Event> items, IEventClickListener iEventClickListener) {
        this.mItems = items;
        this.mcontext = context;
        this.iEventClickListener = iEventClickListener;
    }

    public void AddEvents(List<Event> mItemsList) {
        mItems.addAll(mItemsList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        ItemEventBinding itemEventBinding = DataBindingUtil.inflate(layoutInflater,
                R.layout.item_event,
                parent,
                false);

        return new ViewHolder(itemEventBinding.getRoot());
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Event mEvent = mItems.get(position);
        holder.bind(mEvent);
    }


    @Override
    public int getItemCount() {
        return mItems == null ? 0 : mItems.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private ItemEventBinding itemEventBinding;

        ViewHolder(View itemView) {
            super(itemView);
            itemEventBinding = DataBindingUtil.bind(itemView);
        }


        void bind(Event event) {
            itemEventBinding.setVariable(BR.event, event);
            itemEventBinding.setVariable(BR.position, getAdapterPosition());
            itemEventBinding.setVariable(BR.eventClickListener, iEventClickListener);


            if (event.getLogo() != null)
                itemEventBinding.setVariable(BR.eventImage, event.getLogo().getUrl());

            if (event.getCategory() != null) {
                itemEventBinding.tvTechnology.setVisibility(View.VISIBLE);
                itemEventBinding.setVariable(BR.technology, event.getCategory().getShort_name());
            }

            if (event.getVenue() != null) {
                itemEventBinding.llLocation.setVisibility(View.VISIBLE);
                itemEventBinding.setVariable(BR.city, event.getVenue().getName());
            }


            if (event.isFree()) {
                itemEventBinding.setVariable(BR.price, mcontext.getString(R.string.free));
            } else {
                itemEventBinding.setVariable(BR.price, mcontext.getString(R.string.paid));
            }

            itemEventBinding.executePendingBindings();
        }

    }
}
