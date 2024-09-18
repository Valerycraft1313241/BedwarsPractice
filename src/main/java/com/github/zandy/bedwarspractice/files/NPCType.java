package com.github.zandy.bedwarspractice.files;

import com.github.zandy.bedwarspractice.files.language.Language;

public enum NPCType {
    DEFAULT(Language.MessagesEnum.NPC_HOLOGRAM_DEFAULT, Language.MessagesEnum.NPC_TYPE_DEFAULT),
    BRIDGING(Language.MessagesEnum.NPC_HOLOGRAM_BRIDGING, Language.MessagesEnum.NPC_TYPE_BRIDGING),
    MLG(Language.MessagesEnum.NPC_HOLOGRAM_MLG, Language.MessagesEnum.NPC_TYPE_MLG),
    FIREBALL_TNT_JUMPING(Language.MessagesEnum.NPC_HOLOGRAM_FIREBALL_TNT_JUMPING, Language.MessagesEnum.NPC_TYPE_FIREBALL_TNT_JUMPING);

    final Language.MessagesEnum lines;
    final Language.MessagesEnum type;

    NPCType(Language.MessagesEnum lines, Language.MessagesEnum type) {
        this.lines = lines;
        this.type = type;
    }

    public Language.MessagesEnum getLines() {
        return lines;
    }

    public Language.MessagesEnum getType() {
        return type;
    }
}
