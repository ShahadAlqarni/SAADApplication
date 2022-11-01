package com.hfad.SAADApplication;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.View;
import android.annotation.SuppressLint;
import android.view.Gravity;
import android.widget.PopupMenu;
import android.widget.Toast;
import java.util.Calendar;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

public class aboutApp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View aboutPage = new AboutPage(this)
                .isRTL(false)
                .setImage(R.drawable.saadlogo)
                .setDescription("in  light  of  the  Kingdom  of  Saudi  Arabia’s  orientation  to" +
                        "  build  a  digital  society  duringVision  2030,  we  created  an  application(SAAD) to" +
                        "  report  accidents  immediately when they occur using IOT technologies without" +
                        " the need for external intervention.  This application(SAAD) helps speed up the " +
                        "arrival of ambulances and police to the exact location of the accident and the ability to save" +
                        " the injured in great proportion")
                .addGroup("CONNECT WITH US!")
                .addEmail("Shahad.Alqarni@hotmail.com")
                .addItem(createCopyright())
                .create();
        setContentView(aboutPage);
    }

    private Element createCopyright() {
        Element copyright = new Element();
        @SuppressLint("DefaultLocale") final String copyrightString = String.format("Copyright %d by Shahad Alqarni,Sarah Alharthi,Rahaf Alotaibi, and Relam Aljhany", Calendar.getInstance().get(Calendar.YEAR));
        copyright.setTitle(copyrightString);
        copyright.setGravity(Gravity.CENTER);
        copyright.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(aboutApp.this, copyrightString, Toast.LENGTH_SHORT).show();
            }
        });
        return copyright;
    }

    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.drawer_menu, popup.getMenu());
        popup.show();
    }
}

