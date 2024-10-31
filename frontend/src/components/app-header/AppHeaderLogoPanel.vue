<template>
    <div class="app-header-logo-panel">
        <router-link to="/">
            <span>MY</span>
            <span>CINEMA</span>
        </router-link>
    </div>
</template>

<script>
export default {
    name: 'AppHeaderLogoPanel'
}
</script>

<style lang="scss" scoped>
.app-header-logo-panel {
    font-size: 30px;
    color: #ffffff;
    font-weight: bold;

    & span:first-child {
        color: #00FF7F;
    }

    & a {
        color: #ffffff;
        text-decoration: none;
    }
}
</style>