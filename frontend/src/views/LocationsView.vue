<template>
    <div class="locations-view">
        <h1>
            Cadastro de localizações
        </h1>
        <hr/>
        <LifecyclePanel :callback="load" :signal="signal">
            <div class="content">
                <div class="action">
                    <Button
                        label="Atualizar"
                        severity="secondary"
                        @click="doSignal"
                    />
                    <Button
                        label="Criar"
                        @click="create"
                    />
                </div>
                <LocationTable
                    :locations="locations"
                    @update="update"
                    @remove="remove"
                />
                <LocationDialog
                    :ref="dialog.location"
                />
            </div>
        </LifecyclePanel>
    </div>
</template>

<script>
import Button from 'primevue/button';
import LifecyclePanel from '@/components/lifecycle-panel/LifecyclePanel.vue';
import LocationTable from '@/components/location-table/LocationTable.vue';
import LocationDialog from '@/components/location-dialog/LocationDialog.vue';
import LocationService from '@/services/location-service';
import LifecycleSignalMixin from '@/mixins/lifecycle-signal-mixin';

export default {
    name: 'LocationsView',
    mixins: [
        LifecycleSignalMixin
    ],
    components: {
        Button,
        LifecyclePanel,
        LocationTable,
        LocationDialog
    },
    data() {
        return {
            locations: [],
            dialog: {
                location: 'LocationsView#LocationDialog'
            }
        }
    },
    methods: {
        async load() {
            this.locations = await LocationService.getAll();
        },
        create() {
            const createLocation = (filledLocation) => {
                return LocationService.create(filledLocation);
            };

            const refreshTable = () => {
                this.doSignal();
            };

            const showSuccessMessage = () => {
                this.$toast.add({
                    severity: 'success',
                    summary: 'Successo',
                    detail: 'A localização foi criada com sucesso!',
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

            const onAccept = (filledLocation) => {
                this.$loading.wrap(
                    createLocation(filledLocation)
                        .then(refreshTable)
                        .then(showSuccessMessage)
                        .catch(showErrorMessage)
                );
            };

            const showConfirm = (filledLocation) => {
                this.$confirm.require({
                    header: 'Confirmação',
                    message: 'Deseja realmente criar a localização?',
                    acceptProps: {
                        label: 'Sim'
                    },
                    rejectProps: {
                        label: 'Não',
                        severity: 'secondary'
                    },
                    accept: () => onAccept(filledLocation)
                });
            };

            this.$refs[this.dialog.location].open('create', null, showConfirm);
        },
        update(location) {
            const updateLocation = (filledLocation) => {
                return LocationService.update(location.id, filledLocation);
            };

            const refreshTable = () => {
                this.doSignal();
            };

            const showSuccessMessage = () => {
                this.$toast.add({
                    severity: 'success',
                    summary: 'Successo',
                    detail: 'A localização foi editada com sucesso!',
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

            const onAccept = (filledLocation) => {
                this.$loading.wrap(
                    updateLocation(filledLocation)
                        .then(refreshTable)
                        .then(showSuccessMessage)
                        .catch(showErrorMessage)
                );
            };

            const showConfirm = (filledLocation) => {
                this.$confirm.require({
                    header: 'Confirmação',
                    message: 'Deseja realmente editar a localização?',
                    acceptProps: {
                        label: 'Sim'
                    },
                    rejectProps: {
                        label: 'Não',
                        severity: 'secondary'
                    },
                    accept: () => onAccept(filledLocation)
                });
            };

            this.$refs[this.dialog.location].open('edit', location, showConfirm);
        },
        remove(location) {
            const removeLocation = () => {
                return LocationService.delete(location.id);
            };

            const refreshTable = () => {
                this.doSignal();
            };

            const showSuccessMessage = () => {
                this.$toast.add({
                    severity: 'success',
                    summary: 'Successo',
                    detail: 'A localização foi excluída com sucesso!',
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

            const onAccept = () => {
                this.$loading.wrap(
                    removeLocation()
                        .then(refreshTable)
                        .then(showSuccessMessage)
                        .catch(showErrorMessage)
                );
            };

            this.$confirm.require({
                header: 'Confirmação',
                message: 'Deseja realmente excluir a localização?',
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
    },
    mounted() {
        this.load();
    }
};
</script>

<style lang="scss" scoped>
.locations-view {
    padding: 20px;
}

.content {
    display: flex;
    flex-direction: column;
    gap: 10px;
}

.action {
    display: flex;
    flex-direction: row;
    justify-content: flex-end;
    gap: 5px;
}
</style>