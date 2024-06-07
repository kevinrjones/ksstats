# KSStats

## Notes

1. The database file in `database/cricket.sqlite` is empty, it's used by the JOOQ task to generate the Kotlin code to
   access the database

1. From the IDE you should run the app with `-Dlog4j.configurationFile=log4j2-dev.xml` this then uses the 
   `log4j2-dev.xml` in `ksstats/desktopMain/resources` rather than the `log4j2.xml` file that is then reserved for 
   production