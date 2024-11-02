<template>
    <div class="app-header-menu-panel-after-login-panel">
        <Button
            :label="`Olá, ${firstname}`"
            @click="toggleMenu"
        />
        <Menu
            ref="menu"
            :model="menuItems"
            bazeZIndex="999"
            popup
        />
    </div>
</template>

<script>
import Button from 'primevue/button';
import Menu from 'primevue/menu';
import AuthMixin from '@/mixins/auth-mixin';

export default {
    name: 'AppHeaderMenuPanelAfterLoginPanel',
    mixins: [
        AuthMixin
    ],
    components: {
        Button,
        Menu
    },
    computed: {
        firstname() {
            if (!this.user) {
                return null;
            }

            const name = this.user.name;

            if (!name) {
                return null;
            }

            return name.split(' ')[0];
        },
        menuItems() {
            return this.getMenuItems();
        }
    },
    methods: {
        toggleMenu(event) {
            this.$refs.menu.toggle(event);
        },
        getMenuItems() {
            return [
                {
                    label: 'Perfil',
                    icon: 'pi pi-user',
                    command: () => this.$router.push('/profile')
                },
                {
                    separator: true
                },
                ...this.getAdminMenuItems(),
                {
                    label: 'Sair',
                    icon: 'pi pi-sign-out',
                    command: () => this.doLogout()
                }
            ];
        },
        getAdminMenuItems() {
            const admin = (this.user || { admin: false }).admin;

            if (!admin) {
                return [];
            }

            return [
                {
                    label: 'Cadastro de filmes',
                    icon: 'pi pi-video',
                    command: () => this.$router.push('/movies')
                },
                {
                    label: 'Cadastro de localizações',
                    icon: 'pi pi-map-marker',
                    command: () => this.$router.push('/locations')
                },
                {
                    label: 'Cadastro de sessões',
                    icon: 'pi pi-calendar',
                    command: () => this.$router.push('/sessions')
                },
                {
                    separator: true
                }
            ];
        }
    }
}
</script>