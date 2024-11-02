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

            const onFillLocation = (filledLocation) => {
                createLocation(filledLocation)
                    .then(refreshTable);
            };

            this.$refs[this.dialog.location].open('create', null, onFillLocation);
        },
        update(location) {
            const updateLocation = (filledLocation) => {
                return LocationService.update(location.id, filledLocation);
            };

            const refreshTable = () => {
                this.doSignal();
            };

            const onFillLocation = (filledLocation) => {
                updateLocation(filledLocation)
                    .then(refreshTable);
            };

            this.$refs[this.dialog.location].open('edit', location, onFillLocation);
        },
        async remove(location) {
            try {
                await LocationService.delete(location.id);
                this.doSignal();
            } catch (error) {
                console.error(error.message);
            }
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