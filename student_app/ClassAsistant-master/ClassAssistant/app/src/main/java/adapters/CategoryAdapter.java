package adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.uninorte.classassistant.R;

import minimum.MinCategory;

/**
 * Created by asmateus on 29/05/17.
 */

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyHolder> {
    private LayoutInflater inflater;
    private MinCategory data;
    private Context master;

    public CategoryAdapter(Context context, MinCategory data) {
        this.master = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.element_list_element, parent, false);
        MyHolder holder = new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        holder.element_description.setText(data.getItemsDescriptions().get(position));
        holder.element_weight.setText(Integer.toString(data.getItemsWeights().get(position)));
    }

    @Override
    public int getItemCount() {
        return data.getItemsWeights().size();
    }

    class MyHolder extends RecyclerView.ViewHolder {
        private TextView element_description;
        private TextView element_weight;

        public MyHolder(View itemView) {
            super(itemView);

            element_description = (TextView) itemView.findViewById(R.id.list_element_description);
            element_weight = (TextView) itemView.findViewById(R.id.list_element_weight);
        }
    }
}
