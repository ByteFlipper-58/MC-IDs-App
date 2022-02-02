package com.infinitybyte.mcid.config;

public class Settings {
    @Parameter(jsonKey = "content_language")
    public static String contentLanguage = "en";

    @Parameter(jsonKey = "sorting_type")
    public static String sortingType = "default";

    @Parameter(jsonKey = "id_category")
    public static String idCategory = "items";
}
