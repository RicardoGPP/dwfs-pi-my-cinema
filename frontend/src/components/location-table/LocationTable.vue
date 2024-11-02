<template>
    <div class="location-table">
        <DataTable :value="locations">
            <!-- Empty -->
            <template #empty>
                Não há localizações a exibir.
            </template>

            <!-- Name -->
            <Column
                field="name"
                header="Nome"
                sortable
            />

            <!-- Address -->
            <Column
                field="address"
                header="Endereço"
                sortable
            />

            <!-- Ações -->
            <Column header="Ações" headerStyle="width: 180px">
                <template #body="slotProps">
                    <div class="action">
                        <Button
                            label="Editar"
                            severity="secondary"
                            @click="$emit('update', slotProps.data)"
                        />
                        <Button
                            label="Excluir"
                            severity="danger"
                            @click="$emit('remove', slotProps.data)"
                        />
                    </div>
                </template>
            </Column>
        </DataTable>
    </div>
</template>

<script>
import DataTable from 'primevue/datatable';
import Column from 'primevue/column';
import Button from 'primevue/button';

export default {
    name: 'LocationTable',
    components: {
        DataTable,
        Column,
        Button
    },
    props: {
        locations: {
            type: Array,
            required: true
        }
    }
}
</script>

<style lang="scss" scoped>
.action {
    display: flex;
    flex-direction: row;
    gap: 5px;
}
</style>