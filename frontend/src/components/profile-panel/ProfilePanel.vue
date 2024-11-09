<template>
    <div class="profile-panel">
        <div class="form">
            <div class="input">
                <label for="name">
                    Nome:
                </label>
                <InputText
                    id="name"
                    v-model="newUser.name"
                    autofocus
                />
            </div>
            <div class="input">
                <label for="email">
                    E-mail:
                </label>
                <InputText
                    id="email"
                    :disabled="true"
                    :modelValue="user.email"
                />
                <span class="message">
                    O e-mail não pode ser editado.
                </span>
            </div>
            <div class="input">
                <label for="password">
                    Senha:
                </label>
                <Password
                    id="password"
                    v-model="newUser.password"
                    toggleMask
                    placeholder="Sem alteração"
                />
                <span class="message">
                    Para alterar a senha, preencha uma nova.
                </span>
            </div>
        </div>
        <div class="action">
            <Button
                label="Atualizar"
                outlined
                @click="$emit('update', newUser)"
            />
            <Button
                label="Excluir conta"
                severity="danger"
                @click="$emit('remove')"
            />
        </div>
    </div>
</template>

<script>
import InputText from 'primevue/inputtext';
import Password from 'primevue/password';
import Button from 'primevue/button';

export default {
    name: 'ProfilePanel',
    components: {
        InputText,
        Password,
        Button
    },
    props: {
        user: {
            type: Object,
            required: true
        }
    },
    data() {
        return {
            newUser: {
                name: null,
                password: null
            }
        }
    },
    mounted() {
        this.newUser = {
            name: this.user.name,
            password: null
        };
    }
}
</script>

<style lang="scss" scoped>
.profile-panel {
    display: flex;
    flex-direction: column;
    gap: 30px;
    width: 300px;
    border: 1px solid #565656;
    border-radius: 5px;
    padding: 20px;
}

.form {
    display: flex;
    flex-direction: column;
    gap: 15px;
}

.input {
    display: flex;
    flex-direction: column;
    gap: 5px;
}

.message {
    color: #808080;
    font-size: 12px;
    text-align: right;
}

.action {
    display: flex;
    flex-direction: row;
    gap: 5px;
    justify-content: flex-end;
}

label {
    font-weight: bold;
}
</style>