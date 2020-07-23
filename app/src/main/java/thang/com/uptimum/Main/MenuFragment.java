package thang.com.uptimum.Main;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import thang.com.uptimum.R;
import thang.com.uptimum.login.LoginActivity;
import thang.com.uptimum.login.SessionManagement;

/**
 * A simple {@link Fragment} subclass.
 */
public class MenuFragment extends Fragment {
    private View contactsview;
    private Button btnLogout;
    private SessionManagement sessionManagement;

    public MenuFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        contactsview = inflater.inflate(R.layout.fragment_menu, container, false);
        btnLogout = (Button) contactsview.findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionManagement = new SessionManagement(getActivity().getApplicationContext());
                sessionManagement.removeSession();
                LogoutSuccess();
            }
        });
        return contactsview;
    }

    private void LogoutSuccess() {
        Intent intent = new Intent(getActivity().getApplicationContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        getActivity().finish();
    }
}
