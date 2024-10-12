<template>
    <Dialog
        header="Login"
        v-model:visible="visible"
        modal>
        <div class="form-group">
            <label for="email">
                Email:
            </label>
            <InputText
                id="email"
                v-model="email"
            />
        </div>
        <div class="form-group">
            <label for="password">
                Senha:
            </label>
            <Password
                id="password"
                v-model="password"
                toggleMask
            />
        </div>
        <div v-if="errorMessage" class="error">
            {{ errorMessage }}
        </div>
        <template #footer>
            <Button
                label="Entrar"
                icon="pi pi-sign-in"
                @click="login"
            />
        </template>
    </Dialog>
</template>

<script>
import Dialog from 'primevue/dialog';
import InputText from 'primevue/inputtext';
import Password from 'primevue/password';
import Button from 'primevue/button';
import userService from '@/services/user-service';

export default {
    components: {
        Dialog,
        InputText,
        Password,
        Button
    },
    data() {
        return {
            visible: false,
            callback: null,
            email: '',
            password: '',
            errorMessage: ''
        };
    },
    methods: {
        open(callback) {
            this.visible = true;
            this.callback = callback;
        },
        close() {
            this.visible = false;
            this.callback();
            this.clearForm();
        },
        clearForm() {
            this.email = '';
            this.password = '';
            this.errorMessage = '';
        },
        async login() {
            try {
                const credentials = {
                    email: this.email,
                    password: this.password
                };

                const token = await userService.login(credentials);

                if (token) {
                    localStorage.setItem('token', token);
                    this.close();
                }
            } catch (error) {
                this.errorMessage = 'Email ou senha inválidos.';
            }
        }
    }
};
</script>

<style scoped>
.form-group {
    display: flex;
    flex-direction: column;
    gap: 2px;
    margin-bottom: 1rem;
}

.error {
    color: red;
    margin-bottom: 1rem;
}

.actions {
    text-align: right;
}
</style>