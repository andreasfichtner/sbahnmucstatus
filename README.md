### A small application that queries the S-Bahn-München-punctuality interface.

Each minute, we query the "Störungsmeldungen"-website that is displayed in the app "München Navigator" and parse the content. It contains the punctuality for each S-Bahn line at that time. The result is written to a sqlite database.
An example output between september 2015 and february 2016 can be found [here](https://github.com/retterdesapok/sbahnmucstatus/blob/master/exampledata/exampledata.sqlite "exampledata.sqlite").
