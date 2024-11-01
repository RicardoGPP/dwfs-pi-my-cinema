<template>
    <div class="error-panel">
        <span>
            {{ message }}
        </span>
        <Button
            v-if="retry"
            label="Tentar novamente"
            @click="retry"
        />
    </div>
</template>

<script>
import Button from 'primevue/button';

export default {
    name: 'ErrorPanel',
    components: {
        Button
    },
    props: {
        message: {
            type: String,
            required: true
        },
        retry: {
            type: Function,
            required: false
        }
    }
}
</script>

<style lang="scss" scoped>
.error-panel {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    gap: 10px;
}
</style>