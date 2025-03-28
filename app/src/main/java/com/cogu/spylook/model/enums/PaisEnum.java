package com.cogu.spylook.model.enums;

import com.cogu.spylook.R;

import lombok.Getter;

public enum PaisEnum {
    ESP("España", R.drawable.esp);
    @Getter
    private final String string;
    @Getter
    private final int image;

    private PaisEnum(String string, int image) {
        this.string = string;
        this.image = image;
    }


}
