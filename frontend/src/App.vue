<template>
    <AppHeader/>
    <router-view/>
</template>

<script>
import AppHeader from '@/components/AppHeader.vue';

export default {
    name: 'App',
    components: {
        AppHeader
    }
}
</script>

<style>
#app {
    font-family: Avenir, Helvetica, Arial, sans-serif;
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
}
</style>