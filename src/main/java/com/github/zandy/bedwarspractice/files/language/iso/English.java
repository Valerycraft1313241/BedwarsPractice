package com.github.zandy.bedwarspractice.files.language.iso;

import com.github.zandy.bamboolib.utils.BambooFile;
import com.github.zandy.bedwarspractice.files.language.Language;
import java.util.Arrays;

public class English extends BambooFile {
   public English() {
      super("Language_EN", "Languages");
      Arrays.stream(Language.MessagesEnum.values()).forEach((var1) -> {
         this.addDefault(var1.getPath(), var1.getDefaultValue());
      });
      this.copyDefaults();
      this.save();
   }
}
