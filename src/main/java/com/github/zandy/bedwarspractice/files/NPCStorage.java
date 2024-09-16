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
      Arrays.stream(NPCStorage.NPCType.values()).forEach((var1) -> this.npcTypeIdList.put(var1, null));
   }

   public List<Integer> getIDList(NPCStorage.NPCType var1) {
      List<Integer> var2 = this.npcTypeIdList.get(var1);
      if (var2 != null) {
         return var2;
      } else {
         ArrayList<Integer> var5 = new ArrayList<>();

          for (String var4 : this.getStringList(var1.getPath())) {
              var5.add(Integer.parseInt(var4));
          }

         this.npcTypeIdList.put(var1, var5);
         return var5;
      }
   }

   public void add(NPCStorage.NPCType var1, Integer var2) {
      List<Integer> var3 = this.getIDList(var1);
      var3.add(var2);
      this.npcTypeIdList.put(var1, var3);
      this.saveToFile(var1, var3);
   }

   public boolean contains(NPCStorage.NPCType var1, Integer var2) {
      return this.getIDList(var1).contains(var2);
   }

   public boolean isEmpty(NPCStorage.NPCType var1) {
      return this.getIDList(var1).isEmpty();
   }

   public NPCStorage.NPCType getNPCType(Integer var1) {
      NPCStorage.NPCType[] var2 = NPCStorage.NPCType.values();

       for (NPCType var5 : var2) {
           if (this.contains(var5, var1)) {
               return var5;
           }
       }

      return null;
   }

   public void remove(NPCStorage.NPCType var1, Integer var2) {
      List<Integer> var3 = this.getIDList(var1);
      var3.remove(var2);
      this.npcTypeIdList.put(var1, var3);
      this.saveToFile(var1, var3);
   }

   private void saveToFile(NPCStorage.NPCType var1, List<Integer> var2) {
      ArrayList<String> var3 = new ArrayList<>();
      var2.forEach((var1x) -> var3.add(String.valueOf(var1x)));
      this.set(var1.getPath(), var3);
   }

   public static NPCStorage getInstance() {
      if (instance == null) {
         instance = new NPCStorage();
      }

      return instance;
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

      NPCType(String var3, Language.MessagesEnum var4, String var5, Language.MessagesEnum var6) {
         this.path = var3;
         this.messagesEnum = var4;
         this.placeholder = var5;
         this.type = var6;
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
