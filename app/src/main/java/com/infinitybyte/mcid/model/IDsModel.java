package com.infinitybyte.mcid.model;

public class IDsModel {
    String item_image, item_name, item_stroke_id, item_number_id;

    public IDsModel() {
        //
    }

    public IDsModel(String item_image, String item_name, String item_stroke_id, String item_number_id) {
        this.item_image = item_image;
        this.item_name = item_name;
        this.item_stroke_id = item_stroke_id;
        this.item_number_id = item_number_id;
    }

    public String getItem_image() {
        return item_image;
    }

    public void setItem_image(String item_image) {
        this.item_image = item_image;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getItem_stroke_id() {
        return item_stroke_id;
    }

    public void setItem_stroke_id(String item_stroke_id) {
        this.item_stroke_id = item_stroke_id;
    }

    public String getItem_number_id() {
        return item_number_id;
    }

    public void setItem_number_id(String item_number_id) {
        this.item_number_id = item_number_id;
    }
}
