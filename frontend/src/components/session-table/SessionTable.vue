<template>
    <div class="session-table">
        <DataTable :value="sessions" stripedRows>
            <!-- Empty -->
            <template #empty>
                Não há sessões a exibir.
            </template>

            <!-- Movie -->
            <Column
                field="movie.title"
                header="Filme"
                sortable
            />

            <!-- Localização -->
            <Column
                field="location.name"
                header="Localização"
                sortable
            />

            <!-- Date -->
            <Column
                field="date"
                header="Data"
                sortable>
                <template #body="slotProps">
                    {{ getFormattedDate(slotProps.data.date) }}
                </template>
            </Column>


            <!-- Hora -->
            <Column
                field="time"
                header="Hora"
                sortable>
                <template #body="slotProps">
                    {{ getFormattedTime(slotProps.data.time) }}
                </template>
            </Column>

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
    name: 'SessionTable',
    components: {
        DataTable,
        Column,
        Button
    },
    props: {
        sessions: {
            type: Array,
            required: true
        }
    },
    methods: {
        getFormattedDate(date) {
            const [ year, month, day ] = date.split('-');
            return `${day}/${month}/${year}`;
        },
        getFormattedTime(time) {
            const [ hour, minute ] = time.split(':');
            return `${hour}:${minute}`;
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