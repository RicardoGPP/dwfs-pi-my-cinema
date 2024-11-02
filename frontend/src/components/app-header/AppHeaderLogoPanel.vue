<template>
    <div class="app-header-logo-panel">
        <router-link to="/">
            <div class="content">
                <img
                    src="/logo.webp"
                    height="40"
                />
                <div>
                    <span>My</span>
                    <span>Cinema</span>
                </div>
            </div>
        </router-link>
    </div>
</template>

<script>
export default {
    name: 'AppHeaderLogoPanel'
}
</script>

<style lang="scss" scoped>
.app-header-logo-panel a {
    color: #ffffff;
    text-decoration: none;
}

.content {
    display: flex;
    flex-direction: row;
    align-items: center;
    gap: 8px;
    font-size: 25px;
    color: #ffffff;
    font-weight: bold;

    &:hover * {
        filter: brightness(1.2);
        transition: 0.2s;
    }

    & > img {
        border-radius: 50px;
        border: 1px solid #00FF7F;
    }

    & span:first-child {
        color: #00FF7F;
    }
}
</style>