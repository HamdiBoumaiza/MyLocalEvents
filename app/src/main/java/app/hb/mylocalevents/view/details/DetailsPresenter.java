package app.hb.mylocalevents.view.details;

public class DetailsPresenter implements DetailsContract.IDetailsPresenter {

    private DetailsContract.IDetailsView detailsView;

    public DetailsPresenter(DetailsContract.IDetailsView detailsView) {
        this.detailsView = detailsView;
    }

    @Override
    public void goToReservation(String url) {
        detailsView.openWebReservation(url);

    }

    @Override
    public void goToMaps(String name, String lat, String lng) {
        detailsView.openMaps(name, lat, lat);
    }
}
