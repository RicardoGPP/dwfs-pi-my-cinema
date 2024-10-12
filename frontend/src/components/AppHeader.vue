<template>
    <header class="header">
        <div class="left">
            <router-link to="/">
                MyCinema
            </router-link>
        </div>
        <div class="right">
            <template v-if="isLoggedIn">
                <Button
                    :label="`Olá, ${userName}`"
                    icon="pi pi-angle-down"
                    iconPos="right"
                    @click="toggleMenu"
                    class="text-button"
                />
                <Menu
                    ref="menu"
                    :model="menuItems"
                    popup
                />
            </template>
            <template v-else>
                <Button label="Entrar" icon="pi pi-sign-in" @click="openLoginDialog" />
            </template>
        </div>
        <LoginDialog ref="loginDialog"/>
    </header>
</template>

<script>
import { jwtDecode } from 'jwt-decode';
import Button from 'primevue/button';
import Menu from 'primevue/menu';
import LoginDialog from './LoginDialog.vue';

export default {
    components: {
        LoginDialog,
        Button,
        Menu
    },
    data() {
        return {
            isLoggedIn: false,
            isAdmin: false,
            userName: '',
            menuItems: []
        };
    },
    methods: {
        checkUserStatus() {
            const token = localStorage.getItem('token');

            if (!token) {
                this.isLoggedIn = false;
                this.isAdmin = false;
                return;
            }

            const decodedToken = jwtDecode(token);

            this.userName = decodedToken.name || 'Usuário';
            this.isAdmin = decodedToken.groups && decodedToken.groups.includes('ADMIN');
            this.isLoggedIn = true;

            this.setupMenu();
        },
        setupMenu() {
            this.menuItems = [
                {
                    label: 'Perfil',
                    icon: 'pi pi-user',
                    command: () => this.$router.push('/profile')
                },
                {
                    separator: true
                },
                ...this.isAdmin
                    ? [
                        {
                            label: 'Filmes',
                            icon: 'pi pi-video',
                            command: () => this.$router.push('/movies')
                        },
                        {
                            label: 'Localizações',
                            icon: 'pi pi-map-marker',
                            command: () => this.$router.push('/locations')
                        },
                        {
                            label: 'Sessões',
                            icon: 'pi pi-calendar',
                            command: () => this.$router.push('/sessions')
                        }
                    ]
                    : [],
                {
                    label: 'Logout',
                    icon: 'pi pi-sign-out',
                    command: this.logout
                }
            ];
        },
        toggleMenu(event) {
            this.$refs.menu.toggle(event);
        },
        openLoginDialog() {
            this.$refs.loginDialog.open(this.checkUserStatus);
        },
        logout() {
            localStorage.removeItem('token');

            this.isLoggedIn = false;
            this.isAdmin = false;
            this.userName = '';

            this.$router.push('/');
        }
    },
    created() {
        this.checkUserStatus();
    }
};
</script>

<style scoped>
.header {
    display: flex;
    justify-content: space-between;
    padding: 1rem;
    background-color: #333;
    color: white;
}

.header .left {
    font-size: 1.5rem;
    color: white;
}

.header .left a {
    color: white;
    text-decoration: none;
}

.header .right {
    position: relative;
}

.text-button {
    color: white;
    background-color: transparent;
    border: none;
    cursor: pointer;
}
</style>