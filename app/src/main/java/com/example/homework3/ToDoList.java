package com.example.homework3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ToDoList extends ArrayAdapter<String>{
    Context context;
    ArrayList<String> title;

    ToDoList(Context c, ArrayList<String> title) {
        super(c, R.layout.list,title);
        this.context = c;
        this.title=title;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater vi = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View row = vi.inflate(R.layout.list, parent, false);
        TextView titlee = (TextView) row.findViewById(R.id.listView);
        int pos = position+1;
        titlee.setText(+pos + ". " + title.get(position));
        pos++;
        return row;
    }
}
