# Design Documents

You may have multiple design documents for this project. Place them all in this folder. File naming is up to you, but it should be clear what the document is about. At the bare minimum, you will want a pre/post UML diagram for the project. 


# Preliminary Diagram

```mermaid
classDiagram

    class NetUtils {
        - static final String API_URL_FORMAT
        + getMovieUrl(String ip) String
        + getUrlContents(String urlStr) InputStream
        + getMovieDetails(String url) InputStream
    }

    class movieRecord {
    }

    class IMovieRecord{
    }

    class myWatchListApp {
        + main(String[] args) void
    }

    class ArgsController {
    }

    class watchListController{
    }

    class movieWatchList{
    }

    class IView {
        <<interface>>
        +start(): void
    }

    class watchListView {
    }

    IView ..|> watchListView : implements
    watchListController ..> ArgsController: uses
    movieRecord ..|> IMovieRecord: implements
    movieWatchList --> movieRecord: uses
    myWatchListApp --> watchListController
    myWatchListApp --> IView
    myWatchListApp --> IMovieRecord
    movieRecord --> NetUtils: uses

```