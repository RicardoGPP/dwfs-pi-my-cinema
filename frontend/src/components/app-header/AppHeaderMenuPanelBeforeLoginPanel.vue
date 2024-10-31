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
        <SignUpDialog ref="signUpDialog"/>
        <SignInDialog ref="signInDialog"/>
    </div>
</template>

<script>
import Button from 'primevue/button';
import SignUpDialog from './../sign-up-dialog/SignUpDialog.vue';
import SignInDialog from './../sign-in-dialog/SignInDialog.vue';

export default {
    name: 'AppHeaderMenuPanelBeforeLoginPanel',
    components: {
        Button,
        SignUpDialog,
        SignInDialog
    },
    methods: {
        openSignUpDialog() {
            this.$refs.signUpDialog.open(() => this.$emit('on-sign-up'));
        },
        openSignInDialog() {
            this.$refs.signInDialog.open(() => this.$emit('on-sign-in'));
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