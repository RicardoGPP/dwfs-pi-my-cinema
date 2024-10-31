<template>
    <div class="sign-in-dialog">
        <Dialog
            header="Entrar"
            v-model:visible="visible"
            :draggable="false"
            modal>
            <div class="content">
                <div class="input">
                    <label for="email">
                        E-mail:
                    </label>
                    <InputText
                        id="email"
                        v-model="email"
                        autofocus
                    />
                </div>
                <div class="input">
                    <label for="password">
                        Senha:
                    </label>
                    <Password
                        id="password"
                        v-model="password"
                        :feedback="false"
                        toggleMask
                    />
                </div>
                <div v-if="error" class="error">
                    {{ error }}
                </div>
            </div>
            <template #footer>
                <Button
                    label="Entrar"
                    :disabled="!canSignIn"
                    @click="signIn"
                />
                <Button
                    label="Cancelar"
                    severity="secondary"
                    @click="close"
                />
            </template>
        </Dialog>
    </div>
</template>

<script>
import Dialog from 'primevue/dialog';
import InputText from 'primevue/inputtext';
import Password from 'primevue/password';
import Button from 'primevue/button';
import UserService from '@/services/user-service';

export default {
    name: 'SignInDialog',
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
            email: null,
            password: null,
            error: null
        }
    },
    computed: {
        canSignIn() {
            return this.isFilled(this.email)
                && this.isFilled(this.password);
        }
    },
    methods: {
        open(callback) {
            this.email = null;
            this.password = null;
            this.error = null;
            this.callback = callback;
            this.visible = true;
        },
        close() {
            this.visible = false;
            this.callback();
        },
        isFilled(value) {
            return value !== null
                && value !== undefined
                && value !== '';
        },
        async signIn() {
            const credentials = {
                email: this.email,
                password: this.password
            };

            let token = null;

            try {
                token = await UserService.login(credentials);
            } catch (error) {
                this.error = error.message;
                this.password = null;
                return;
            }

            localStorage.setItem('token', token);

            this.close();
        }
    }
};
</script>

<style lang="scss" scoped>
.content {
    display: flex;
    flex-direction: column;
    gap: 15px;
    width: 250px;
    margin-bottom: 10px;
}

.input {
    display: flex;
    flex-direction: column;
    gap: 5px;
}

label {
    font-weight: bold;
}

.error {
    color: #ff0000;
    margin-bottom: 1rem;
}

.actions {
    text-align: right;
}
</style>