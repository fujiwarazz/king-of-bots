<template>
    <div class="playground">
        <gameMap/>
    </div>
</template>

<script>
import gameMap from './gameMap.vue'
export default {
    components:{
        gameMap
    },
    setup () {
        

        return {}
    }
}
</script>

<style  scoped>

div.playground{
    width: 50vw;
    height: 70vh;
    margin: 0 auto;
    margin-top: 40px;
 }
</style>