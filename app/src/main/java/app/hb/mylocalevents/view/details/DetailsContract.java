package app.hb.mylocalevents.view.details;

/**
 * This specifies the contract between the view and the presenter.
 */
public interface DetailsContract {

    interface IDetailsView {
        void openWebReservation(String url);

        void openMaps(String name, String lat, String lng);

    }

    interface IDetailsPresenter {
        void goToReservation(String url);
        void goToMaps(String name, String lat, String lng);


    }
}
