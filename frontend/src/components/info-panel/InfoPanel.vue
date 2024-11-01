<template>
    <div class="info-panel">
        <h2>
            {{ title }}
        </h2>
        <span>
            {{ message }}
        </span>
    </div>
</template>

<script>
export default {
    name: 'InfoPanel',
    props: {
        title: {
            type: String,
            required: true
        },
        message: {
            type: String,
            required: true
        }
    }
}
</script>

<style lang="scss" scoped>
.info-panel {
    display: flex;
    flex-direction: column;
    gap: 10px;
    padding: 15px;
    border: 1px solid #353535;
    border-left: 5px solid #00FF7F;
    background: linear-gradient(to bottom, #353535, #222222);
    color: white !important;
    border-radius: 5px;
    color: #000000;
}

h2 {
    margin: 0;
}
</style>