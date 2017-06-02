package adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.uninorte.classassistant.EvaluateStudentActivity;
import com.uninorte.classassistant.R;

import minimum.MinCategory;
import minimum.MinRubric;

/**
 * Created by asmateus on 1/06/17.
 */

public class GradingAdapter extends RecyclerView.Adapter<GradingAdapter.MyHolder> {
    private LayoutInflater inflater;
    private MinRubric data;
    private EvaluateStudentActivity master;

    public GradingAdapter(EvaluateStudentActivity context, MinRubric data) {
        this.master = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.element_student_evaluate, parent, false);
        MyHolder holder = new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        if(data.getLabelFields().get(position) == 1) {
            // This is a category title
            holder.element_weight.setText("");
            holder.element_weight.setEnabled(false);

            for(int i = 0; i < data.getCategories().size(); ++i) {
                if(data.getCategories().get(i).getId().equals(data.getLabelIDs().get(position))) {
                    holder.element_description.setText(data.getCategories().get(i).getName());
                    break;
                }
            }
            holder.element_description.setTextColor(Color.parseColor("#E12929"));
        }
        else {
            holder.element_weight.setEnabled(true);
            holder.element_weight.setText("" + data.getScoreFields().get(position));
            holder.element_description.setText(data.getLabelIDs().get(position));
        }
    }

    @Override
    public int getItemCount() {
        return data.getLabelFields().size();
    }

    class MyHolder extends RecyclerView.ViewHolder {
        private TextView element_description;
        private EditText element_weight;

        public MyHolder(View itemView) {
            super(itemView);

            element_description = (TextView) itemView.findViewById(R.id.element_grade_description);
            element_weight = (EditText) itemView.findViewById(R.id.grade_val);
            element_weight.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        String val;
                        if(element_weight.getText().toString().isEmpty()) {
                            val = "0.0";
                        }
                        else {
                            val = element_weight.getText().toString();
                        }
                        master.updateValue(getAdapterPosition(), Double.parseDouble(val));
                    }
                }
            });
        }
    }
}
