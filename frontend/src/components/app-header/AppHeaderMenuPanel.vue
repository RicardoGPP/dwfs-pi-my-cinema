<template>
    <div class="app-header-menu-panel">
        <BeforeLoginPanel v-if="!user"/>
        <AfterLoginPanel v-else/>
    </div>
</template>

<script>
import BeforeLoginPanel from './AppHeaderMenuPanelBeforeLoginPanel.vue';
import AfterLoginPanel from './AppHeaderMenuPanelAfterLoginPanel.vue';
import AuthMixin from '@/mixins/auth-mixin';

export default {
    name: 'AppHeaderMenuPanel',
    mixins: [
        AuthMixin
    ],
    components: {
        BeforeLoginPanel,
        AfterLoginPanel
    }
}
</script>