package controller;

import model.IMovieModel;

public class IController {
    private IMovieModel model;

    public IController(IMovieModel model) {
        this.model = model;
    }

    public void start() {
        // start the args controller here
    }

    public void printMovies() {
        // should this be shareMovieList??
    }

    public void lookupMovie() {
        // return a list of movie records
    }

    public void lookupWatchlist() {
        // return a list of movie records
    }

    public void sortMovieList() {
        // return sorted movie list
    }

    public void filterMovieList() {
        // return filtered movie list
    }

    public void addMovieToMovieList() {
        // receive a movie record from the view, and the controller
        // tells the model to add it to the watchlist
    }

    public void removeMovieFromMovieList() {
        // removes a movie record from the view, and the controller
        // tells the model to remove it from the watchlist
    }
}
