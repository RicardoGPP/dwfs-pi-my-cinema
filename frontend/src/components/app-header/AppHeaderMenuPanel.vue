<template>
    <div class="app-header-menu-panel">
        <BeforeLoginPanel
            v-if="!loggedIn"
            @on-sign-up="checkUserStatus"
            @on-sign-in="checkUserStatus"
        />
        <AfterLoginPanel
            v-else
            :username="username"
            :admin="admin"
            @logout="logout"
        />
    </div>
</template>

<script>
import { jwtDecode } from 'jwt-decode';
import BeforeLoginPanel from './AppHeaderMenuPanelBeforeLoginPanel.vue';
import AfterLoginPanel from './AppHeaderMenuPanelAfterLoginPanel.vue';

export default {
    name: 'AppHeaderMenuPanel',
    components: {
        BeforeLoginPanel,
        AfterLoginPanel
    },
    data() {
        return {
            loggedIn: false,
            admin: false,
            username: ''
        }
    },
    methods: {
        checkUserStatus() {
            const token = localStorage.getItem('token');

            if (!token) {
                this.loggedIn = false;
                this.admin = false;
                return;
            }

            const decodedToken = jwtDecode(token);

            this.username = decodedToken.name || 'Usuário';
            this.admin = decodedToken.groups && decodedToken.groups.includes('ADMIN');
            this.loggedIn = true;
        },
        logout() {
            localStorage.removeItem('token');

            this.loggedIn = false;
            this.admin = false;
            this.username = '';

            this.$router.push('/');
        }
    },
    mounted() {
        this.checkUserStatus();
    }
}
</script>