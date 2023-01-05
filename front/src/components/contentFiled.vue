<template>
     <div class="container content">
        <div class="card">
            <div class="card-body">
                <slot>
                    
                </slot>
            </div>
        </div>
     </div>
</template>

<script>
export default {
    setup () {
        

        return {}
    }
}
</script>

<style scoped>
.content{
    margin-top: 30px;
}
</style>