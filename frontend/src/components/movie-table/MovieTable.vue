<template>
    <div class="movie-table">
        <DataTable :value="movies">
            <!-- Empty -->
            <template #empty>
                Não há filmes a exibir.
            </template>

            <!-- Title -->
            <Column
                field="title"
                header="Título"
                sortable
            />

            <!-- Release date -->
            <Column
                field="releaseDate"
                header="Data de Lançamento"
                sortable
            />

            <!-- Runtime -->
            <Column
                field="runtime"
                header="Duração (min)"
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
    name: 'MovieTable',
    components: {
        DataTable,
        Column,
        Button
    },
    props: {
        movies: {
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