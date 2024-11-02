<template>
    <header class="app-header">
        <LogoPanel/>
        <MenuPanel/>
    </header>
</template>

<script>
import LogoPanel from './AppHeaderLogoPanel.vue';
import MenuPanel from './AppHeaderMenuPanel.vue';

export default {
    name: 'AppHeader',
    components: {
        LogoPanel,
        MenuPanel
    }
}
</script>

<style lang="scss" scoped>
.app-header {
    display: flex;
    align-items: center;
    justify-content: space-between;
    width: 100%;
    height: 80px;
    padding: 0 20px;
    background: linear-gradient(to right, #0e0e0e, #333333);
    border-bottom: 3px solid #00FF7F;
}
</style>