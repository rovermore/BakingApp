package com.example.rovermore.bakingapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.rovermore.bakingapp.R;
import com.example.rovermore.bakingapp.datamodel.Step;

import java.util.List;

public class StepListAdapter extends RecyclerView.Adapter<StepListAdapter.StepViewHolder> {

    private List<Step> stepList;
    private Context context;
    private onStepClickHandler mOnStepClickHandler;

    public interface onStepClickHandler{
        void onStepClicked(Step step);
    }

    public StepListAdapter(Context context, List<Step> stepList, onStepClickHandler mOnStepClickHandler){
        this.context = context;
        this.stepList = stepList;
        this.mOnStepClickHandler = mOnStepClickHandler;
    }

    @NonNull
    @Override
    public StepListAdapter.StepViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.step_item_list,viewGroup,false);

        return new StepViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull StepViewHolder stepViewHolder, int i) {
        Step step = stepList.get(i);
        stepViewHolder.stepDescription.setText(step.getShortDescription());
    }

    @Override
    public int getItemCount() {
        if(stepList==null)return 0;
        return stepList.size();
    }

    public class StepViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView stepDescription;

        public StepViewHolder(@NonNull View itemView) {
            super(itemView);

            stepDescription = itemView.findViewById(R.id.tv_step_description);
            stepDescription.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            mOnStepClickHandler.onStepClicked(stepList.get(position));
        }
    }

    public void setStepList(List<Step> stepList){
        this.stepList = stepList;
        notifyDataSetChanged();
    }

    public void clearStepListAdapter(){
        if(stepList!=null) {
            stepList.clear();
            stepList = null;
            notifyDataSetChanged();
        }
    }
}
