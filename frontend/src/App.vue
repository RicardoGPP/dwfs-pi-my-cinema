<template>
    <AppHeader/>
    <div class="global-content">
        <router-view/>
    </div>
</template>

<script>
import AppHeader from '@/components/app-header/AppHeader.vue';

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

html, body {
    margin: 0;
    padding: 0;
}

.global-content {
    padding: 20px;
}
</style>