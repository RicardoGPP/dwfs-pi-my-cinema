<template>
    <div class="profile-view">
        <ProfilePanel
            :user="user"
            @update="update"
            @remove="remove"
        />
    </div>
</template>

<script>
import ProfilePanel from '@/components/profile-panel/ProfilePanel.vue';
import AuthMixin from '@/mixins/auth-mixin';
import LifecycleSignalMixin from '@/mixins/lifecycle-signal-mixin';
import UserService from '@/services/user-service';

export default {
    name: 'ProfileView',
    mixins: [
        AuthMixin,
        LifecycleSignalMixin
    ],
    components: {
        ProfilePanel
    },
    methods: {
        update(newUser) {
            const updateUser = () => {
                return UserService.update(this.user.id, newUser);
            };

            const setUserName = () => {
                this.setUserName(newUser.name);
            };

            const showSuccessMessage = () => {
                this.$toast.add({
                    severity: 'success',
                    summary: 'Successo',
                    detail: 'Os dados foram atualizados com sucesso!',
                    life: 3000
                });
            };

            const showErrorMessage = (error) => {
                this.$toast.add({
                    severity: 'error',
                    summary: 'Erro',
                    detail: error.message,
                    life: 3000
                });
            };

            const wait = () => {
                return new Promise((resolve) => {
                    setTimeout(() => {
                        resolve();
                    }, 1000);
                })
            };

            const onAccept = () => {
                this.$loading.wrap(
                    updateUser()
                        .then(setUserName)
                        .then(showSuccessMessage)
                        .then(wait)
                        .then(this.refreshPage)
                        .catch(showErrorMessage)
                );
            };

            this.$confirm.require({
                header: 'Confirmação',
                message: 'Deseja realmente atualizar seus dados?',
                acceptProps: {
                    label: 'Sim'
                },
                rejectProps: {
                    label: 'Não',
                    severity: 'secondary'
                },
                accept: onAccept
            });
        },
        remove() {
            const removeUser = () => {
                return UserService.delete(this.user.id);
            };

            const showSuccessMessage = () => {
                this.$toast.add({
                    severity: 'success',
                    summary: 'Successo',
                    detail: 'A conta foi excluída com sucesso!',
                    life: 3000
                });
            };

            const showErrorMessage = (error) => {
                this.$toast.add({
                    severity: 'error',
                    summary: 'Erro',
                    detail: error.message,
                    life: 3000
                });
            };

            const wait = () => {
                return new Promise((resolve) => {
                    setTimeout(() => {
                        resolve();
                    }, 1000);
                })
            };

            const onAccept = () => {
                this.$loading.wrap(
                    removeUser()
                        .then(showSuccessMessage)
                        .then(wait)
                        .then(this.doLogout)
                        .catch(showErrorMessage)
                );
            };

            this.$confirm.require({
                header: 'Confirmação',
                message: 'Deseja realmente excluir sua conta?',
                acceptProps: {
                    label: 'Sim',
                    severity: 'danger'
                },
                rejectProps: {
                    label: 'Não',
                    severity: 'secondary'
                },
                accept: onAccept
            });
        }
    }
}
</script>

<style lang="scss" scoped>
.profile-view {
    display: flex;
    align-items: center;
    justify-content: center;
    padding-top: 20px;
}
</style>