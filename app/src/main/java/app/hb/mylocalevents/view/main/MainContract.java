package app.hb.mylocalevents.view.main;

import android.app.Activity;
import android.content.Context;

import java.util.ArrayList;

import app.hb.mylocalevents.models.Event;

/**
 * This specifies the contract between the view and the presenter.
 */
public interface MainContract {

    interface IMainView {
        void hideProgressBar();

        void showEvents(ArrayList<Event> mEventList);

    }

    interface IMainPresenter {
        void getEvents(String token, String longitude, String latitude, String date, String range, String idCategory);

    }
}
