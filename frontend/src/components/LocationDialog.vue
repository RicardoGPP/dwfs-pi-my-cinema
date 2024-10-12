<template>
    <Dialog
        class="location-dialog"
        header="Localização"
        v-model:visible="dialogVisible"
        modal
        :closable="false">
        <div class="form-group">
            <label for="name">
                Nome:
            </label>
            <InputText
                v-model="location.name"
                id="name"
                placeholder="Digite o nome da localização"
            />
        </div>
        <div class="form-group">
            <label for="address">
                Endereço:
            </label>
            <InputText
                v-model="location.address"
                id="address"
                placeholder="Digite o endereço da localização"
            />
        </div>
        <template #footer>
            <Button
                label="Salvar"
                icon="pi pi-check"
                @click="saveLocation"
            />
            <Button
                label="Cancelar"
                icon="pi pi-times"
                class="p-button-secondary"
                @click="closeDialog"
            />
        </template>
    </Dialog>
</template>

<script>
import InputText from 'primevue/inputtext';
import Dialog from 'primevue/dialog';
import Button from 'primevue/button';

export default {
    components: {
        InputText,
        Dialog,
        Button
    },
    data() {
        return {
            dialogVisible: false,
            location: {
                name: '',
                address: ''
            }
        };
    },
    methods: {
        openDialog(location = null) {
            if (location) {
                this.location.name = location.name;
                this.location.address = location.address;
            } else {
                this.location.name = '';
                this.location.address = '';
            }
            this.dialogVisible = true;
        },
        saveLocation() {
            this.$emit('save', { ...this.location });
            this.dialogVisible = false;
        },
        closeDialog() {
            this.dialogVisible = false;
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
</style>