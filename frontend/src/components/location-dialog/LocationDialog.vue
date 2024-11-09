<template>
    <div class="location-dialog">
        <Dialog
            v-if="visible"
            v-model:visible="visible"
            :header="`${action} de localização`"
            :draggable="false"
            modal
            contentStyle="width: 400px">

            <!-- Name -->
            <div class="input">
                <label for="name">
                    *Name:
                </label>
                <InputText
                    id="name"
                    v-model="location.target.name"
                    placeholder="Digite o nome"
                    autofocus
                />
            </div>

            <!-- Address -->
            <div class="input">
                <label for="address">
                    *Tagline:
                </label>
                <InputText
                    id="address"
                    v-model="location.target.address"
                    placeholder="Digite o endereço"
                />
            </div>

            <!-- Actions -->
            <template #footer>
                <Button
                    label="Aplicar"
                    :disabled="!canApply"
                    @click="apply"
                />
                <Button
                    label="Cancelar"
                    class="p-button-secondary"
                    @click="close"
                />
            </template>
        </Dialog>
    </div>
</template>

<script>
import InputText from 'primevue/inputtext';
import Dialog from 'primevue/dialog';
import Button from 'primevue/button';
import _ from 'lodash';

export default {
    components: {
        InputText,
        Dialog,
        Button
    },
    data() {
        return {
            visible: false,
            callback: null,
            mode: null,
            location: {
                source: null,
                target: null
            }
        }
    },
    computed: {
        action() {
            if (this.mode === 'create') {
                return 'Criação';
            }
            return 'Edição';
        },
        canApply() {
            const requiredFields = [
                'name',
                'address'
            ];

            for (let requiredField of requiredFields) {
                if (_.isEmpty(this.location.target[requiredField])) {
                    return false;
                }
            }

            return true;
        }
    },
    methods: {
        open(mode, location, callback) {
            this.mode = mode;

            if (mode === 'create') {
                this.location.target = this.getDefault();
            } else {
                this.location.source = location;
                this.location.target = _.cloneDeep(location);
            }

            this.callback = callback;
            this.visible = true;
        },
        getDefault() {
            return {
                name: '',
                address: ''
            };
        },
        close() {
            this.visible = false;
        },
        apply() {
            this.callback(this.location.target);
            this.close();
        }
    }
};
</script>

<style lang="scss" scoped>
.input {
    display: flex;
    flex-direction: column;
    gap: 2px;
    margin-bottom: 1rem;

    & > label {
        font-weight: bold;
    }
}
</style>