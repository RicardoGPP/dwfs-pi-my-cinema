<template>
    <div class="app-header-menu-panel-before-login-panel">
        <div class="action">
            <Button
                label="Criar conta"
                severity="secondary"
                @click="openSignUpDialog"
            />
            <Button
                label="Entrar"
                @click="openSignInDialog"
            />
        </div>
        <SignUpDialog :ref="dialog.signUp"/>
        <SignInDialog :ref="dialog.signIn"/>
    </div>
</template>

<script>
import Button from 'primevue/button';
import SignUpDialog from '@/components/sign-up-dialog/SignUpDialog.vue';
import SignInDialog from '@/components/sign-in-dialog/SignInDialog.vue';

export default {
    name: 'AppHeaderMenuPanelBeforeLoginPanel',
    components: {
        Button,
        SignUpDialog,
        SignInDialog
    },
    data() {
        return {
            dialog: {
                signUp: 'AppHeaderMenuPanelBeforeLoginPanel#SignUpDialog',
                signIn: 'AppHeaderMenuPanelBeforeLoginPanel#SignInDialog'
            }
        }
    },
    methods: {
        openSignUpDialog() {
            this.$refs[this.dialog.signUp].open(() => null);
        },
        openSignInDialog() {
            this.$refs[this.dialog.signIn].open(() => null);
        }
    }
}
</script>

<style lang="scss" scoped>
.action {
    display: flex;
    justify-content: flex-end;
    gap: 10px;
}
</style>