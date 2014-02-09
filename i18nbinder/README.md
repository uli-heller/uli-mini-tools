I18NBINDER
==========

A small tool converting between java localized message properties files and xls files.

This is just a wrapper around [i18n-binder](https://code.google.com/p/i18n-binder/).
The [i18n-binder](https://code.google.com/p/i18n-binder/) is basically an extension
to [ant](http://ant.apache.org). My wrapper looks more like an ordinary script.

From Properties To XLS
----------------------

Assume you have a collection of files like these:

* resources/com/porsche/ipl/messages/messages.properties
* resources/com/porsche/ipl/messages/messages_nl.properties
* resources/com/porsche/ipl/messages/messages_it.properties
* resources/com/porsche/ipl/messages/messages_it_CH.properties
* resources/com/porsche/ipl/messages/messages_ru.properties
* resources/com/porsche/ipl/messages/messages_jp.properties

With these, you'll be able go create an XLS file using this command:

```
$ ./i18nbinder-0.4.0.BUILD-SNAPSHOT.sh -c -d resources -x translations.xlsx
[i18nBinder] createXLSFile=true
[i18nBinder] xlsFileName=translations.xlsx
[i18nBinder] fileEncoding=UTF-8
[i18nBinder] fileNameLocaleGroupPattern=.*?((_\w{2,3}_\w{2,3})|(_\w{2,3})|())\.\w*
[i18nBinder] Create XLS file from property files...
[i18nBinder] ...done
```

The output will be an XLS file containing:

* a line for each property key
* a column for each translation

Use the option "-u" to get rid off the unicode escapes. The option "-e encoding" allows
you to adjust the file encoding.

From XLS To Properties
----------------------

The XLS file created with the method mentioned in the previous chapter 
allows you to recreate the properties files by executing this command:

```
$ ./i18nbinder-0.4.0.BUILD-SNAPSHOT.sh -x translations.xlsx
[i18nBinder] xlsFileName=translations.xlsx
[i18nBinder] fileEncoding=UTF-8
[i18nBinder] localeFilterRegex=.*
[i18nBinder] deletePropertiesWithBlankValue=false
[i18nBinder] Write properties from XLS file back to property files...
[i18nBinder] ...done
```

You don't have to specify a folder for the properties files, since
the XLS typically contains absolute filenames.
