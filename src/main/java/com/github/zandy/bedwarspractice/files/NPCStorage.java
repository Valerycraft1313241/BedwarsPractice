package com.github.zandy.bedwarspractice.files;

import com.github.zandy.bamboolib.utils.BambooFile;
import com.github.zandy.bedwarspractice.files.language.Language;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public class NPCStorage extends BambooFile {
    private static final NPCStorage INSTANCE = new NPCStorage();
   private final ConcurrentHashMap<NPCType, List<Integer>> npcTypeIdList = new ConcurrentHashMap<>();

   private NPCStorage() {
      super("NPCs");
      Arrays.stream(NPCType.values()).forEach(type -> this.npcTypeIdList.put(type, null));
   }

   public static NPCStorage getInstance() {
      return INSTANCE;
   }

   public List<Integer> getIDList(NPCType type) {
      return npcTypeIdList.computeIfAbsent(type, t -> {
         List<Integer> ids = new ArrayList<>();
         for (String id : this.getStringList(t.getPath())) {
            ids.add(Integer.parseInt(id));
         }
         return ids;
      });
   }

   public void add(NPCType type, Integer id) {
      List<Integer> ids = this.getIDList(type);
      ids.add(id);
      this.npcTypeIdList.put(type, ids);
      this.saveToFile(type, ids);
   }

   public boolean contains(NPCType type, Integer id) {
      return this.getIDList(type).contains(id);
   }

   public boolean isEmpty(NPCType type) {
      return this.getIDList(type).isEmpty();
   }

   public NPCType getNPCType(Integer id) {
      return Arrays.stream(NPCType.values())
              .filter(type -> this.contains(type, id))
              .findFirst()
              .orElse(null);
   }

   public void remove(NPCType type, Integer id) {
      List<Integer> ids = this.getIDList(type);
      ids.remove(id);
      this.npcTypeIdList.put(type, ids);
      this.saveToFile(type, ids);
   }

   private void saveToFile(NPCType type, List<Integer> ids) {
      List<String> idStrings = new ArrayList<>();
      ids.forEach(id -> idStrings.add(String.valueOf(id)));
      this.set(type.getPath(), idStrings);
   }

   public enum NPCType {
      DEFAULT("IDs", Language.MessagesEnum.NPC_HOLOGRAM_DEFAULT, "[practice_player]", Language.MessagesEnum.NPC_TYPE_DEFAULT),
      BRIDGING("Bridging.IDs", Language.MessagesEnum.NPC_HOLOGRAM_BRIDGING, "[bridging_player]", Language.MessagesEnum.NPC_TYPE_BRIDGING),
      MLG("MLG.IDs", Language.MessagesEnum.NPC_HOLOGRAM_MLG, "[mlg_player]", Language.MessagesEnum.NPC_TYPE_MLG),
      FIREBALL_TNT_JUMPING("Fireball-TNT-Jumping.IDs", Language.MessagesEnum.NPC_HOLOGRAM_FIREBALL_TNT_JUMPING, "[fireball_tnt_jumping_player]", Language.MessagesEnum.NPC_TYPE_FIREBALL_TNT_JUMPING);

      private final String path;
      private final String placeholder;
      private final Language.MessagesEnum messagesEnum;
      private final Language.MessagesEnum type;

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
