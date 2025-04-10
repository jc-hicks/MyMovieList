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

    // be able to print both the movie list and the watchlist
    private void printMovies() {
    }

    // look up movie by title, add it if it does not exist
    private void lookupMovie() {
    }

    private void lookupWatchlist() {
    }

    private void sortMovieList() {
    }

    private void filterMovieList() {
    }

    private void filterWatchlist() {
    }

    private void sortWatchlist() {
    }

    private void addMovieToWatchlist() {
    }

    private void removeMovieFromWatchlist() {
    }

    private void saveWatchlist() {
    }

    private void loadWatchlist() {
    }

    private void rateMovie() {
    }

}
