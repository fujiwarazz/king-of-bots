<template>
  <div @keypress.space="refresh" ref="parent" class="gameMap">
    <canvas ref="canvas" tabindex="0"></canvas>
  </div>
</template>

<script>
import { GameMap } from "@/assets/scripts/GameMap";
import { ref, onMounted } from "vue";
import { useStore } from "vuex";



export default {
  setup() {
    let parent = ref(null);
    let canvas = ref(null);
    const store = useStore();
    onMounted(() => {
      store.commit("updateGameObject",new GameMap(canvas.value.getContext("2d"), parent.value,store))
    });
   
    return {
      parent,
      canvas
    };
  },
};
</script>

<style scoped>
.gameMap {
  height: 100%;
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
}
</style>
