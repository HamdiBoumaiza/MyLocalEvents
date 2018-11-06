package app.hb.mylocalevents.view.settings;

import java.util.ArrayList;

import app.hb.mylocalevents.models.Category;

/**
 * This specifies the contract between the view and the presenter.
 */
public interface SettingContract {

    interface ISettingView {

        void showCategoryError(String error);

        void displayCategories(ArrayList<Category> categories);

       // void showPicker();

    }

    interface ISettingPresenter {

        void getCategories(String token);

    }
}
