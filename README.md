# JSnap---Screenshot-capture-in-Java

JSnap is a Java application that allows you to capture as many screenshots you like and save them all together
or choose to keep the ones you like.

It allows you to rename them and choose the destination folder in which the application will save them.

The application uses the [jNativeHook](https://github.com/kwhat/jnativehook) library for global keystrokes, capturing screenshots with the PRINTSCREEN key.

The .jar executable of the application can be found in the 'dist' directory.

Application screenshot:
![Alt text](https://github.com/QISnaith/JSnap-Multilang/blob/master/examples/JSnap_screenshot.png)

## Set Custom language
if you want to set a custom language for JSnap you need to create a file starting with lang_ the lang_ file must be next to the JSnap.jar file, after that JSnap will create a simple dialog makes you select the language you want to start JSnap with

This is what should ``lang_`` look from inside.<br>
`NOTE: You don't need to put all keys, keys that don't have value or deleted will stay to the default language (English)`

```
label_image_name=
label_current_directory=
label_thumbnails=
tooltip_thumbnails=
button_rename_image=
button_save=
button_save_all=
button_delete=
button_set_directory=
onclick_button_set_directory=
onerror_create_native_keyboard_hook=
label_current_name=
label_new_name=
button_rename=
button_cancel=
onerror_button_rename=
onsuccess_button_rename=
onsuccess_images_saved=
label_there_is_unsaved_images_warning=
button_save_images=
button_discard_images=
onsuccess_image_saved=
```

Supported language in the current moment is arabic, see [languages](https://github.com/QISnaith/JSnap-Multilang/tree/master/languages) folder.

Examples:
![Alt Text](https://github.com/QISnaith/JSnap-Multilang/blob/master/examples/JSnap-Multilang-test.gif)
