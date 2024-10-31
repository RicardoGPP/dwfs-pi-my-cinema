<template>
    <div class="sign-up-dialog">
        <Dialog
            header="Criar conta"
            v-model:visible="visible"
            :draggable="false"
            modal>
            <div class="content">
                <div class="input">
                    <label for="name">
                        Nome:
                    </label>
                    <InputText
                        id="name"
                        v-model="name"
                        autofocus
                    />
                </div>
                <div class="input">
                    <label for="email">
                        E-mail:
                    </label>
                    <InputText
                        id="email"
                        v-model="email"
                    />
                </div>
                <div class="input">
                    <label for="password">
                        Senha:
                    </label>
                    <Password
                        id="password"
                        v-model="password"
                        toggleMask
                    />
                </div>
                <div v-if="error" class="error">
                    {{ error }}
                </div>
            </div>
            <template #footer>
                <Button
                    label="Criar"
                    :disabled="!canSignUp"
                    @click="signUp"
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
    name: 'SignUpDialog',
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
            name: null,
            email: null,
            password: null,
            error: null
        }
    },
    computed: {
        canSignUp() {
            return this.isFilled(this.name)
                && this.isFilled(this.email)
                && this.isFilled(this.password);
        }
    },
    methods: {
        open(callback) {
            this.name = null;
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
        async signUp() {
            const user = {
                name: this.name,
                email: this.email,
                password: this.password
            };

            try {
                await UserService.create(user);
            } catch (error) {
                this.error = error.message;
                return;
            }

            const credentials = {
                email: this.email,
                password: this.password
            };

            let token = null;

            try {
                token = await UserService.login(credentials);
            } catch (error) {
                this.error = error.message;
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