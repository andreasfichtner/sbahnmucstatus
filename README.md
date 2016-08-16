A small application that queries the S-Bahn-München-punctuality interface.

Each minute, we query the "Störungsmeldungen"-website that is displayed in the app "München Navigator" and parse the content. It contains the punctuality for each S-Bahn line at that time. The result is written to a Sqlite-database.
