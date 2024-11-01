<template>
    <div class="loading-panel">
        <ProgressSpinner/>
        <span>
            Carregando...
        </span>
    </div>
</template>

<script>
import ProgressSpinner from 'primevue/progressspinner';

export default {
    name: 'LoadingPanel',
    components: {
        ProgressSpinner
    }
}
</script>

<style lang="scss" scoped>
.loading-panel {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    gap: 10px;
}
</style>