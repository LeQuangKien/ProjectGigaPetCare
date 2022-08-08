package tuan.aprotrain.projectpetcare.Adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import tuan.aprotrain.projectpetcare.R;
import tuan.aprotrain.projectpetcare.entity.Service;


@SuppressLint("UseSparseArrays")
public class ExpandLVCheckBox extends BaseExpandableListAdapter {

    private Context mContext;
    private ArrayList<String> mListCategory;
    private HashMap<String, List<Service>> mListService;
    private HashMap<Integer, boolean[]> mChildCheckStates;
    private ChildViewHolder childViewHolderNameService;

    private GroupViewHolder groupViewHolder;
    private String groupText;
    private String childText;

    public ExpandLVCheckBox(Context context, ArrayList<String> listCategory, HashMap<String, List<Service>> lisService){

        mContext = context;
        mListCategory = listCategory;
        mListService = lisService;

        mChildCheckStates = new HashMap<Integer, boolean[]>();
    }

    public int getNumberOfCheckedItemsInGroup(int mGroupPosition)
    {
        boolean getChecked[] = mChildCheckStates.get(mGroupPosition);
        int count = 0;
        if(getChecked != null) {
            for (int j = 0; j < getChecked.length; ++j) {
                if (getChecked[j] == true) count++;
            }
        }
        return  count;
    }

    @Override
    public int getGroupCount() {
        return mListCategory.size();
    }


    @Override
    public String getGroup(int groupPosition) {
        return mListCategory.get(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }


    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {


        groupText = getGroup(groupPosition);

        if (convertView == null) {

            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.layout_item_group, null);


            groupViewHolder = new GroupViewHolder();

            groupViewHolder.mGroupText = (TextView) convertView.findViewById(R.id.petCate);

            convertView.setTag(groupViewHolder);
        } else {

            groupViewHolder = (GroupViewHolder) convertView.getTag();
        }

        groupViewHolder.mGroupText.setText(groupText);

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mListService.get(mListCategory.get(groupPosition)).size();
    }


    @Override
    public String getChild(int groupPosition, int childPosition) {
        return mListService.get(mListCategory.get(groupPosition)).get(childPosition).getServiceName();
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        final int mGroupPosition = groupPosition;
        final int mChildPosition = childPosition;


        //childText = getChild(mGroupPosition, mChildPosition);

        if (convertView == null) {

            LayoutInflater inflater = (LayoutInflater) this.mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.layout_item, null);

            childViewHolderNameService = new ChildViewHolder();


            childViewHolderNameService.textViewName = (TextView) convertView
                    .findViewById(R.id.petService);
            childViewHolderNameService.textViewPrice = (TextView) convertView
                    .findViewById(R.id.servicePrice);
            childViewHolderNameService.textViewTime = (TextView) convertView
                    .findViewById(R.id.serviceTime);

            childViewHolderNameService.mCheckBox = (CheckBox) convertView
                    .findViewById(R.id.lstcheckBox);
            childViewHolderNameService.mCheckBox = (CheckBox) convertView
                    .findViewById(R.id.lstcheckBox);
            childViewHolderNameService.mCheckBox = (CheckBox) convertView
                    .findViewById(R.id.lstcheckBox);

            convertView.setTag(R.layout.layout_item, childViewHolderNameService);

        } else {

            childViewHolderNameService = (ChildViewHolder) convertView
                    .getTag(R.layout.layout_item);
            childViewHolderNameService = (ChildViewHolder) convertView
                    .getTag(R.layout.layout_item);
            childViewHolderNameService = (ChildViewHolder) convertView
                    .getTag(R.layout.layout_item);
        }

        childViewHolderNameService.textViewName.setText(mListService.get(mListCategory.get(groupPosition)).get(childPosition).getServiceName());
        childViewHolderNameService.textViewPrice.setText("price: "+mListService.get(mListCategory.get(groupPosition)).get(childPosition).getServicePrice()+"$");
        childViewHolderNameService.textViewTime.setText("time: "+mListService.get(mListCategory.get(groupPosition)).get(childPosition).getServiceTime()+" mins");


        childViewHolderNameService.mCheckBox.setOnCheckedChangeListener(null);
        childViewHolderNameService.mCheckBox.setOnCheckedChangeListener(null);
        childViewHolderNameService.mCheckBox.setOnCheckedChangeListener(null);

        if (mChildCheckStates.containsKey(mGroupPosition)) {

            boolean getChecked[] = mChildCheckStates.get(mGroupPosition);


            childViewHolderNameService.mCheckBox.setChecked(getChecked[mChildPosition]);
            childViewHolderNameService.mCheckBox.setChecked(getChecked[mChildPosition]);
            childViewHolderNameService.mCheckBox.setChecked(getChecked[mChildPosition]);

        } else {


            boolean getChecked[] = new boolean[getChildrenCount(mGroupPosition)];


            mChildCheckStates.put(mGroupPosition, getChecked);


            childViewHolderNameService.mCheckBox.setChecked(false);

        }

        childViewHolderNameService.mCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {

                    boolean getChecked[] = mChildCheckStates.get(mGroupPosition);
                    getChecked[mChildPosition] = isChecked;
                    mChildCheckStates.put(mGroupPosition, getChecked);

                } else {

                    boolean getChecked[] = mChildCheckStates.get(mGroupPosition);
                    getChecked[mChildPosition] = isChecked;
                    mChildCheckStates.put(mGroupPosition, getChecked);
                }
            }
        });



        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    public final class GroupViewHolder {

        TextView mGroupText;
    }

    public final class ChildViewHolder {

        TextView textViewName,textViewPrice, textViewTime;
        CheckBox mCheckBox;
    }
}