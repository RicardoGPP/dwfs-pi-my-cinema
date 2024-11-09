<template>
    <div class="info-panel">
        <h2>
            {{ title }}
        </h2>
        <h5 v-if="subtitle">
            {{ subtitle }}
        </h5>
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
        subtitle: {
            type: String,
            required: false
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

h5 {
    margin: 0;
    margin-bottom: 15px;
    color: #a5a5a5;
    font-weight: normal;
}
</style>