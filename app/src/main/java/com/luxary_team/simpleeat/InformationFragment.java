package com.luxary_team.simpleeat;

import android.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ShareCompat;
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
        mInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent vkintent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.send_my_vk)));
                startActivity(vkintent);
            }
        });

        mContactButton = (Button) view.findViewById(R.id.contact_with_us);
        mContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo unhardcode
                String body = getPhoneInfo();
                Intent shareIntent = ShareCompat.IntentBuilder.from(getActivity())
                        .setType("text/rfc822")
                        .setText(body)
                        .setSubject(getString(R.string.send_subject))
                        .setChooserTitle(getString(R.string.send_chooser_title))
                        .setEmailTo(new String[]{getString(R.string.send_my_email)})
                        .createChooserIntent();

                //// new Intent(Intent.ACTION_SEND);
//                shareIntent.setType("text/rfc822");
//                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Отзыв о приложении");
//                shareIntent.putExtra(Intent.EXTRA_TEXT, body);
//                shareIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"ng@redmadrobot.com"});
                startActivity(shareIntent);
            }
        });

        return view;
    }

    private String getPhoneInfo() {
        String version;
        String deviceName = Build.MODEL;
        String osVersion = String.format("%s (SDK %s)", Build.VERSION.RELEASE, Build.VERSION.SDK_INT);
//        Build.VERSION.RELEASE + " (SDK " + Build.VERSION.SDK_INT + ")";
        try {
            version = String.valueOf(getActivity().getPackageManager()
                    .getPackageInfo(getActivity().getPackageName(), 0)
                    .versionCode);
        } catch (PackageManager.NameNotFoundException e) {
            version = "null";
            Log.d(MainActivity.TAG, "Error in get version");
        }
        String result = String.format(getString(R.string.send_device_info), version, deviceName, osVersion);
//        "\n\nВерсия приложения: " + version + "\nУстройство: " + deviceName +
//                "\nВерсия ОС: Android " + osVersion;
        return result;
    }
}
