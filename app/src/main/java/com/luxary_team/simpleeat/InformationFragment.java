package com.luxary_team.simpleeat;

import android.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class InformationFragment extends Fragment {

    private Button mInfoButton;
    private Button mContactButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.information_fragment, container, false);

        mInfoButton = (Button) view.findViewById(R.id.inf_button);

        mContactButton = (Button) view.findViewById(R.id.contact_with_us);
        mContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo unhardcode
                String body = getPhoneInfo();
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/rfc822");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Отзыв о приложении");
                shareIntent.putExtra(Intent.EXTRA_TEXT, body);
                shareIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"ng@redmadrobot.com"});
                startActivity(Intent.createChooser(shareIntent, "Отправить с помощью..."));
            }
        });

        return view;
    }

    private String getPhoneInfo() {
        String version;
        String deviceName = Build.MODEL;
        String osVersion = Build.VERSION.RELEASE + " (SDK " + Build.VERSION.SDK_INT + ")";
        try {
            version = String.valueOf(getActivity().getPackageManager()
                    .getPackageInfo(getActivity().getPackageName(), 0)
                    .versionCode);
        } catch (PackageManager.NameNotFoundException e) {
            version = "null";
            Log.d(MainActivity.TAG, "Error in get version");
        }
        String result = "\n\nВерсия приложения: " + version + "\nУстройство: " + deviceName +
                "\nВерсия ОС: Android " + osVersion;
        return result;
    }
}
