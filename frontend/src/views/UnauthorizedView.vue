<template>
    <div class="unauthorized-view">
        <ErrorPanel message="Acesso não autorizado."/>
    </div>
</template>

<script>
import ErrorPanel from '@/components/error-panel/ErrorPanel.vue';

export default {
    name: 'UnauthorizedView',
    components: {
        ErrorPanel
    }
}
</script>

<style lang="scss" scoped>
.unauthorized-view {
    display: flex;
    align-items: center;
    justify-content: center;
    padding: 30px;
}
</style>