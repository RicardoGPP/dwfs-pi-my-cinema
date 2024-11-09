<template>
    <div class="not-found-view">
        <ErrorPanel message="Página não encontrada."/>
    </div>
</template>

<script>
import ErrorPanel from '@/components/error-panel/ErrorPanel.vue';

export default {
    name: 'NotFoundView',
    components: {
        ErrorPanel
    }
}
</script>

<style lang="scss" scoped>
.not-found-view {
    display: flex;
    align-items: center;
    justify-content: center;
    padding: 30px;
}
</style>