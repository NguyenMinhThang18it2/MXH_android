package thang.com.uptimum.Main;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import thang.com.uptimum.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class FriendFragment extends Fragment {
    private View view;
    private TextView tvhome;
    private int abcde=0;
    public FriendFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_newfriend, container, false);
        tvhome = (TextView) view.findViewById(R.id.tvhome);
        tvhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return view;
    }
}
