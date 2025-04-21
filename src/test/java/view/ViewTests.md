# Test searching a single movie adds it to the list
![alt text](<../../../../DesignDocuments/View Design Documentation/Adding Existing movie.png>) 

# Test adding an api key
![alt text](<../../../../DesignDocuments/View Design Documentation/API Key button added.png>)

# Test 
 ![alt text](<../../../../DesignDocuments/View Design Documentation/API Key Field added.png>) 

 # Test adding the same movie twice does not show a duplicate in the main list.
 Added the movie Dredd, and then searched it again to confirm the movies on the list are unique.
 ![alt text](<../../../../DesignDocuments/View Design Documentation/Existing movie not Duped.png>) 

 # Test Changing a rating
 Changed the rating for Lord of the Rings in the My Watchlist field. Confirmed that this only changed the rating for the movie in MyWatchlist, and not for the movie shown in the master list.
 ![alt text](<../../../../DesignDocuments/View Design Documentation/TestChangingARatingWatchlist.png>) 

# Test Sort Ascending or Descending by Title
Chose the Title field, and descending/ ascending options. Checked what was shown in the results vs. expected distribution
 
 ![alt text](<../../../../DesignDocuments/View Design Documentation/TestsortDescending.png>) 

# Test Filtering by Year

Selected the year filter, and tested searching for years that are out of range of any movies in the list.

 ![alt text](<../../../../DesignDocuments/View Design Documentation/TestYearHigh.png>)
![alt text](<../../../../DesignDocuments/View Design Documentation/TestYearLow.png>) 

# Test Adding a Duplicate to Watchlist
Added Lord of the Rings to the watchlist, and then tested adding it a second time to ensure the error prompt triggered.
![alt text](<../../../../DesignDocuments/View Design Documentation/WatchlistDoubleAdd.png>)