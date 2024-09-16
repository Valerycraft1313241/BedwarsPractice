package com.github.zandy.bedwarspractice.files;

import com.github.zandy.bamboolib.utils.BambooFile;
import com.github.zandy.bedwarspractice.files.language.Language;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class NPCStorage extends BambooFile {
    private static NPCStorage instance = null;
    private final HashMap<NPCStorage.NPCType, List<Integer>> npcTypeIdList = new HashMap<>();

    private NPCStorage() {
        super("NPCs");
        Arrays.stream(NPCStorage.NPCType.values()).forEach((npcType) -> this.npcTypeIdList.put(npcType, null));
    }

    public static NPCStorage getInstance() {
        if (instance == null) {
            instance = new NPCStorage();
        }

        return instance;
    }

    public List<Integer> getIDList(NPCStorage.NPCType npcType) {
        List<Integer> idList = this.npcTypeIdList.get(npcType);
        if (idList != null) {
            return idList;
        } else {
            ArrayList<Integer> newIdList = new ArrayList<>();

            for (String idString : this.getStringList(npcType.getPath())) {
                newIdList.add(Integer.parseInt(idString));
            }

            this.npcTypeIdList.put(npcType, newIdList);
            return newIdList;
        }
    }

    public void add(NPCStorage.NPCType npcType, Integer id) {
        List<Integer> idList = this.getIDList(npcType);
        idList.add(id);
        this.npcTypeIdList.put(npcType, idList);
        this.saveToFile(npcType, idList);
    }

    public boolean contains(NPCStorage.NPCType npcType, Integer id) {
        return this.getIDList(npcType).contains(id);
    }

    public boolean isEmpty(NPCStorage.NPCType npcType) {
        return this.getIDList(npcType).isEmpty();
    }

    public NPCStorage.NPCType getNPCType(Integer id) {
        NPCStorage.NPCType[] npcTypes = NPCStorage.NPCType.values();

        for (NPCType npcType : npcTypes) {
            if (this.contains(npcType, id)) {
                return npcType;
            }
        }

        return null;
    }

    public void remove(NPCStorage.NPCType npcType, Integer id) {
        List<Integer> idList = this.getIDList(npcType);
        idList.remove(id);
        this.npcTypeIdList.put(npcType, idList);
        this.saveToFile(npcType, idList);
    }

    private void saveToFile(NPCStorage.NPCType npcType, List<Integer> idList) {
        ArrayList<String> idStringList = new ArrayList<>();
        idList.forEach((id) -> idStringList.add(String.valueOf(id)));
        this.set(npcType.getPath(), idStringList);
    }

    public enum NPCType {
        DEFAULT("IDs", Language.MessagesEnum.NPC_HOLOGRAM_DEFAULT, "[practice_player]", Language.MessagesEnum.NPC_TYPE_DEFAULT),
        BRIDGING("Bridging.IDs", Language.MessagesEnum.NPC_HOLOGRAM_BRIDGING, "[bridging_player]", Language.MessagesEnum.NPC_TYPE_BRIDGING),
        MLG("MLG.IDs", Language.MessagesEnum.NPC_HOLOGRAM_MLG, "[mlg_player]", Language.MessagesEnum.NPC_TYPE_MLG),
        FIREBALL_TNT_JUMPING("Fireball-TNT-Jumping.IDs", Language.MessagesEnum.NPC_HOLOGRAM_FIREBALL_TNT_JUMPING, "[fireball_tnt_jumping_player]", Language.MessagesEnum.NPC_TYPE_FIREBALL_TNT_JUMPING);

        final String path;
        final String placeholder;
        final Language.MessagesEnum messagesEnum;
        final Language.MessagesEnum type;

        NPCType(String path, Language.MessagesEnum messagesEnum, String placeholder, Language.MessagesEnum type) {
            this.path = path;
            this.messagesEnum = messagesEnum;
            this.placeholder = placeholder;
            this.type = type;
        }

        public String getPath() {
            return this.path;
        }

        public String getPlaceholder() {
            return this.placeholder;
        }

        public Language.MessagesEnum getMessagesEnum() {
            return this.messagesEnum;
        }

        public Language.MessagesEnum getType() {
            return this.type;
        }

    }
}
