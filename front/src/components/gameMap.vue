<template>
  <div @keypress.space="refresh" ref="parent" class="gameMap">
    <canvas ref="canvas" tabindex="0"></canvas>
  </div>
</template>

<script>
import { GameMap } from "@/assets/scripts/GameMap";
import { ref, onMounted } from "vue";
export default {
  setup() {
    let parent = ref(null);
    let canvas = ref(null);

    onMounted(() => {
      new GameMap(canvas.value.getContext("2d"), parent.value);
    });
    let refresh = () =>{
      new GameMap(canvas.value.getContext("2d"), parent.value);
 
    }
    return {
      parent,
      canvas
    ,refresh
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
