<template>
    <div class="app-header-menu-panel-after-login-panel">
        <Button
            :label="`Olá, ${firstname}`"
            @click="toggleMenu"
        />
        <Menu
            ref="menu"
            :model="menuItems"
            popup
        />
    </div>
</template>

<script>
import Button from 'primevue/button';
import Menu from 'primevue/menu';

export default {
    name: 'AppHeaderMenuPanelAfterLoginPanel',
    components: {
        Button,
        Menu
    },
    props: {
        admin: {
            type: Boolean,
            required: true
        },
        username: {
            type: String,
            required: true
        }
    },
    computed: {
        firstname() {
            if (!this.username) {
                return null;
            }
            return this.username.split(' ')[0];
        }
    },
    data() {
        return {
            menuItems: [
                {
                    label: 'Perfil',
                    icon: 'pi pi-user',
                    command: () => this.$router.push('/profile')
                },
                {
                    separator: true
                },
                ...!this.admin ? [] : [
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
                ],
                {
                    label: 'Logout',
                    icon: 'pi pi-sign-out',
                    command: () => this.$emit('logout')
                }
            ]
        }
    },
    methods: {
        toggleMenu(event) {
            this.$refs.menu.toggle(event);
        }
    }
}
</script>