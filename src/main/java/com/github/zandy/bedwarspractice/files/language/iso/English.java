package com.github.zandy.bedwarspractice.files.language.iso;

import com.github.zandy.bamboolib.utils.BambooFile;
import com.github.zandy.bedwarspractice.files.language.Language;

import java.util.Arrays;

public class English extends BambooFile {
    public English() {
        super("Language_EN", "Languages");
        Arrays.stream(Language.MessagesEnum.values()).forEach((message) -> {
            this.addDefault(message.getPath(), message.getDefaultValue());
        });
        this.copyDefaults();
        this.save();
    }
}
