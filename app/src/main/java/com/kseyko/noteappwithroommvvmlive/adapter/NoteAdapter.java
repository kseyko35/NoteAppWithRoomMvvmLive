package com.kseyko.noteappwithroommvvmlive.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.kseyko.noteappwithroommvvmlive.R;
import com.kseyko.noteappwithroommvvmlive.model.Note;

/**
 * Code with ❤
 * ╔════════════════════════════╗
 * ║   Created by Seyfi ERCAN   ║
 * ╠════════════════════════════╣
 * ║  seyfiercan35@hotmail.com  ║
 * ╠════════════════════════════╣
 * ║      30,June,2021      ║
 * ╚════════════════════════════╝
 */
public class NoteAdapter extends ListAdapter<Note,NoteAdapter.NoteViewHolder> {

    private IOnClickListener listener;

    public NoteAdapter() {
        super(DIFF_CALLBACK);
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.note_item,parent,false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note currentItem = getItem(position);
        holder.txtTitle.setText(currentItem.getTitle());
        holder.txtDescription.setText(currentItem.getDescription());
        holder.txtPriority.setText(String.valueOf(currentItem.getPriority()) );
    }

    public Note getNoteAt(int position) {
        return getItem(position);
    }

    public class NoteViewHolder extends RecyclerView.ViewHolder {
        private TextView txtTitle,txtDescription,txtPriority;
        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            this.txtTitle=itemView.findViewById(R.id.txtTitle);
            this.txtDescription= itemView.findViewById(R.id.txtDescription);
            this.txtPriority= itemView.findViewById(R.id.txtPriority);

            itemView.setOnClickListener(view -> {
                if (listener!=null){
                    listener.onItemViewClick(getItem(getAdapterPosition()));
                }
            });
        }
    }

    public interface IOnClickListener {
        void onItemViewClick(Note note);
    }
    public void setOnClickListener(IOnClickListener listener){
        this.listener=listener;
    }

    private static final DiffUtil.ItemCallback<Note> DIFF_CALLBACK=new DiffUtil.ItemCallback<Note>() {
        @Override
        public boolean areItemsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {
            return oldItem.getId()== newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {
            return oldItem.getTitle().equals(newItem.getTitle()) &&
                    oldItem.getDescription().equals(newItem.getDescription()) &&
                    oldItem.getPriority() == newItem.getPriority();
        }
    };
}
