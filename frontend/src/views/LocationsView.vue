<template>
    <div class="locations-view">
        <h1>Localizações</h1>
        <Button
            label="Criar"
            icon="pi pi-plus"
            @click="openNewLocationDialog"
        />
        <DataTable :value="locations">
            <Column
                field="name"
                header="Nome"
                sortable
            />
            <Column
                field="address"
                header="Endereço"
                sortable
            />
            <Column header="Ações">
                <template #body="slotProps">
                    <Button
                        label="Editar"
                        icon="pi pi-pencil"
                        class="p-button-text"
                        @click="editLocation(slotProps.data)"
                    />
                    <Button
                        label="Excluir"
                        icon="pi pi-trash"
                        class="p-button-text"
                        @click="confirmDeleteLocation(slotProps.data)"
                    />
                </template>
            </Column>
        </DataTable>
        <LocationDialog
            ref="locationDialog"
            @save="saveLocation"
        />
        <Dialog
            header="Confirmar"
            v-model:visible="deleteDialogVisible"
            modal>
            <p>
                Você tem certeza que deseja excluir esta localização?
            </p>
            <Button
                label="Sim"
                icon="pi pi-check"
                @click="deleteLocation"
            />
            <Button
                label="Não"
                icon="pi pi-times"
                class="p-button-secondary"
                @click="hideDeleteDialog"
            />
        </Dialog>
    </div>
</template>

<script>
import DataTable from 'primevue/datatable';
import Column from 'primevue/column';
import Button from 'primevue/button';
import Dialog from 'primevue/dialog';
import locationService from '@/services/location-service';
import LocationDialog from '@/components/LocationDialog.vue';

export default {
    components: {
        DataTable,
        Column,
        Button,
        Dialog,
        LocationDialog
    },
    data() {
        return {
            locations: [],
            deleteDialogVisible: false,
            selectedLocation: null
        };
    },
    methods: {
        async loadLocations() {
            this.locations = await locationService.getAll();
        },
        openNewLocationDialog() {
            this.selectedLocation = null;
            this.$refs.locationDialog.openDialog();
        },
        editLocation(location) {
            this.selectedLocation = location;
            this.$refs.locationDialog.openDialog(location);
        },
        async saveLocation(location) {
            if (this.selectedLocation) {
                await locationService.update(this.selectedLocation.id, location);
            } else {
                await locationService.create(location);
            }
            this.loadLocations();
        },
        confirmDeleteLocation(location) {
            this.selectedLocation = location;
            this.deleteDialogVisible = true;
        },
        async deleteLocation() {
            await locationService.delete(this.selectedLocation.id);
            this.loadLocations();
            this.deleteDialogVisible = false;
        },
        hideDeleteDialog() {
            this.deleteDialogVisible = false;
        }
    },
    mounted() {
        this.loadLocations();
    }
};
</script>