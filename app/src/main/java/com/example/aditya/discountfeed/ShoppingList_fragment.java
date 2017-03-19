package com.example.aditya.discountfeed;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

import static com.google.android.gms.analytics.internal.zzy.v;


/**
 * A simple {@link Fragment} subclass.
 */
public class ShoppingList_fragment extends ListFragment  implements AdapterView.OnItemClickListener  {
    private ArrayList<String> list = new ArrayList<String>();
    private ArrayAdapter<String> allItemsAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.shopping_list_fragment, container, false);
        return view;
    }

//
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//            Button button = (Button) getView().findViewById(R.id.addItem);
//
//        ArrayList<String> item = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.brands)));
//        ;
//        allItemsAdapter = new ArrayAdapter<String>(getActivity().getBaseContext(), android.R.layout.simple_list_item_1, item);
//        ListFragment.setListAdapter(allItemsAdapter);

    ArrayAdapter adapter = ArrayAdapter.createFromResource(getActivity(),
            R.array.brands, android.R.layout.simple_list_item_1);
    setListAdapter(adapter);
    getListView().setOnItemClickListener(this);
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        int p = position+1;
        Toast.makeText(getActivity(), "Item: " +p, Toast.LENGTH_SHORT).show();

    }






















//    public void onClick(View v) {
//        EditText edit = (EditText) getView().findViewById(R.id.editText);
//        list.add(edit.getText().toString());
//        edit.setText("");
//        allItemsAdapter.notifyDataSetChanged();
//    }


}








