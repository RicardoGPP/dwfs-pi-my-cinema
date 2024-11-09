<template>
    <AppHeader/>
    <div class="global-content">
        <router-view/>
    </div>
    <ConfirmDialog/>
    <LoadingDialog/>
    <Toast/>
</template>

<script>
import AppHeader from '@/components/app-header/AppHeader.vue';
import ConfirmDialog from 'primevue/confirmdialog';
import LoadingDialog from '@/components/loading-dialog/LoadingDialog.vue';
import Toast from 'primevue/toast';

export default {
    name: 'App',
    components: {
        AppHeader,
        ConfirmDialog,
        LoadingDialog,
        Toast
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

header {
    position: fixed;
    z-index: 99;
}

.global-content {
    position: absolute;
    width: 100%;
    margin-top: 80px;
}
</style>